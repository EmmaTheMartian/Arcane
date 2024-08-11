package martian.arcane.common.spell;

import martian.arcane.ArcaneMod;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.spell.CastResult;
import martian.arcane.api.spell.SpellConfig;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

public class SpellEnlarging extends AbstractSpell {
    private static final SpellConfig config = SpellConfig
            .basicConfig(ArcaneMod.id("enlarging"), 2, 10, 1)
            .set("enlargementFactor", 0.25d)
            .set("maxScale", 2d)
            .build();

    @Override
    protected SpellConfig getConfig() {
        return config;
    }

    @Override
    public boolean canCastFromContext(CastContext c) {
        return c instanceof CastContext.WandContext;
    }

    @Override
    public CastResult cast(CastContext c) {
        CastContext.WandContext wc = ((CastContext.WandContext) c);

        final ScaleData data = ScaleTypes.BASE.getScaleData(wc.caster);
        if (data.getTargetScale() < ((double) config.get("maxScale"))) {
            data.setTargetScale((float) (data.getTargetScale() + ((double) config.get("enlargementFactor"))));
            return CastResult.SUCCESS;
        } else {
            return CastResult.FAILED;
        }
    }
}
