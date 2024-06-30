package martian.arcane.common.spell;

import martian.arcane.ArcaneMod;
import martian.arcane.api.block.BlockHelpers;
import martian.arcane.api.spell.*;
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

        if (c.target.type() == CastTarget.Type.BLOCK) {
            BlockPos pos = ((BlockPos) c.target.value());
            if (c.level.getBlockEntity(pos) instanceof BlockEntitySpellCircle spellCircle && !spellCircle.getActive()) {
                ArcaneFx.ON_CAST_GRAVITY.goBlock(c.level, pos);
                ArcaneFx.SPELL_CIRCLE_INIT.goBlock(c.level, pos);
                spellCircle.setActive(true);
                BlockHelpers.sync(spellCircle);
                return CastResult.SUCCESS;
            }
        }

        return CastResult.FAILED;
    }
}
