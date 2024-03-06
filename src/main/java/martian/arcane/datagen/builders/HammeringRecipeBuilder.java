package martian.arcane.datagen.builders;

import martian.arcane.api.ListHelpers;
import martian.arcane.api.recipe.RecipeOutput;
import martian.arcane.common.registry.ArcaneRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class HammeringRecipeBuilder extends AbstractSpellRecipeBuilder {
    public HammeringRecipeBuilder(RecipeCategory category, RecipeSerializer<?> type, Ingredient input, NonNullList<RecipeOutput.DataGenHolder> results) {
        super(category, type, input, results);
    }

    public static HammeringRecipeBuilder hammering(Ingredient input, NonNullList<RecipeOutput.DataGenHolder> results) {
        return new HammeringRecipeBuilder(RecipeCategory.MISC, ArcaneRecipeTypes.HAMMERING_SERIALIZER.get(), input, results);
    }

    public static HammeringRecipeBuilder hammering(Ingredient input, RecipeOutput.DataGenHolder result) {
        return hammering(input, ListHelpers.nonNullListOf(result));
    }

    public static HammeringRecipeBuilder hammering(Ingredient input, Item item, int count) {
        return hammering(input, new RecipeOutput.DataGenHolder(item, count, 1));
    }
}
