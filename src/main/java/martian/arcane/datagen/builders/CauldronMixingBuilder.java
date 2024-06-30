package martian.arcane.datagen.builders;

import martian.arcane.api.recipe.BlockIngredient;
import martian.arcane.api.recipe.BlockOutput;
import martian.arcane.api.recipe.RecipeOutput;
import martian.arcane.common.recipe.RecipeCauldronMixing;
import martian.arcane.datagen.util.RecipeDataHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CauldronMixingBuilder implements RecipeBuilder {
    public Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    public RecipeCategory category;
    public @Nullable String group;
    public NonNullList<Ingredient> inputs = NonNullList.create();
    public BlockIngredient cauldron;
    public NonNullList<RecipeOutput> results = NonNullList.create();
    public int fluidAmount;
    public BlockOutput resultCauldron = null;

    private CauldronMixingBuilder() {}

    public CauldronMixingBuilder setCategory(RecipeCategory category) {
        this.category = category;
        return this;
    }

    public CauldronMixingBuilder addInput(Ingredient input) {
        this.inputs.add(input);
        return this;
    }

    public CauldronMixingBuilder addInput(ItemLike input) {
        this.inputs.add(Ingredient.of(input));
        return this;
    }

    public CauldronMixingBuilder setCauldron(Block block) {
        this.cauldron = new BlockIngredient(block);
        return this;
    }

    public CauldronMixingBuilder setResultCauldron(Block block) {
        this.resultCauldron = new BlockOutput(block);
        return this;
    }

    public CauldronMixingBuilder setFluidAmount(int fluidAmount) {
        this.fluidAmount = fluidAmount;
        return this;
    }

    public CauldronMixingBuilder addResult(RecipeOutput result) {
        this.results.add(result);
        return this;
    }

    public CauldronMixingBuilder addResult(ItemStack stack) {
        return addResult(new RecipeOutput(stack, 1));
    }

    public CauldronMixingBuilder addResult(ItemStack stack, float chance) {
        return addResult(new RecipeOutput(stack, chance));
    }

    public CauldronMixingBuilder addResult(ItemLike item, int count, float chance) {
        return addResult(new RecipeOutput(item, count, chance));
    }

    public CauldronMixingBuilder addResult(ItemLike item, int count) {
        return addResult(new RecipeOutput(item, count, 1));
    }

    public CauldronMixingBuilder addResult(ItemLike item) {
        return addResult(new RecipeOutput(item, 1, 1));
    }

    public CauldronMixingBuilder unlockedBy(String criterionName, Criterion criterion) {
        this.advancement.addCriterion(criterionName, criterion);
        return this;
    }

    public CauldronMixingBuilder unlockedWith(ItemLike item) {
        this.advancement.addCriterion("has_item", RecipeDataHelper.has(item));
        return this;
    }

    public CauldronMixingBuilder unlockedWith(TagKey<Item> item) {
        this.advancement.addCriterion("has_item", RecipeDataHelper.has(item));
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    public Item getResult() {
        return results.getFirst().getStack().getItem();
    }

    public void save(net.minecraft.data.recipes.RecipeOutput recipeOutput, ResourceLocation recipeId) {
        this.advancement
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                .rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(recipeId))
                .requirements(AdvancementRequirements.Strategy.OR);

        recipeOutput.accept(
                recipeId,
                new RecipeCauldronMixing(inputs, results, cauldron, Optional.ofNullable(resultCauldron), fluidAmount),
                this.advancement.build(recipeId.withPrefix("recipes/"))
        );
    }

    public void save(RecipeDataHelper helper, ResourceLocation id) {
        helper.wrap(this).save(id);
    }

    public void save(RecipeDataHelper helper) {
        helper.wrap(this).save();
    }

    public static CauldronMixingBuilder cauldronMixing() {
        return new CauldronMixingBuilder();
    }
}
