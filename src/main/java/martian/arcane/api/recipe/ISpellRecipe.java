package martian.arcane.api.recipe;

import net.minecraft.core.NonNullList;

public interface ISpellRecipe {
    NonNullList<RecipeOutput> getRecipeOutput();
}
