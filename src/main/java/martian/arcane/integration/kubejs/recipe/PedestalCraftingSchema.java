package martian.arcane.integration.kubejs.recipe;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface PedestalCraftingSchema {
    RecipeKey<OutputItem[]> RESULTS = ItemComponents.OUTPUT_ARRAY.key("results");
    RecipeKey<InputItem> INPUT = ItemComponents.INPUT.key("input");
    RecipeKey<InputItem> INTERACTION_INPUT = ItemComponents.INPUT.key("interaction_input");
    RecipeKey<Boolean> CONSUME = BooleanComponent.BOOLEAN.key("consume");

    RecipeSchema SCHEMA = new RecipeSchema(ArcaneRecipeJS.class, ArcaneRecipeJS::new, RESULTS, INPUT, INTERACTION_INPUT, CONSUME);
}
