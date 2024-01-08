package martian.arcane.api.item;

import martian.arcane.api.MathHelpers;
import martian.arcane.api.Raycasting;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.registry.ArcaneCapabilities;
import martian.arcane.api.capability.AuraStorageItemProvider;
import martian.arcane.api.capability.IAuraStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractAuraItem extends Item {
    private final int maxAura;
    private final boolean extractable;
    private final boolean receivable;

    public AbstractAuraItem(int maxAura, boolean extractable, boolean receivable, Item.Properties properties) {
        super(properties);
        this.maxAura = maxAura;
        this.extractable = extractable;
        this.receivable = receivable;
    }

    // Capability
    @Override
    @Nullable
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new AuraStorageItemProvider(stack, maxAura, extractable, receivable);
    }

    public Optional<IAuraStorage> getAuraStorage(@NotNull ItemStack stack) {
        return stack.getCapability(ArcaneCapabilities.AURA_STORAGE).resolve();
    }

    public <U> Optional<U> mapAuraStorage(ItemStack stack, Function<? super IAuraStorage, ? extends U> func) {
        return getAuraStorage(stack).map(func);
    }

    public boolean canExtractFrom(ItemStack stack) {
        return mapAuraStorage(stack, IAuraStorage::canExtract).orElse(false);
    }

    // Durability bar thingy
    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        return Math.round(mapAuraStorage(stack, aura -> aura.getAura() * 13.0F / aura.getMaxAura()).orElse(1F));
    }

    private static final int color_highAura = FastColor.ARGB32.color(155, 50, 155, 255);
    private static final int color_lowAura = FastColor.ARGB32.color(255, 50, 155, 255);

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        return mapAuraStorage(stack, aura -> (float)aura.getAura() / aura.getMaxAura() >= 0.5F ? color_highAura : color_lowAura)
                .orElse(color_highAura);
    }

    private final String AURA_TEXT = "Aura: %s/%s";

    // Tooltip
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> text, @NotNull TooltipFlag flags) {
        text.add(Component.literal(
                mapAuraStorage(stack, aura -> AURA_TEXT.formatted(aura.getAura(), aura.getMaxAura())).orElseThrow()
        ).withStyle(ChatFormatting.AQUA));
    }

    // Extract mana from another item or block
    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        final IAuraStorage aura = getAuraStorage(stack).orElseThrow();

        if (aura.getAura() >= aura.getMaxAura())
            return InteractionResultHolder.pass(stack);

        if (level.isClientSide())
            return InteractionResultHolder.success(stack);

        if (player.getOffhandItem().getCapability(ArcaneCapabilities.AURA_STORAGE).isPresent()) {
            ItemStack other = stack == player.getMainHandItem() ? player.getOffhandItem() : player.getMainHandItem();
            final IAuraStorage otherAura = getAuraStorage(other).orElseThrow();

            if (otherAura.canExtract())
            {
                aura.extractAuraFrom(otherAura, -1);
                player.sendSystemMessage(Component.translatable("messages.arcane.aura_extracted"));
                return InteractionResultHolder.success(stack);
            }
            else {
                player.sendSystemMessage(Component.translatable("messages.arcane.offhand_not_extractable"));
                return InteractionResultHolder.fail(stack);
            }
        } else {
//            HitResult hit = player.pick(player.getBlockReach(), 0, false);
//            if (hit.getType() != HitResult.Type.BLOCK) {
//                return InteractionResultHolder.pass(stack);
//            }
//            BlockHitResult blockHit = (BlockHitResult)hit;
            BlockHitResult hit = Raycasting.blockRaycast(player, player.getBlockReach(), false);
            if (hit == null) {
                return InteractionResultHolder.pass(stack);
            }
            BlockState block = level.getBlockState(hit.getBlockPos());

            if (!block.hasBlockEntity())
            {
                player.sendSystemMessage(Component.translatable("messages.arcane.offhand_not_extractable"));
                return InteractionResultHolder.fail(stack);
            }

            BlockEntity be = level.getBlockEntity(hit.getBlockPos());

            if (be instanceof AbstractAuraBlockEntity auraBe) {
                Optional<IAuraStorage> auraStorageOptional = auraBe.getAuraStorage();
                if (auraStorageOptional.isEmpty()) {
                    player.sendSystemMessage(Component.translatable("messages.arcane.offhand_not_extractable"));
                    return InteractionResultHolder.fail(stack);
                }

                IAuraStorage blockAura = auraStorageOptional.get();

                if (blockAura.getAura() <= 0)
                {
                    player.sendSystemMessage(Component.translatable("messages.arcane.out_of_aura"));
                    return InteractionResultHolder.fail(stack);
                }

                aura.extractAuraFrom(blockAura, -1);
                player.sendSystemMessage(Component.translatable("messages.arcane.aura_extracted"));
                return InteractionResultHolder.success(stack);
            }
            else
            {
                player.sendSystemMessage(Component.translatable("messages.arcane.offhand_not_extractable"));
                return InteractionResultHolder.fail(stack);
            }
        }
    }
}
