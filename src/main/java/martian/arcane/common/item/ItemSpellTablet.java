package martian.arcane.common.item;

import martian.arcane.ArcaneConfig;
import martian.arcane.api.ArcaneRegistries;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.common.ArcaneContent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

public class ItemSpellTablet extends Item {
    public ItemSpellTablet() {
        super(new Properties().stacksTo(1));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> text, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, text, flag);
        text.add(Component.translatable("item.arcane.spell_tablet.tooltip"));
        if (hasSpell(stack)) {
            AbstractSpell spell = getSpellOrThrow(stack);
            text.add(Component
                    .translatable("messages.arcane.spell")
                    .append(spell.getSpellName())
                    .withStyle(ChatFormatting.AQUA));
            text.add(Component
                    .translatable("messages.arcane.spell_min_level")
                    .append(Integer.toString(spell.getMinLevel()))
                    .withStyle(ChatFormatting.AQUA));
            if (ArcaneConfig.disabledSpells.contains(getSpellId(stack)))
                text.add(Component.translatable("messages.arcane.spell_disabled").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.isEnchanted() || hasSpell(stack);
    }

    public static void setSpell(ResourceLocation newSpell, ItemStack stack) {
        stack.set(ArcaneContent.DC_SPELL, newSpell);
        if (!stack.has(DataComponents.CUSTOM_NAME))
            stack.set(DataComponents.CUSTOM_NAME, getSpellOrThrow(stack).getSpellName());
    }

    public static AbstractSpell getSpellOrThrow(ItemStack stack) {
        return Objects.requireNonNull(ArcaneRegistries.SPELLS.get(getSpellId(stack)));
    }

    public static boolean hasSpell(ItemStack stack) {
        return getSpellId(stack) != null;
    }

    public static @Nullable ResourceLocation getSpellId(ItemStack stack) {
        return stack.get(ArcaneContent.DC_SPELL);
    }
}
