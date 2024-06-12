package martian.arcane.datagen.builders;

import martian.arcane.api.recipe.RecipeOutput;
import martian.arcane.common.recipe.RecipePedestalCrafting;
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
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PedestalRecipeBuilder implements RecipeBuilder {
    public Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    public RecipeCategory category;
    public @Nullable String group;
    public Ingredient input;
    public Ingredient interactionInput;
    public boolean consume;
    public NonNullList<RecipeOutput> results = NonNullList.create();

    private PedestalRecipeBuilder() {}

    public PedestalRecipeBuilder setCategory(RecipeCategory category) {
        this.category = category;
        return this;
    }

    public PedestalRecipeBuilder setInput(Ingredient input) {
        this.input = input;
        return this;
    }

    public PedestalRecipeBuilder setInteractionInput(Ingredient input, boolean consumed) {
        this.interactionInput = input;
        this.consume = consumed;
        return this;
    }

    public PedestalRecipeBuilder addResult(RecipeOutput result) {
        this.results.add(result);
        return this;
    }

    public PedestalRecipeBuilder addResult(ItemStack stack) {
        return addResult(new RecipeOutput(stack, 1));
    }

    public PedestalRecipeBuilder addResult(ItemStack stack, float chance) {
        return addResult(new RecipeOutput(stack, chance));
    }

    public PedestalRecipeBuilder addResult(Item item, int count, float chance) {
        return addResult(new RecipeOutput(item, count, chance));
    }

    public PedestalRecipeBuilder addResult(Item item, int count) {
        return addResult(new RecipeOutput(item, count, 1));
    }

    public PedestalRecipeBuilder addResult(Item item) {
        return addResult(new RecipeOutput(item, 1, 1));
    }

    public PedestalRecipeBuilder unlockedBy(String criterionName, Criterion criterion) {
        this.advancement.addCriterion(criterionName, criterion);
        return this;
    }

    public PedestalRecipeBuilder unlockedWith(ItemLike item) {
        this.advancement.addCriterion("has_item", RecipeDataHelper.has(item));
        return this;
    }

    public PedestalRecipeBuilder unlockedWith(TagKey<Item> item) {
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
                new RecipePedestalCrafting(input, interactionInput, consume, results),
                this.advancement.build(recipeId.withPrefix("recipes/"))
        );
    }

    public void save(RecipeDataHelper helper, ResourceLocation id) {
        helper.wrap(this).save(id);
    }

    public void save(RecipeDataHelper helper) {
        helper.wrap(this).save();
    }

    public static PedestalRecipeBuilder pedestalCrafting() {
        return new PedestalRecipeBuilder();
    }

    public static PedestalRecipeBuilder simpleRecipe(Ingredient input, Ingredient interactionInput, RecipeOutput output) {
        return pedestalCrafting().setInput(input).setInteractionInput(interactionInput, true).addResult(output);
    }

    public static PedestalRecipeBuilder simpleRecipe(Ingredient input, Ingredient interactionInput, ItemLike output, int count) {
        return simpleRecipe(input, interactionInput, new RecipeOutput(new ItemStack(output, count), 1));
    }
}
