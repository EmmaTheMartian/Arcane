package martian.arcane.common.spell;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.block.BlockHelpers;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.spell.CastResult;
import martian.arcane.common.block.spellcircle.BlockEntitySpellCircle;
import martian.arcane.integration.photon.ArcaneFx;
import net.minecraft.core.BlockPos;

public class SpellSpellCircleActivator extends AbstractSpell {
    public SpellSpellCircleActivator() {
        super(ArcaneStaticConfig.SpellMinLevels.ACTIVATOR);
    }

    @Override
    public int getCooldownTicks(CastContext c) {
        return 60;
    }

    @Override
    public int getAuraCost(CastContext c) {
        return ArcaneStaticConfig.SpellCosts.ACTIVATOR;
    }

    @Override
    public CastResult cast(CastContext c) {
        if (c.level.isClientSide)
            return CastResult.SUCCESS;

        BlockPos pos = c.getTarget();
        if (pos == null)
            return CastResult.FAILED;

        if (c.level.getBlockEntity(pos) instanceof BlockEntitySpellCircle spellCircle && !spellCircle.getActive()) {
            ArcaneFx.ON_CAST_GRAVITY.goBlock(c.level, pos);
            ArcaneFx.SPELL_CIRCLE_INIT.goBlock(c.level, pos);
            spellCircle.setActive(true);
            BlockHelpers.sync(spellCircle);
            return CastResult.SUCCESS;
        }

        return CastResult.FAILED;
    }
}
