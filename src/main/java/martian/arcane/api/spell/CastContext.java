package martian.arcane.api.spell;

import martian.arcane.api.Raycasting;
import martian.arcane.api.aura.IAuraStorage;
import martian.arcane.common.block.spellcircle.BlockEntitySpellCircle;
import martian.arcane.common.item.ItemAuraWand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public abstract class CastContext {
    public final Level level;
    public final IAuraStorage aura;
    public final ICastingSource source;

    public CastContext(Level level, IAuraStorage aura, ICastingSource source) {
        this.level = level;
        this.aura = aura;
        this.source = source;
    }

    public abstract @Nullable BlockPos getTarget();

    public static final class WandContext extends CastContext {
        public final Player caster;
        public final InteractionHand castingHand;
        public final ItemStack castingStack;
        public final ItemAuraWand wand;

        public WandContext(Level level, IAuraStorage aura, Player caster, InteractionHand castingHand, ItemStack castingStack, ItemAuraWand wand) {
            super(level, aura, wand);
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

        public @Nullable BlockPos getTarget() {
            HitResult hit = raycast();
            if (hit.getType() == HitResult.Type.BLOCK)
                return ((BlockHitResult)hit).getBlockPos();
            return null;
        }
    }

    public static final class SpellCircleContext extends CastContext {
        public final BlockPos target;

        public SpellCircleContext(Level level, IAuraStorage aura, BlockPos target, BlockEntitySpellCircle circle) {
            super(level, aura, circle);
            this.target = target;
        }

        public SpellCircleContext(BlockEntitySpellCircle circle) {
            this(circle.getLevel(), circle, circle.getBlockPos().below(), circle);
        }

        public @Nullable BlockPos getTarget() {
            return target;
        }
    }
}
