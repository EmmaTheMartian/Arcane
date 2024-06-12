package martian.arcane.api.item;

import martian.arcane.api.aura.AuraRecord;
import martian.arcane.api.aura.AuraStorage;
import martian.arcane.api.aura.IMutableAuraStorage;
import martian.arcane.common.ArcaneContent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public abstract class AbstractAuraItem extends Item {
    private final int defaultMaxAura;
    private final boolean defaultExtractable, defaultInsertable;

    public AbstractAuraItem(int maxAura, boolean extractable, boolean insertable, Item.Properties properties) {
        super(properties.component(ArcaneContent.DC_AURA, new AuraRecord(maxAura, 0, extractable, insertable)));
        this.defaultMaxAura = maxAura;
        this.defaultExtractable = extractable;
        this.defaultInsertable = insertable;
    }

    // Aura storage
    public AuraRecord getAuraStorage(@NotNull ItemStack stack) {
        return AuraStorage.getOrCreate(stack, () -> new AuraRecord(defaultMaxAura, 0, defaultExtractable, defaultInsertable));
    }

    public <U> U mapAuraStorage(ItemStack stack, Function<? super AuraRecord, ? extends U> func) {
        return func.apply(getAuraStorage(stack));
    }

    public void voidMapAuraStorage(ItemStack stack, Consumer<? super AuraRecord> func) {
        func.accept(getAuraStorage(stack));
    }

    public void mutateAuraStorage(ItemStack stack, UnaryOperator<IMutableAuraStorage> func) {
        stack.set(ArcaneContent.DC_AURA, new AuraRecord(func.apply(getAuraStorage(stack).unfreeze())));
    }

    public boolean canExtractFrom(ItemStack stack) {
        return mapAuraStorage(stack, AuraRecord::canExtract);
    }

    // Durability bar thingy
    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        return Math.round(mapAuraStorage(stack, aura -> aura.aura() * 13.0F / aura.maxAura()));
    }

    private static final int color_highAura = FastColor.ARGB32.color(155, 50, 155, 255);
    private static final int color_lowAura = FastColor.ARGB32.color(255, 50, 155, 255);

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        return mapAuraStorage(stack, aura -> (float)aura.aura() / aura.maxAura() >= 0.5F ? color_highAura : color_lowAura);
    }

    // Tooltip
    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> text, TooltipFlag flag) {
        super.appendHoverText(stack, context, text, flag);
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
