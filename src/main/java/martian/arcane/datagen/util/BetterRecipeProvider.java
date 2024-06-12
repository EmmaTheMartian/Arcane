package martian.arcane.datagen.util;

import martian.arcane.ArcaneMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class BetterRecipeProvider extends RecipeProvider {
    protected RecipeDataHelper helper;

    public BetterRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        helper = new RecipeDataHelper(output, ArcaneMod.MODID, RecipeCategory.MISC);
        build();
    }

    protected abstract void build();

    // Utility methods
    protected <B extends RecipeBuilder> RecipeDataHelper.BuilderWrapper<B> wrap(B provider) {
        return helper.wrap(provider);
    }

    // Shaped
    protected RecipeDataHelper.BuilderWrapper<ShapedRecipeBuilder> shaped(ItemLike output, int count) {
        return helper.shaped(output, count);
    }

    protected RecipeDataHelper.BuilderWrapper<ShapedRecipeBuilder> shaped(ItemLike output) {
        return helper.shaped(output);
    }

    // Shapeless
    protected RecipeDataHelper.BuilderWrapper<ShapelessRecipeBuilder> shapeless(ItemLike output, int count) {
        return helper.shapeless(output, count);
    }

    protected RecipeDataHelper.BuilderWrapper<ShapelessRecipeBuilder> shapeless(ItemLike output) {
        return helper.shapeless(output);
    }

    protected RecipeDataHelper.BuilderWrapper<ShapelessRecipeBuilder> shapeless(ItemLike output, int count, Map<Ingredient, Integer> items) {
        return helper.shapeless(output, count, items);
    }

    protected RecipeDataHelper.BuilderWrapper<ShapelessRecipeBuilder> shapeless(ItemLike output, Map<Ingredient, Integer> items) {
        return helper.shapeless(output, items);
    }

    protected void swappable(Item a, Item b) {
        helper.swappable(a, b);
    }
}
