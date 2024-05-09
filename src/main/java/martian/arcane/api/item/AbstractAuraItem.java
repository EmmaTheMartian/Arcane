package martian.arcane.api.item;

import martian.arcane.api.capability.aura.AuraStorageItemProvider;
import martian.arcane.api.capability.aura.IAuraStorage;
import martian.arcane.common.registry.ArcaneCapabilities;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
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

    // Tooltip
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> text, @NotNull TooltipFlag flags) {
        super.appendHoverText(stack, level, text, flags);
        mapAuraStorage(stack, aura -> {
            text.add(Component
                    .translatable("messages.arcane.aura")
                    .append(Integer.toString(aura.getAura()))
                    .append(" / ")
                    .append(Integer.toString(aura.getMaxAura()))
                    .withStyle(ChatFormatting.AQUA));
            return null;
        });
    }
}
