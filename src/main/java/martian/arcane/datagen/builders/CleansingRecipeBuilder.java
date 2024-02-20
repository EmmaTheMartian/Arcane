package martian.arcane.datagen.builders;

import martian.arcane.common.registry.ArcaneRecipeTypes;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CleansingRecipeBuilder extends AbstractSpellRecipeBuilder {
    public CleansingRecipeBuilder(RecipeCategory category, RecipeSerializer<?> type, Ingredient input, Item result, int count) {
        super(category, type, input, result, count);
    }

    public static CleansingRecipeBuilder cleansing(Ingredient input, Item result, int count) {
        return new CleansingRecipeBuilder(RecipeCategory.MISC, ArcaneRecipeTypes.CLEANSING_SERIALIZER.get(), input, result, count);
    }
}
