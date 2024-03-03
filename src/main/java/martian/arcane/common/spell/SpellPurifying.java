package martian.arcane.common.spell;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.recipe.SimpleContainer;
import martian.arcane.api.spell.AbstractCraftingSpell;
import martian.arcane.common.recipe.RecipePurifying;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class SpellPurifying extends AbstractCraftingSpell<RecipePurifying> {
    public SpellPurifying() {
        super(ArcaneStaticConfig.SpellMinLevels.PURIFYING, ArcaneStaticConfig.SpellCosts.PURIFYING);
    }

    @Override
    protected Optional<RecipePurifying> getRecipeFor(Level level, SimpleContainer container) {
        return RecipePurifying.getRecipeFor(level, container);
    }
}
