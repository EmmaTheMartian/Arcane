package martian.arcane.common.spell;

import martian.arcane.api.recipe.SimpleContainer;
import martian.arcane.api.spell.AbstractCraftingSpell;
import martian.arcane.common.recipe.RecipeCleansing;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class SpellCleansing extends AbstractCraftingSpell<RecipeCleansing> {
    public SpellCleansing() {
        super(2);
    }

    @Override
    public int getAuraCost(int level) {
        return 2;
    }

    @Override
    protected Optional<RecipeCleansing> getRecipeFor(Level level, SimpleContainer container) {
        return RecipeCleansing.getRecipeFor(level, container);
    }
}
