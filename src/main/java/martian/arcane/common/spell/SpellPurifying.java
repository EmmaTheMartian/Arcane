package martian.arcane.common.spell;

import martian.arcane.api.recipe.SimpleContainer;
import martian.arcane.api.spell.AbstractCraftingSpell;
import martian.arcane.common.recipe.RecipePurifying;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class SpellPurifying extends AbstractCraftingSpell<RecipePurifying> {
    public SpellPurifying() {
        super(2);
    }

    @Override
    public int getAuraCost(int level) {
        return 4;
    }

    @Override
    protected Optional<RecipePurifying> getRecipeFor(Level level, SimpleContainer container) {
        return RecipePurifying.getRecipeFor(level, container);
    }
}
