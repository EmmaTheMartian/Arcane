package martian.arcane.integration.kubejs.recipe;

import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.rhino.type.TypeInfo;
import martian.arcane.api.recipe.RecipeOutput;

public record RecipeOutputComponent(String name) implements RecipeComponent<RecipeOutput> {
    public static final RecipeOutputComponent RECIPE_OUTPUT = new RecipeOutputComponent("recipe_output");
    public static final TypeInfo RECIPE_OUTPUT_TYPE_INFO = TypeInfo.of(RecipeOutput.class);

    @Override
    public Codec<RecipeOutput> codec() {
        return RecipeOutput.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return RECIPE_OUTPUT_TYPE_INFO;
    }
}
