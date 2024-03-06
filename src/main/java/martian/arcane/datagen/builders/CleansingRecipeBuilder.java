package martian.arcane.datagen.builders;

import martian.arcane.api.ListHelpers;
import martian.arcane.api.recipe.RecipeOutput;
import martian.arcane.common.registry.ArcaneRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CleansingRecipeBuilder extends AbstractSpellRecipeBuilder {
    public CleansingRecipeBuilder(RecipeCategory category, RecipeSerializer<?> type, Ingredient input, NonNullList<RecipeOutput.DataGenHolder> results) {
        super(category, type, input, results);
    }

    public static CleansingRecipeBuilder cleansing(Ingredient input, NonNullList<RecipeOutput.DataGenHolder> results) {
        return new CleansingRecipeBuilder(RecipeCategory.MISC, ArcaneRecipeTypes.CLEANSING_SERIALIZER.get(), input, results);
    }

    public static CleansingRecipeBuilder cleansing(Ingredient input, RecipeOutput.DataGenHolder result) {
        return cleansing(input, ListHelpers.nonNullListOf(result));
    }

    public static CleansingRecipeBuilder cleansing(Ingredient input, Item item, int count) {
        return cleansing(input, new RecipeOutput.DataGenHolder(item, count, 1));
    }
}
