package martian.arcane.spells;

import martian.arcane.api.recipe.SimpleContainer;
import martian.arcane.api.spell.AbstractCraftingSpell;
import martian.arcane.item.ItemAuraWand;
import martian.arcane.recipe.RecipePurifying;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class SpellPurifying extends AbstractCraftingSpell<RecipePurifying> {
    public SpellPurifying() {
        super(2);
    }

    @Override
    public int getAuraCost(ItemAuraWand wand, ItemStack stack) {
        return 4;
    }

    @Override
    protected Optional<RecipePurifying> getRecipeFor(Level level, SimpleContainer container) {
        return RecipePurifying.getRecipeFor(level, container);
    }
}
