package martian.arcane.spells;

import martian.arcane.api.recipe.SimpleContainer;
import martian.arcane.item.ItemAuraWand;
import martian.arcane.recipe.RecipeHammering;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class SpellHammering extends AbstractCraftingSpell<RecipeHammering> {
    public SpellHammering() {
        super(2);
    }

    @Override
    public int getAuraCost(ItemAuraWand wand, ItemStack stack) {
        return 2;
    }

    @Override
    protected Optional<RecipeHammering> getRecipeFor(Level level, SimpleContainer container) {
        return RecipeHammering.getRecipeFor(level, container);
    }
}
