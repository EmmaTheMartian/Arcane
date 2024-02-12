package martian.arcane.spells;

import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.item.ItemAuraWand;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
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
    public void cast(ItemAuraWand wand, ItemStack stack, Level level, Player caster, InteractionHand castHand, HitResult hit) {
        double m = getMultiplier(wand);
        Vec3 look = caster.getLookAngle().normalize();
        caster.push(look.x * m, look.y * m, look.z * m);
    }

    public static double getMultiplier(ItemAuraWand wand) {
        return switch (wand.level) {
            case 2 -> 2.5;
            case 3 -> 3;
            default -> 2;
        };
    }
}
