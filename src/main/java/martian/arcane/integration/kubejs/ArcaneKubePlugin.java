package martian.arcane.integration.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import martian.arcane.ArcaneMod;
import martian.arcane.common.recipe.*;
import martian.arcane.integration.kubejs.recipe.AuraInfusionSchema;
import martian.arcane.integration.kubejs.recipe.BasicRecipeSchema;
import martian.arcane.integration.kubejs.recipe.PedestalCraftingSchema;

public class ArcaneKubePlugin extends KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.namespace(ArcaneMod.MODID)
                .register(RecipeAuraInfusion.NAME, AuraInfusionSchema.SCHEMA)
                .register(RecipePedestalCrafting.NAME, PedestalCraftingSchema.SCHEMA)
                .register(RecipeHammering.NAME, BasicRecipeSchema.SCHEMA)
                .register(RecipeCleansing.NAME, BasicRecipeSchema.SCHEMA)
                .register(RecipePurifying.NAME, BasicRecipeSchema.SCHEMA)
        ;
    }
}
