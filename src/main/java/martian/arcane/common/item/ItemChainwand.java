package martian.arcane.common.item;

import martian.arcane.api.ArcaneRegistries;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.wand.WandData;
import martian.arcane.client.particle.MagicParticle;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;

public class ItemChainwand extends AbstractWandbookItem {
    public ItemChainwand(Supplier<Integer> maxWandsSupplier, Supplier<Integer> maxAuraSupplier, int castLevel) {
        super(maxWandsSupplier, maxAuraSupplier, false, new Properties().stacksTo(1));
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        int slot = getData(stack).selection();

        if (player.getCooldowns().isOnCooldown(this) || !wandHasSpell(stack, slot)) {
            return InteractionResultHolder.fail(stack);
        }

        List<ItemStack> wands = getWands(stack);
        if (wands.isEmpty()) {
            return InteractionResultHolder.pass(stack);
        }

        mutateAuraStorage(stack, aura -> {
            CastContext.WandContext ctx = new CastContext.WandContext(level, aura, player, hand, stack, this);

            int cooldownTicks = 0, successfulCasts = 0;

            for (ItemStack wand : wands) {
                AbstractSpell spell = ItemWand.getSpell(wand);
                if (spell == null) {
                    return aura;
                }

                if (ctx.tryCast(spell).failed()) {
                    break;
                }

                cooldownTicks += spell.getCooldownTicks(ctx);
                successfulCasts++;
            }

            int averageCooldownTicks = (int) (((float) cooldownTicks / (float) successfulCasts) * 2f);
            MagicParticle.spawn(level, player.position(), player.getEyeHeight() - .2f, ArcaneRegistries.PIGMENTS.get(getData(stack).pigment()));
            player.getCooldowns().addCooldown(this, averageCooldownTicks);

            return aura;
        });

        return InteractionResultHolder.success(stack);
    }

    @Override
    public int getCastLevel(CastContext context) {
        return WandData.getOrCreate(((CastContext.WandContext) context).castingStack, () -> WandData.DEFAULT.withLevel(3)).level();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> text, TooltipFlag flag) {
        text.add(Component.literal("Casts each wand in order.")); //todo: translatable
        super.appendHoverText(stack, context, text, flag);
    }
}
