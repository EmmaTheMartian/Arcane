package martian.arcane.common.item;

import martian.arcane.ArcaneConfig;
import martian.arcane.api.ArcaneRegistries;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.client.particle.MagicParticle;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ItemWandbook extends AbstractWandbookItem {
    public ItemWandbook(Supplier<Integer> maxWandsSupplier, Supplier<Integer> maxAuraSupplier) {
        super(maxWandsSupplier, maxAuraSupplier, true, new Properties().stacksTo(1));
    }

    public ItemWandbook() {
        this(() -> ArcaneConfig.wandbookSpellCapacity, () -> ArcaneConfig.wandbookAuraCapacity);
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        int slot = getData(stack).selection();

        if (player.getCooldowns().isOnCooldown(this) || !wandHasSpell(stack, slot))
            return InteractionResultHolder.fail(stack);

        AbstractSpell spell = getWandSpell(stack, slot);
        if (spell == null)
            return InteractionResultHolder.fail(stack);

        mutateAuraStorage(stack, aura -> {
            CastContext.WandContext ctx = new CastContext.WandContext(level, aura, player, hand, stack, this);

            if (!ctx.tryCast(spell).failed()) {
                MagicParticle.spawn(level, player.position(), player.getEyeHeight() - .2f, ArcaneRegistries.PIGMENTS.get(getData(stack).pigment()));
                player.getCooldowns().addCooldown(this, spell.getCooldownTicks(ctx));
            }

            return aura;
        });

        return InteractionResultHolder.success(stack);
    }
}
