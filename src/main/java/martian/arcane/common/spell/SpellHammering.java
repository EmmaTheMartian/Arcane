package martian.arcane.common.spell;

import martian.arcane.api.recipe.SimpleContainer;
import martian.arcane.api.spell.AbstractCraftingSpell;
import martian.arcane.common.recipe.RecipeHammering;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class SpellHammering extends AbstractCraftingSpell<RecipeHammering> {
    public SpellHammering() {
        super(2);
    }

    @Override
    public int getAuraCost(int level) {
        return 2;
    }

    @Override
    protected Optional<RecipeHammering> getRecipeFor(Level level, SimpleContainer container) {
        return RecipeHammering.getRecipeFor(level, container);
    }
}
