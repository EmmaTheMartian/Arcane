package martian.arcane.api.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiFunction;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class SimpleRecipe<T extends SimpleContainer> implements Recipe<T> {
    public final Ingredient input;
    public final NonNullList<RecipeOutput> results;

    public SimpleRecipe(Ingredient input, NonNullList<RecipeOutput> results) {
        this.input = input;
        this.results = results;
    }

    public final Ingredient input() {
        return input;
    }

    public final NonNullList<RecipeOutput> results() {
        return results;
    }

    public NonNullList<ItemStack> getResultItems() {
        NonNullList<ItemStack> list = NonNullList.create();
        list.addAll(results.stream().map(RecipeOutput::roll).toList());
        return list;
    }

    @Override
    @Deprecated
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return results.getFirst().stack();
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
    @Deprecated
    public ItemStack assemble(SimpleContainer container, HolderLookup.Provider registries) throws RuntimeException {
        throw new RuntimeException("Cannot invoke SimpleRecipe#assemble(SimpleContainer, HolderLookup.Provider)");
    }

    public static abstract class SimpleSerializer<R extends SimpleRecipe<?>> implements RecipeSerializer<R> {
        private final BiFunction<Ingredient, NonNullList<RecipeOutput>, R> recipeFunc;

        public SimpleSerializer(BiFunction<Ingredient, NonNullList<RecipeOutput>, R> recipeFunc) {
            this.recipeFunc = recipeFunc;

            CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(R::input),
                    NonNullList.codecOf(RecipeOutput.CODEC).fieldOf("results").forGetter(R::results)
            ).apply(instance, recipeFunc));
        }

        public final MapCodec<R> CODEC;
        public final StreamCodec<RegistryFriendlyByteBuf, R> STREAM_CODEC = StreamCodec.of(this::toNetwork, this::fromNetwork);

        public void toNetwork(RegistryFriendlyByteBuf buf, R recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.input);
            buf.writeInt(recipe.results.size());
            recipe.results.forEach(res -> RecipeOutput.STREAM_CODEC.encode(buf, res));
        }

        public R fromNetwork(RegistryFriendlyByteBuf buf) {
            Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
            int resultsLength = buf.readInt();
            NonNullList<RecipeOutput> results = NonNullList.create();
            for (int i = 0; i < resultsLength; i++)
                results.add(RecipeOutput.STREAM_CODEC.decode(buf));
            return this.recipeFunc.apply(input, results);
        }

        @Override
        public MapCodec<R> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, R> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
