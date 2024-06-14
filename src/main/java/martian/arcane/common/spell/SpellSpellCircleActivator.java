package martian.arcane.common.spell;

import martian.arcane.ArcaneMod;
import martian.arcane.api.block.BlockHelpers;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.spell.CastResult;
import martian.arcane.api.spell.SpellConfig;
import martian.arcane.common.block.spellcircle.BlockEntitySpellCircle;
import martian.arcane.integration.photon.ArcaneFx;
import net.minecraft.core.BlockPos;

public class SpellSpellCircleActivator extends AbstractSpell {
    private static final SpellConfig config = SpellConfig.basicConfig(ArcaneMod.id("activator"), 16, 60, 2).build();

    @Override
    protected SpellConfig getConfig() {
        return config;
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
