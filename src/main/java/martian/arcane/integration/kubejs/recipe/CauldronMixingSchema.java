package martian.arcane.integration.kubejs.recipe;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BlockComponent;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import martian.arcane.api.recipe.RecipeOutput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public interface CauldronMixingSchema {
    RecipeKey<List<RecipeOutput>> RESULTS = RecipeOutputComponent.RECIPE_OUTPUT.asList().key("results", ComponentRole.OUTPUT);
    RecipeKey<Ingredient> INPUT = IngredientComponent.NON_EMPTY_INGREDIENT.key("input", ComponentRole.INPUT);
    RecipeKey<Block> INPUT_CAULDRON = BlockComponent.BLOCK.key("cauldron", ComponentRole.INPUT);
    RecipeKey<Block> RESULT_CAULDRON = BlockComponent.BLOCK.key("resultBlock", ComponentRole.OUTPUT).optional(Blocks.CAULDRON);
    RecipeKey<Integer> FLUID_AMOUNT = NumberComponent.INT.key("fluidAmount", ComponentRole.INPUT).optional(0);

    RecipeSchema SCHEMA = new RecipeSchema(RESULTS, INPUT, INPUT_CAULDRON, RESULT_CAULDRON, FLUID_AMOUNT);
}
