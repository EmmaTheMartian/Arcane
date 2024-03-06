package martian.arcane.datagen.builders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import martian.arcane.api.ListHelpers;
import martian.arcane.api.recipe.RecipeOutput;
import martian.arcane.common.registry.ArcaneRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PedestalRecipeBuilder implements RecipeBuilder {
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    private @javax.annotation.Nullable String group;
    private final RecipeCategory category;
    private final RecipeSerializer<?> type;
    private final Ingredient input;
    private final Ingredient interactionInput;
    private final boolean consume;
    private final NonNullList<RecipeOutput.DataGenHolder> results;

    public PedestalRecipeBuilder(RecipeCategory category, RecipeSerializer<?> type, Ingredient input, Ingredient interactionItem, boolean consume, NonNullList<RecipeOutput.DataGenHolder> results) {
        this.category = category;
        this.type = type;
        this.input = input;
        this.interactionInput = interactionItem;
        this.consume = consume;
        this.results = results;
    }

    public static PedestalRecipeBuilder pedestalCrafting(Ingredient input, Ingredient interactionInput, boolean consume, NonNullList<RecipeOutput.DataGenHolder> results) {
        return new PedestalRecipeBuilder(RecipeCategory.MISC, ArcaneRecipeTypes.PEDESTAL_SERIALIZER.get(), input, interactionInput, consume, results);
    }

    public static PedestalRecipeBuilder simpleRecipe(Ingredient input, Ingredient interactionInput, RecipeOutput.DataGenHolder output) {
        return pedestalCrafting(input, interactionInput, true, ListHelpers.nonNullListOf(output));
    }

    public static PedestalRecipeBuilder simpleRecipe(Ingredient input, Ingredient interactionInput, Item output, int count) {
        return simpleRecipe(input, interactionInput, new RecipeOutput.DataGenHolder(output, count, 1));
    }

    public Item getResult() {
        return results.get(0).item();
    }

    public PedestalRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger) {
        this.advancement.addCriterion(criterionName, criterionTrigger);
        return this;
    }

    public PedestalRecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId) {
        this.ensureValid(recipeId);
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId)).rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
        finishedRecipeConsumer.accept(new PedestalRecipeBuilder.Result(recipeId, this.type, this.group == null ? "" : this.group, this.input, this.interactionInput, this.consume, this.results, this.advancement, recipeId.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation id) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final String group;
        private final Ingredient input;
        private final Ingredient interactionInput;
        private final boolean consume;
        private final NonNullList<RecipeOutput.DataGenHolder> results;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final RecipeSerializer<?> type;

        public Result(ResourceLocation id, RecipeSerializer<?> type, String group, Ingredient input, Ingredient interactionInput, boolean consume, NonNullList<RecipeOutput.DataGenHolder> results, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.type = type;
            this.group = group;
            this.input = input;
            this.interactionInput = interactionInput;
            this.consume = consume;
            this.results = results;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        public void serializeRecipeData(JsonObject json) {
            if (!this.group.isEmpty())
                json.addProperty("group", this.group);

            json.add("input", this.input.toJson());
            json.add("interaction_input", this.interactionInput.toJson());
            json.addProperty("consume", this.consume);

            JsonArray jsonResults = new JsonArray();
            results.forEach(result -> jsonResults.add(result.toRecipeOutput().toJson()));
            json.add("results", jsonResults);
        }

        public ResourceLocation getId() {
            return this.id;
        }

        public RecipeSerializer<?> getType() {
            return this.type;
        }

        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
