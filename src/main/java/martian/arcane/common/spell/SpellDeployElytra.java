package martian.arcane.common.spell;

import martian.arcane.ArcaneMod;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.spell.CastResult;
import martian.arcane.api.spell.SpellConfig;
import martian.arcane.api.IHasFakeElytra;
import net.minecraft.world.entity.EquipmentSlot;

public class SpellDeployElytra extends AbstractSpell {
    private static final SpellConfig config = SpellConfig
            .basicConfig(ArcaneMod.id("deploy_elytra"), 4, 40, 3)
            .set("requireEquippedElytra", false)
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
        if (config.<Boolean>get("requireEquippedElytra") && !wc.caster.getInventory().armor.get(EquipmentSlot.CHEST.getIndex()).canElytraFly(wc.caster)) {
            return CastResult.FAILED;
        }

        ((IHasFakeElytra) wc.caster).arcane$setHasFakeElytra(true);
        wc.caster.startFallFlying();

        return CastResult.FAILED;
    }
}
