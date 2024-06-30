package martian.arcane.api.spell;

import martian.arcane.api.Raycasting;
import martian.arcane.api.aura.IAuraStorage;
import martian.arcane.api.item.IAuraWand;
import martian.arcane.common.block.spellcircle.BlockEntitySpellCircle;
import martian.arcane.common.block.spellcircle.BlockSpellCircle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public abstract class CastContext {
    public final Level level;
    public final IAuraStorage aura;
    public final ICastingSource source;
    public final CastTarget<?> target;

    public CastContext(Level level, IAuraStorage aura, ICastingSource source, CastTarget<?> target) {
        this.level = level;
        this.aura = aura;
        this.source = source;
        this.target = target;
    }

    public final CastResult cast(AbstractSpell spell) {
        if (AbstractSpell.isDisabled(spell))
            return CastResult.FAIL_DISABLED;
        return spell.cast(this);
    }

    public static final class WandContext extends CastContext {
        public final Player caster;
        public final InteractionHand castingHand;
        public final ItemStack castingStack;
        public final IAuraWand wand;

        public WandContext(Level level, IAuraStorage aura, Player caster, InteractionHand castingHand, ItemStack castingStack, IAuraWand wand) {
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

    public static final class SpellCircleContext extends CastContext {
        public final BlockPos target;

        public SpellCircleContext(Level level, IAuraStorage aura, BlockPos target, BlockEntitySpellCircle circle) {
            super(level, aura, circle, CastTarget.targetBlock(target));
            this.target = target;
        }

        public SpellCircleContext(BlockEntitySpellCircle circle) {
            this(circle.getLevel(), circle, circle.getBlockPos().relative(circle.getBlockState().getValue(BlockSpellCircle.FACING)), circle);
        }
    }
}
