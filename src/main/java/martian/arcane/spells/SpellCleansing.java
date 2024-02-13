package martian.arcane.spells;

import martian.arcane.api.recipe.SimpleContainer;
import martian.arcane.api.spell.AbstractCraftingSpell;
import martian.arcane.item.ItemAuraWand;
import martian.arcane.recipe.RecipeCleansing;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class SpellCleansing extends AbstractCraftingSpell<RecipeCleansing> {
    public SpellCleansing() {
        super(2);
    }

    @Override
    public int getAuraCost(ItemAuraWand wand, ItemStack stack) {
        return 1;
    }

    @Override
    protected Optional<RecipeCleansing> getRecipeFor(Level level, SimpleContainer container) {
        return RecipeCleansing.getRecipeFor(level, container);
    }
}
