package martian.arcane.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import martian.arcane.api.recipe.ArcaneRecipeType;
import martian.arcane.api.recipe.RecipeOutput;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class SpellRecipeType implements ArcaneRecipeType<SpellRecipe> {
    public static MapCodec<SpellRecipe> makeCodec(SpellRecipeType type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(SpellRecipe::input),
                NonNullList.codecOf(RecipeOutput.CODEC).fieldOf("results").forGetter(SpellRecipe::results)
        ).apply(instance, (input, results) -> new SpellRecipe(type, input, results)));
    }

    public static StreamCodec<RegistryFriendlyByteBuf, SpellRecipe> makeStreamCodec(SpellRecipeType type) {
        return StreamCodec.of(
                (RegistryFriendlyByteBuf buf, SpellRecipe recipe) -> {
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.input);
                    buf.writeInt(recipe.results.size());
                    recipe.results.forEach(res -> RecipeOutput.STREAM_CODEC.encode(buf, res));
                },
                (RegistryFriendlyByteBuf buf) -> {
                    Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                    int resultsLength = buf.readInt();
                    NonNullList<RecipeOutput> results = NonNullList.create();
                    for (int i = 0; i < resultsLength; i++)
                        results.add(RecipeOutput.STREAM_CODEC.decode(buf));
                    return new SpellRecipe(type, input, results);
                }
        );
    }

    private final MapCodec<SpellRecipe> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, SpellRecipe> streamCodec;

    public SpellRecipeType() {
        codec = makeCodec(this);
        streamCodec = makeStreamCodec(this);
    }

    @Override
    @NotNull
    public MapCodec<SpellRecipe> codec() {
        return codec;
    }

    @Override
    @NotNull
    public StreamCodec<RegistryFriendlyByteBuf, SpellRecipe> streamCodec() {
        return streamCodec;
    }
}
