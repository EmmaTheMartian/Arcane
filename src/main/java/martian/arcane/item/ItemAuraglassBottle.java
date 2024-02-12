package martian.arcane.item;

import martian.arcane.api.NBTHelpers;
import martian.arcane.api.capability.IAuraStorage;
import martian.arcane.api.item.AbstractAuraItem;
import martian.arcane.registry.ArcaneCapabilities;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemAuraglassBottle extends AbstractAuraItem {
    public final int pushRate;

    public ItemAuraglassBottle(int maxAura, int pushRate) {
        super(maxAura, true, true, new Item.Properties().stacksTo(1));
        this.pushRate = pushRate;
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide())
            return InteractionResultHolder.success(stack);

        if (player.isCrouching()) {
            CompoundTag nbt = stack.getOrCreateTag();
            initNBT(nbt);
            nbt.putBoolean(NBTHelpers.KEY_ACTIVE, !nbt.getBoolean(NBTHelpers.KEY_ACTIVE));
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.isEnchanted() || isActive(stack);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide())
            return;

        if (entity instanceof Player player && isActive(stack)) {
            ItemStack held = player.getMainHandItem();
            if (held.isEmpty())
                return;

            LazyOptional<IAuraStorage> heldCap = held.getCapability(ArcaneCapabilities.AURA_STORAGE);

            if (heldCap.isPresent()) {
                IAuraStorage heldAura = heldCap.resolve().orElseThrow();
                if (heldAura.canInsert()) {
                    mapAuraStorage(stack, bottleAura -> {
                        bottleAura.sendAuraTo(heldAura, getPushRate(stack));
                        return null;
                    });
                }
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> text, @NotNull TooltipFlag flags) {
        super.appendHoverText(stack, level, text, flags);

        text.add(Component
                .translatable("messages.arcane.push_rate")
                .append(Integer.toString(getPushRate(stack)))
                .withStyle(ChatFormatting.AQUA));
    }

    public int getPushRate(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        initNBT(nbt);
        return nbt.getInt(NBTHelpers.KEY_PUSH_RATE);
    }

    public boolean isActive(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        initNBT(nbt);
        return nbt.getBoolean(NBTHelpers.KEY_ACTIVE);
    }

    public void initNBT(CompoundTag nbt) {
        initNBT(nbt, pushRate);
    }

    public static void initNBT(CompoundTag nbt, int pushRate) {
        NBTHelpers.init(nbt, NBTHelpers.KEY_ACTIVE, (_nbt, key) -> nbt.putBoolean(key, false));
        NBTHelpers.init(nbt, NBTHelpers.KEY_PUSH_RATE, (_nbt, key) -> nbt.putInt(key, pushRate));
    }
}
