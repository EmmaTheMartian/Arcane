package martian.arcane.common.spell;

import martian.arcane.ArcaneMod;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.spell.CastResult;
import martian.arcane.api.spell.SpellConfig;
import net.minecraft.world.phys.Vec3;

public class SpellDashing extends AbstractSpell {
    private static final SpellConfig config = SpellConfig
            .basicConfig(ArcaneMod.id("dashing"), 2, 10, 1)
            .set("multiplierAtLevel1", 2d)
            .set("multiplierAtLevel2", 2.5d)
            .set("multiplierAtLevel3", 3d)
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
        double m = getMultiplier(wc);
        Vec3 look = wc.caster.getLookAngle().normalize();
        wc.caster.push(look.x * m, look.y * m, look.z * m);
        return CastResult.SUCCESS;
    }

    public static double getMultiplier(CastContext context) {
        return switch (context.source.getCastLevel(context)) {
            case 1 -> config.get("multiplierAtLevel1");
            case 2 -> config.get("multiplierAtLevel2");
            default -> config.get("multiplierAtLevel3");
        };
    }
}
