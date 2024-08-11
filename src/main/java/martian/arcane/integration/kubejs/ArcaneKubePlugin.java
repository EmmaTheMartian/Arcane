package martian.arcane.integration.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeComponentFactoryRegistry;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import martian.arcane.ArcaneMod;
import martian.arcane.integration.kubejs.recipe.*;

public class ArcaneKubePlugin implements KubeJSPlugin {
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.register(ArcaneMod.id("aura_infusion"), AuraInfusionSchema.SCHEMA);
        registry.register(ArcaneMod.id("pedestal_crafting"), PedestalCraftingSchema.SCHEMA);
        registry.register(ArcaneMod.id("cauldron_mixing"), CauldronMixingSchema.SCHEMA);
        registry.register(ArcaneMod.id("hammering"), BasicRecipeSchema.SCHEMA);
        registry.register(ArcaneMod.id("cleansing"), BasicRecipeSchema.SCHEMA);
        registry.register(ArcaneMod.id("purifying"), BasicRecipeSchema.SCHEMA);
        registry.register(ArcaneMod.id("freezing"), BasicRecipeSchema.SCHEMA);
    }

    public void registerRecipeComponents(RecipeComponentFactoryRegistry registry) {
        registry.register("recipe_output", RecipeOutputComponent.RECIPE_OUTPUT);
    }
}
