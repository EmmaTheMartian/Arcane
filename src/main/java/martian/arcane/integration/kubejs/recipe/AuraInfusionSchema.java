package martian.arcane.integration.kubejs.recipe;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface AuraInfusionSchema {
    RecipeKey<OutputItem[]> RESULTS = ItemComponents.OUTPUT_ARRAY.key("results");
    RecipeKey<InputItem> INPUT = ItemComponents.INPUT.key("input");
    RecipeKey<Integer> AURA = NumberComponent.INT.key("aura");

    RecipeSchema SCHEMA = new RecipeSchema(ArcaneRecipeJS.class, ArcaneRecipeJS::new, RESULTS, INPUT, AURA);
}
