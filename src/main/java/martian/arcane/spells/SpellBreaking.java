package martian.arcane.spells;

import martian.arcane.api.AOEHelpers;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.spell.ICastingSource;
import martian.arcane.item.ItemAuraWand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class SpellBreaking extends AbstractSpell {
    public SpellBreaking() {
        super(1);
    }

    @Override
    public int getAuraCost(ItemAuraWand wand, ItemStack stack) {
        return wand.level >= 3 ? 3 : 2;
    }

    @Override
    public void cast(CastContext c) {
        if (c.level.isClientSide())
            return;

        if (c.source == ICastingSource.Type.WAND) {
            CastContext.WandContext wc = (CastContext.WandContext)c;
            HitResult result = wc.raycast();
            if (result.getType() != HitResult.Type.BLOCK)
                return;

            BlockHitResult bHit = (BlockHitResult)result;
            if (wc.wand.level == 1 || wc.caster.isCrouching())
                tryBreak(c.level, bHit.getBlockPos());
            else
                AOEHelpers.streamAOE(bHit.getBlockPos(), bHit.getDirection(), getRadius(wc.wand)).forEach(pos -> tryBreak(c.level, pos));
        } else if (c.source == ICastingSource.Type.SPELL_CIRCLE) {
            tryBreak(c.level, c.getTarget());
        }
    }

    private static void tryBreak(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getDestroySpeed(level, pos) >= 0) {
            level.destroyBlock(pos, true);
        }
    }

    private static int getRadius(ItemAuraWand wand) {
        return switch (wand.level) {
            case 2 -> 1;
            case 3 -> 2;
            default -> 0;
        };
    }
}
