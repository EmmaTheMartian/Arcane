package martian.arcane.common.spell;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.block.IPreservable;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.spell.CastResult;
import martian.arcane.integration.photon.ArcaneFx;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SpellPreservation extends AbstractSpell {
    public SpellPreservation() {
        super(ArcaneStaticConfig.SpellMinLevels.PRESERVATION);
    }

    @Override
    public CastResult cast(CastContext c) {
        if (c.level.isClientSide)
            return CastResult.PASS;

        BlockPos pos = c.getTarget();
        if (pos == null)
            return CastResult.FAILED;

        BlockState state = c.level.getBlockState(pos);
        if (state.getBlock() instanceof IPreservable preservable) {
            ArcaneFx.ON_CAST_GRAVITY.goBlock(c.level, pos.above());
            preservable.onPreserve(c.level, pos, state, c);
            return new CastResult(ArcaneStaticConfig.SpellCosts.PRESERVATION, false);
        }

        return CastResult.FAILED;
    }
}
