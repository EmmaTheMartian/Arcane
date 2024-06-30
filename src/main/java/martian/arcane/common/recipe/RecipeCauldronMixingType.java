package martian.arcane.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import martian.arcane.api.recipe.BlockIngredient;
import martian.arcane.api.recipe.ArcaneRecipeType;
import martian.arcane.api.recipe.BlockOutput;
import martian.arcane.api.recipe.RecipeOutput;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class RecipeCauldronMixingType implements ArcaneRecipeType<RecipeCauldronMixing> {
    public static final MapCodec<RecipeCauldronMixing> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            NonNullList.codecOf(Ingredient.CODEC_NONEMPTY).fieldOf("inputs").forGetter(RecipeCauldronMixing::inputs),
            NonNullList.codecOf(RecipeOutput.CODEC).fieldOf("results").forGetter(RecipeCauldronMixing::results),
            BlockIngredient.CODEC.fieldOf("cauldron").forGetter(RecipeCauldronMixing::cauldron),
            BlockOutput.CODEC.optionalFieldOf("resultBlock").forGetter(RecipeCauldronMixing::resultBlock),
            Codec.INT.fieldOf("fluidAmount").forGetter(RecipeCauldronMixing::fluidAmount)
    ).apply(instance, RecipeCauldronMixing::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeCauldronMixing> STREAM_CODEC = StreamCodec.of(
            (buf, it) -> {
                buf.writeInt(it.inputs().size());
                it.inputs().forEach(res -> Ingredient.CONTENTS_STREAM_CODEC.encode(buf, res));
                buf.writeInt(it.results().size());
                it.results().forEach(res -> RecipeOutput.STREAM_CODEC.encode(buf, res));
                BlockIngredient.STREAM_CODEC.encode(buf, it.cauldron());
                buf.writeOptional(it.resultBlock(), BlockOutput.STREAM_CODEC);
                buf.writeInt(it.fluidAmount());
            },
            buf -> {
                int inputsLength = buf.readInt();
                NonNullList<Ingredient> inputs = NonNullList.create();
                for (int i = 0; i < inputsLength; i++)
                    inputs.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buf));

                int resultsLength = buf.readInt();
                NonNullList<RecipeOutput> results = NonNullList.create();
                for (int i = 0; i < resultsLength; i++)
                    results.add(RecipeOutput.STREAM_CODEC.decode(buf));

                return new RecipeCauldronMixing(
                        inputs,
                        results,
                        BlockIngredient.STREAM_CODEC.decode(buf),
                        buf.readOptional(BlockOutput.STREAM_CODEC),
                        buf.readInt()
                );
            }
    );

    @Override
    @NotNull
    public MapCodec<RecipeCauldronMixing> codec() {
        return CODEC;
    }

    @Override
    @NotNull
    public StreamCodec<RegistryFriendlyByteBuf, RecipeCauldronMixing> streamCodec() {
        return STREAM_CODEC;
    }
}
