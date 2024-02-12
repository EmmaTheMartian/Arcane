package martian.arcane.datagen.builders;

import martian.arcane.registry.ArcaneRecipeTypes;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class PurifyingRecipeBuilder extends AbstractSpellRecipeBuilder {
    public PurifyingRecipeBuilder(RecipeCategory category, RecipeSerializer<?> type, Ingredient input, Item result, int count) {
        super(category, type, input, result, count);
    }

    public static PurifyingRecipeBuilder purifying(Ingredient input, Item result, int count) {
        return new PurifyingRecipeBuilder(RecipeCategory.MISC, ArcaneRecipeTypes.PURIFYING_SERIALIZER.get(), input, result, count);
    }
}
