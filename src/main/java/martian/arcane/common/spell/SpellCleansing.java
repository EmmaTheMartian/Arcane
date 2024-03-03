package martian.arcane.common.spell;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.recipe.SimpleContainer;
import martian.arcane.api.spell.AbstractCraftingSpell;
import martian.arcane.common.recipe.RecipeCleansing;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class SpellCleansing extends AbstractCraftingSpell<RecipeCleansing> {
    public SpellCleansing() {
        super(ArcaneStaticConfig.SpellMinLevels.CLEANSING, ArcaneStaticConfig.SpellCosts.CLEANSING);
    }

    @Override
    protected Optional<RecipeCleansing> getRecipeFor(Level level, SimpleContainer container) {
        return RecipeCleansing.getRecipeFor(level, container);
    }
}
