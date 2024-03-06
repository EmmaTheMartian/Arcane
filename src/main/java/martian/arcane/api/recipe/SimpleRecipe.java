package martian.arcane.api.recipe;

import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.function.TriFunction;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class SimpleRecipe<T extends SimpleContainer> implements Recipe<T> {
    private final RecipeType<?> type;
    public final ResourceLocation id;
    public final Ingredient input;
    public final NonNullList<RecipeOutput> results;

    public SimpleRecipe(RecipeType<?> type, ResourceLocation id, Ingredient input, NonNullList<RecipeOutput> results) {
        this.type = type;
        this.id = id;
        this.input = input;
        this.results = results;
    }

    public NonNullList<ItemStack> getResultItems() {
        NonNullList<ItemStack> list = NonNullList.create();
        list.addAll(results.stream().map(RecipeOutput::roll).toList());
        return list;
    }

    @Override
    @Deprecated
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return results.get(0).stack();
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        return this.input.test(container.getItem());
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeType<?> getType() {
        return type;
    }

    @Override
    @Deprecated
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) throws RuntimeException {
        throw new RuntimeException("Cannot invoke SimpleRecipe#assemble(SimpleContainer, RegistryAccess)");
    }

    public static abstract class SimpleSerializer<R extends SimpleRecipe<?>> implements RecipeSerializer<R> {
        private final TriFunction<ResourceLocation, Ingredient, NonNullList<RecipeOutput>, R> recipeFunc;

        public SimpleSerializer(TriFunction<ResourceLocation, Ingredient, NonNullList<RecipeOutput>, R> recipeFunc) {
            this.recipeFunc = recipeFunc;
        }

        @Override
        @NotNull
        public R fromJson(ResourceLocation id, JsonObject json) {
            SerializerDataHolder holder = SerializerDataHolder.fromJson(json);
            return recipeFunc.apply(id, holder.input, holder.results);
        }

        @Override
        public @Nullable R fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            SerializerDataHolder holder = SerializerDataHolder.fromNetwork(buf);
            return recipeFunc.apply(id, holder.input, holder.results);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, R recipe) {
            new SerializerDataHolder(recipe.input, recipe.results).toNetwork(buf);
        }
    }

    public record SerializerDataHolder(Ingredient input, NonNullList<RecipeOutput> results) {
        public void toNetwork(FriendlyByteBuf buf) {
            input.toNetwork(buf);
            buf.writeInt(results.size());
            results.forEach(res -> res.toNetwork(buf));
        }

        public static SerializerDataHolder fromJson(JsonObject json) {
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
            NonNullList<RecipeOutput> results = NonNullList.create();
            GsonHelper.getAsJsonArray(json, "results").forEach(result ->
                    results.add(RecipeOutput.fromJson(result.getAsJsonObject())));
            return new SerializerDataHolder(input, results);
        }

        public static SerializerDataHolder fromNetwork(FriendlyByteBuf buf) {
            Ingredient input = Ingredient.fromNetwork(buf);
            int resultsLength = buf.readInt();
            NonNullList<RecipeOutput> results = NonNullList.create();
            for (int i = 0; i < resultsLength; i++)
                results.add(RecipeOutput.fromNetwork(buf));
            return new SerializerDataHolder(input, results);
        }
    }
}
