package martian.arcane.api.spell;

import net.minecraft.world.item.ItemStack;

public interface ICastingSource {
    int getCastLevel(ItemStack stack);
}
