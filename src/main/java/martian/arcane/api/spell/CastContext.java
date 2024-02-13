package martian.arcane.api.spell;

import martian.arcane.api.Raycasting;
import martian.arcane.item.ItemAuraWand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class CastContext {
    public final Level level;
    public final ICastingSource.Type source;

    public CastContext(Level level, ICastingSource.Type source) {
        this.level = level;
        this.source = source;
    }

    @Nullable
    public BlockPos getTarget() {
        if (source == ICastingSource.Type.WAND) {
            CastContext.WandContext wandContext = (CastContext.WandContext)this;
            HitResult hit = wandContext.raycast();
            if (hit.getType() == HitResult.Type.BLOCK)
                return ((BlockHitResult)hit).getBlockPos();
        } else if (source == ICastingSource.Type.SPELL_CIRCLE) {
            return ((CastContext.SpellCircleContext)this).target;
        }
        return null;
    }

    public static final class WandContext extends CastContext {
        public final Player caster;
        public final InteractionHand castingHand;
        public final ItemStack castingStack;
        public final ItemAuraWand wand;

        public WandContext(Level level, Player caster, InteractionHand castingHand, ItemStack castingStack, ItemAuraWand wand) {
            super(level, ICastingSource.Type.WAND);
            this.caster = caster;
            this.castingHand = castingHand;
            this.castingStack = castingStack;
            this.wand = wand;
        }

        public HitResult raycast(boolean hitFluids) {
            return Raycasting.raycast(caster, 7.0f, hitFluids);
        }

        public HitResult raycast() {
            return raycast(false);
        }
    }

    public static final class SpellCircleContext extends CastContext {
        public final BlockPos target;

        public SpellCircleContext(Level level, BlockPos target) {
            super(level, ICastingSource.Type.SPELL_CIRCLE);
            this.target = target;
        }
    }
}
