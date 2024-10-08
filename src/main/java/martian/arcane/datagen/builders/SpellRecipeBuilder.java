package martian.arcane.datagen.builders;

import martian.arcane.api.recipe.RecipeOutput;
import martian.arcane.common.ArcaneContent;
import martian.arcane.common.recipe.SpellRecipe;
import martian.arcane.common.recipe.SpellRecipeType;
import martian.arcane.datagen.util.RecipeDataHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.*;
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

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpellRecipeBuilder implements RecipeBuilder {
    public final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    public @Nullable String group;
    public RecipeCategory category;
    public SpellRecipeType type;
    public Ingredient input;
    public NonNullList<RecipeOutput> results = NonNullList.create();

    public SpellRecipeBuilder() {}

    public Item getResult() {
        return results.getFirst().getStack().getItem();
    }

    public SpellRecipeBuilder setCategory(RecipeCategory category) {
        this.category = category;
        return this;
    }

    public SpellRecipeBuilder setType(SpellRecipeType type) {
        this.type = type;
        return this;
    }

    public SpellRecipeBuilder setInput(Ingredient input) {
        this.input = input;
        return this;
    }

    public SpellRecipeBuilder setInput(ItemLike input) {
        return setInput(Ingredient.of(input));
    }

    public SpellRecipeBuilder setInput(TagKey<Item> input) {
        return setInput(Ingredient.of(input));
    }

    public SpellRecipeBuilder addResult(RecipeOutput result) {
        this.results.add(result);
        return this;
    }

    public SpellRecipeBuilder addResult(ItemStack stack) {
        return addResult(new RecipeOutput(stack, 1));
    }

    public SpellRecipeBuilder addResult(ItemStack stack, float chance) {
        return addResult(new RecipeOutput(stack, chance));
    }

    public SpellRecipeBuilder addResult(ItemLike item, int count, float chance) {
        return addResult(new RecipeOutput(item, count, chance));
    }

    public SpellRecipeBuilder addResult(ItemLike item, int count) {
        return addResult(new RecipeOutput(item, count, 1));
    }

    public SpellRecipeBuilder addResult(ItemLike item) {
        return addResult(new RecipeOutput(item, 1, 1));
    }

    public SpellRecipeBuilder unlockedBy(String criterionName, Criterion criterion) {
        this.advancement.addCriterion(criterionName, criterion);
        return this;
    }

    public SpellRecipeBuilder unlockedWith(ItemLike item) {
        this.advancement.addCriterion("has_item", RecipeDataHelper.has(item));
        return this;
    }

    public SpellRecipeBuilder unlockedWith(TagKey<Item> item) {
        this.advancement.addCriterion("has_item", RecipeDataHelper.has(item));
        return this;
    }

    public SpellRecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    public SpellRecipe build() {
        assert !results.isEmpty();
        assert !input.hasNoItems();
        return new SpellRecipe(type, input, results);
    }

    public void save(net.minecraft.data.recipes.RecipeOutput output, ResourceLocation id) {
        this.advancement
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);

        output.accept(id, build(), this.advancement.build(id.withPrefix("recipes/")));
    }

    public void save(RecipeDataHelper helper, ResourceLocation id) {
        helper.wrap(this).save(id);
    }

    public void save(RecipeDataHelper helper) {
        helper.wrap(this).save();
    }

    public static SpellRecipeBuilder hammering() {
        return new SpellRecipeBuilder().setType(ArcaneContent.RT_HAMMERING);
    }

    public static SpellRecipeBuilder cleansing() {
        return new SpellRecipeBuilder().setType(ArcaneContent.RT_CLEANSING);
    }

    public static SpellRecipeBuilder purifying() {
        return new SpellRecipeBuilder().setType(ArcaneContent.RT_PURIFYING);
    }

    public static SpellRecipeBuilder freezing() {
        return new SpellRecipeBuilder().setType(ArcaneContent.RT_FREEZING);
    }
}
