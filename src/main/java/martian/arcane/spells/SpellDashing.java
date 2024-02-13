package martian.arcane.spells;

import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.spell.ICastingSource;
import martian.arcane.item.ItemAuraWand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class SpellDashing extends AbstractSpell {
    public SpellDashing() {
        super(1);
    }

    @Override
    public int getAuraCost(ItemAuraWand wand, ItemStack stack) {
        return 2;
    }

    @Override
    public void cast(CastContext c) {
        if (c.source == ICastingSource.Type.WAND) {
            CastContext.WandContext wandContext = (CastContext.WandContext)c;
            double m = getMultiplier(wandContext.wand);
            Vec3 look = wandContext.caster.getLookAngle().normalize();
            wandContext.caster.push(look.x * m, look.y * m, look.z * m);
        }
    }

    public static double getMultiplier(ItemAuraWand wand) {
        return switch (wand.level) {
            case 2 -> 2.5;
            case 3 -> 3;
            default -> 2;
        };
    }
}
