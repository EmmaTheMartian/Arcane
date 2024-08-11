package martian.arcane.api.spell;

import martian.arcane.api.Raycasting;
import martian.arcane.api.aura.IMutableAuraStorage;
import martian.arcane.api.item.IAuraWand;
import martian.arcane.common.block.spellcircle.BlockEntitySpellCircle;
import martian.arcane.common.block.spellcircle.BlockSpellCircle;
import martian.arcane.common.item.ItemWand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public abstract class CastContext {
    public final Level level;
    public final IMutableAuraStorage aura;
    public final ICastingSource source;
    public final CastTarget<?> target;

    public CastContext(Level level, IMutableAuraStorage aura, ICastingSource source, CastTarget<?> target) {
        this.level = level;
        this.aura = aura;
        this.source = source;
        this.target = target;
    }

    /**
     * Cast a spell, failing if the source is out of Aura or if the spell is
     * disabled.
     * <p>
     * See {@link ItemWand#use(Level, Player, InteractionHand)}
     * for an example usage.
     * @param spell The spell to cast.
     * @return The result of the cast.
     */
    public CastResult tryCast(AbstractSpell spell) {
        int cost = spell.getAuraCost(this);

        if (AbstractSpell.isDisabled(spell)) {
            return CastResult.FAIL_DISABLED;
        } else if (aura.getAura() < cost) {
            return CastResult.FAIL_OUT_OF_AURA;
        } else if (source.getCastLevel(this) < spell.getMinLevel()) {
            return CastResult.FAIL_WAND_LEVEL_TOO_LOW;
        }

        CastResult result = forceCast(spell);

        if (!result.failed()) {
            aura.removeAura(cost);
        }

        return result;
    }

    /**
     * Forcefully cast a spell. Ignores if the spell is disabled and if the
     * source has enough Aura. If this is used then make sure to check if the
     * spell is disabled first!
     * <p>
     * See {@link CastContext#tryCast(AbstractSpell)} for a safe spell casting
     * @param spell The spell to cast.
     * @return The result of the cast.
     */
    public CastResult forceCast(AbstractSpell spell) {
        return spell.cast(this);
    }

    public static class WandContext extends CastContext {
        public final Player caster;
        public final InteractionHand castingHand;
        public final ItemStack castingStack;
        public final IAuraWand wand;

        public WandContext(Level level, IMutableAuraStorage aura, Player caster, InteractionHand castingHand, ItemStack castingStack, IAuraWand wand) {
            super(level, aura, wand, CastTarget.fromHitResult(Raycasting.raycast(caster, caster.entityInteractionRange(), false)));
            this.caster = caster;
            this.castingHand = castingHand;
            this.castingStack = castingStack;
            this.wand = wand;
        }

        public HitResult raycast(boolean hitFluids) {
            return Raycasting.raycast(caster, caster.entityInteractionRange(), hitFluids);
        }

        public HitResult raycast() {
            return raycast(false);
        }
    }

    public static class SpellCircleContext extends CastContext {
        public final BlockPos target;

        public SpellCircleContext(Level level, IMutableAuraStorage aura, BlockPos target, BlockEntitySpellCircle circle) {
            super(level, aura, circle, CastTarget.targetBlock(target));
            this.target = target;
        }

        public SpellCircleContext(BlockEntitySpellCircle circle) {
            this(circle.getLevel(), circle, circle.getBlockPos().relative(circle.getBlockState().getValue(BlockSpellCircle.FACING)), circle);
        }
    }
}
