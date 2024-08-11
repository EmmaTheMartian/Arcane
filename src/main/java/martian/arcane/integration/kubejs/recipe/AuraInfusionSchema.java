package martian.arcane.integration.kubejs.recipe;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import martian.arcane.api.recipe.RecipeOutput;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public interface AuraInfusionSchema {
    RecipeKey<List<RecipeOutput>> RESULTS = RecipeOutputComponent.RECIPE_OUTPUT.asList().key("results", ComponentRole.OUTPUT);
    RecipeKey<Ingredient> INPUT = IngredientComponent.NON_EMPTY_INGREDIENT.key("input", ComponentRole.INPUT);
    RecipeKey<Integer> AURA = NumberComponent.INT.key("aura", ComponentRole.INPUT);

    RecipeSchema SCHEMA = new RecipeSchema(RESULTS, INPUT, AURA);
}
