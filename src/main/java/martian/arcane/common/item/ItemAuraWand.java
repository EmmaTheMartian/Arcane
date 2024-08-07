package martian.arcane.common.item;

import martian.arcane.api.ArcaneRegistries;
import martian.arcane.api.aura.AuraRecord;
import martian.arcane.api.item.AbstractAuraItem;
import martian.arcane.api.item.IAuraWand;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.common.ArcaneContent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ItemAuraWand extends AbstractAuraItem implements IAuraWand {
    public final int level;

    @Deprecated
    public ItemAuraWand(int maxAura, int level, Properties properties) {
        this(() -> maxAura, level, properties);
    }

    public ItemAuraWand(Supplier<Integer> maxAura, int level, Properties properties) {
        //noinspection DataFlowIssue
        super(() -> new AuraRecord(maxAura.get(), 0, false, true), properties.component(ArcaneContent.DC_SPELL.get(), null));
        this.level = level;
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(this) || !hasSpell(stack))
            return InteractionResultHolder.fail(stack);

        AbstractSpell spell = getSpell(stack);
        if (spell == null)
            return InteractionResultHolder.fail(stack);

        mutateAuraStorage(stack, aura -> {
            CastContext.WandContext ctx = new CastContext.WandContext(level, aura, player, hand, stack, this);

            int cost = spell.getAuraCost(ctx);
            if (aura.getAura() < cost) {
                return aura;
            }

            player.getCooldowns().addCooldown(this, spell.getCooldownTicks(ctx));

            ctx.cast(spell);
            aura.removeAura(cost);
            return aura;
        });

        return InteractionResultHolder.success(stack);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> text, TooltipFlag flag) {
        super.appendHoverText(stack, context, text, flag);

        if (hasSpell(stack))
            text.add(Component
                    .translatable("messages.arcane.spell")
                    .append(getSpellOrThrow(stack).getSpellName())
                    .withStyle(ChatFormatting.AQUA));
        else
            text.add(Component.translatable("messages.arcane.no_spell"));

        text.add(Component
                .translatable("messages.arcane.wand_level")
                .append(Integer.toString(this.level)));
    }

    // Spell casting stuffs
    public void setSpell(ResourceLocation newSpell, ItemStack stack) {
        stack.set(ArcaneContent.DC_SPELL, newSpell);
        if (!stack.has(DataComponents.CUSTOM_NAME))
            stack.set(DataComponents.CUSTOM_NAME, getSpellOrThrow(stack).getItemName());
    }

    public @Nullable AbstractSpell getSpell(ItemStack stack) {
        return ArcaneRegistries.SPELLS.get(getSpellId(stack));
    }

    public AbstractSpell getSpellOrThrow(ItemStack stack) {
        return Objects.requireNonNull(ArcaneRegistries.SPELLS.get(getSpellId(stack)));
    }

    @Override
    public int getCastLevel(CastContext context) {
        return level;
    }

    // Static methods
    public static void removeSpell(ItemStack stack) {
        stack.remove(ArcaneContent.DC_SPELL);
    }

    public static boolean hasSpell(ItemStack stack) {
        return getSpellId(stack) != null;
    }

    public static @Nullable ResourceLocation getSpellId(ItemStack stack) {
        return stack.get(ArcaneContent.DC_SPELL);
    }

    public static ItemStack oakWandOfSpell(ResourceLocation spell) {
        ItemStack stack = new ItemStack(ArcaneContent.WAND_OAK.get());
        stack.set(ArcaneContent.DC_SPELL, spell);
        return stack;
    }
}
