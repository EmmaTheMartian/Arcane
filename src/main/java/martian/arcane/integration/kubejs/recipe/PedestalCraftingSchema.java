package martian.arcane.integration.kubejs.recipe;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import martian.arcane.api.recipe.RecipeOutput;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public interface PedestalCraftingSchema {
    RecipeKey<List<RecipeOutput>> RESULTS = RecipeOutputComponent.RECIPE_OUTPUT.asList().key("results", ComponentRole.OUTPUT);
    RecipeKey<Ingredient> INPUT = IngredientComponent.NON_EMPTY_INGREDIENT.key("input", ComponentRole.INPUT);
    RecipeKey<Ingredient> INTERACTION_INPUT = IngredientComponent.INGREDIENT.key("interaction_input", ComponentRole.INPUT);
    RecipeKey<Boolean> CONSUME = BooleanComponent.BOOLEAN.key("consume", ComponentRole.OTHER);

    RecipeSchema SCHEMA = new RecipeSchema(RESULTS, INPUT, INTERACTION_INPUT, CONSUME);
}
