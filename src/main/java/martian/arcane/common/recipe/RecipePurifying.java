package martian.arcane.common.recipe;

import martian.arcane.ArcaneMod;
import martian.arcane.api.recipe.ISpellRecipe;
import martian.arcane.api.recipe.RecipeOutput;
import martian.arcane.api.recipe.SimpleContainer;
import martian.arcane.api.recipe.SimpleRecipe;
import martian.arcane.common.registry.ArcaneRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class RecipePurifying extends SimpleRecipe<SimpleContainer> implements ISpellRecipe {
    public static final String NAME = "purifying";
    public static final ResourceLocation ID = ArcaneMod.id(NAME);

    public RecipePurifying(ResourceLocation id, Ingredient input, NonNullList<RecipeOutput> results) {
        super(ArcaneRecipeTypes.PURIFYING.get(), id, input, results);
    }

    @Override
    public NonNullList<RecipeOutput> getRecipeOutput() {
        return this.results;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return new Serializer();
    }

    public static Optional<RecipePurifying> getRecipeFor(Level level, SimpleContainer container) {
        return getAllRecipes(level)
                .stream()
                .filter(recipe -> recipe.matches(container, level))
                .findFirst();
    }

    public static List<RecipePurifying> getAllRecipes(Level level) {
        return level.getRecipeManager().getAllRecipesFor(ArcaneRecipeTypes.PURIFYING.get());
    }

    public static class Serializer extends SimpleSerializer<RecipePurifying> {
        public Serializer() {
            super(RecipePurifying::new);
        }
    }
}
