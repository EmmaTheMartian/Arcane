package martian.arcane.common.recipe;

import martian.arcane.api.recipe.*;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class SpellRecipe extends SimpleRecipe<SimpleContainer> implements ISpellRecipe {
    private final SpellRecipeType type;

    public SpellRecipe(SpellRecipeType type, Ingredient input, NonNullList<RecipeOutput> results) {
        super(input, results);
        this.type = type;
    }

    @Override
    public NonNullList<RecipeOutput> getRecipeOutput() {
        return this.results;
    }

    @Override
    public @NotNull SpellRecipeType getSerializer() {
        return type;
    }

    @Override
    public @NotNull SpellRecipeType getType() {
        return type;
    }

    public static Optional<RecipeHolder<SpellRecipe>> getRecipeFor(SpellRecipeType type, Level level, SimpleContainer container) {
        return getAllRecipes(type, level)
                .stream()
                .filter(recipe -> recipe.value().matches(container, level))
                .findFirst();
    }

    public static List<RecipeHolder<SpellRecipe>> getAllRecipes(SpellRecipeType type, Level level) {
        return level.getRecipeManager().getAllRecipesFor(type);
    }
}
