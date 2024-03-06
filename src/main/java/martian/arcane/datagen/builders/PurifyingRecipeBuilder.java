package martian.arcane.datagen.builders;

import martian.arcane.api.ListHelpers;
import martian.arcane.api.recipe.RecipeOutput;
import martian.arcane.common.registry.ArcaneRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class PurifyingRecipeBuilder extends AbstractSpellRecipeBuilder {
    public PurifyingRecipeBuilder(RecipeCategory category, RecipeSerializer<?> type, Ingredient input, NonNullList<RecipeOutput.DataGenHolder> results) {
        super(category, type, input, results);
    }

    public static PurifyingRecipeBuilder purifying(Ingredient input, NonNullList<RecipeOutput.DataGenHolder> results) {
        return new PurifyingRecipeBuilder(RecipeCategory.MISC, ArcaneRecipeTypes.PURIFYING_SERIALIZER.get(), input, results);
    }

    public static PurifyingRecipeBuilder purifying(Ingredient input, RecipeOutput.DataGenHolder result) {
        return purifying(input, ListHelpers.nonNullListOf(result));
    }

    public static PurifyingRecipeBuilder purifying(Ingredient input, Item item, int count) {
        return purifying(input, new RecipeOutput.DataGenHolder(item, count, 1));
    }
}
