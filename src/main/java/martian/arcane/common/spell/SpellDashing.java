package martian.arcane.common.spell;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.item.IAuraWand;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.spell.CastResult;
import martian.arcane.api.spell.ICastingSource;
import martian.arcane.common.item.ItemAuraWand;
import martian.arcane.integration.photon.ArcaneFx;
import net.minecraft.world.phys.Vec3;

public class SpellDashing extends AbstractSpell {
    public SpellDashing() {
        super(ArcaneStaticConfig.SpellMinLevels.DASHING);
    }

    @Override
    public int getCooldownTicks(CastContext c) {
        return 10;
    }

    @Override
    public int getAuraCost(CastContext c) {
        return c instanceof CastContext.WandContext ? ArcaneStaticConfig.SpellCosts.DASHING : 0;
    }

    @Override
    public CastResult cast(CastContext c) {
        if (c instanceof CastContext.WandContext wc) {
            double m = getMultiplier(wc.wand);
            Vec3 look = wc.caster.getLookAngle().normalize();

            if (!c.level.isClientSide)
                ArcaneFx.ON_CAST_CONSTANT.goEntity(c.level, wc.caster);

            wc.caster.push(look.x * m, look.y * m, look.z * m);

            return CastResult.SUCCESS;
        }

        return CastResult.FAILED;
    }

    public static double getMultiplier(ICastingSource source) {
        return switch (source.getCastLevel()) {
            case 2 -> 2.5;
            case 3 -> 3;
            default -> 2;
        };
    }
}
