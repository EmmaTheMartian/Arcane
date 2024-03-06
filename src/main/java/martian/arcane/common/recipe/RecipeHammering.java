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

public class RecipeHammering extends SimpleRecipe<SimpleContainer> implements ISpellRecipe {
    public static final String NAME = "hammering";
    public static final ResourceLocation ID = ArcaneMod.id(NAME);

    public RecipeHammering(ResourceLocation id, Ingredient input, NonNullList<RecipeOutput> results) {
        super(ArcaneRecipeTypes.HAMMERING.get(), id, input, results);
    }

    @Override
    public NonNullList<RecipeOutput> getRecipeOutput() {
        return this.results;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return new Serializer();
    }

    public static Optional<RecipeHammering> getRecipeFor(Level level, SimpleContainer container) {
        return getAllRecipes(level)
                .stream()
                .filter(recipe -> recipe.matches(container, level))
                .findFirst();
    }

    public static List<RecipeHammering> getAllRecipes(Level level) {
        return level.getRecipeManager().getAllRecipesFor(ArcaneRecipeTypes.HAMMERING.get());
    }

    public static class Serializer extends SimpleSerializer<RecipeHammering> {
        public Serializer() {
            super(RecipeHammering::new);
        }
    }
}
