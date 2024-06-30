package martian.arcane.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import martian.arcane.api.recipe.ArcaneRecipeType;
import martian.arcane.api.recipe.RecipeOutput;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class RecipeAuraInfusionType implements ArcaneRecipeType<RecipeAuraInfusion> {
    public static final MapCodec<RecipeAuraInfusion> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(RecipeAuraInfusion::input),
            NonNullList.codecOf(RecipeOutput.CODEC).fieldOf("results").forGetter(RecipeAuraInfusion::results),
            Codec.INT.fieldOf("aura").forGetter(RecipeAuraInfusion::aura)
    ).apply(instance, RecipeAuraInfusion::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeAuraInfusion> STREAM_CODEC = StreamCodec.of(RecipeAuraInfusionType::toNetwork, RecipeAuraInfusionType::fromNetwork);

    public static RecipeAuraInfusion fromNetwork(RegistryFriendlyByteBuf buf) {
        Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
        int resultsLength = buf.readInt();
        NonNullList<RecipeOutput> results = NonNullList.create();
        for (int i = 0; i < resultsLength; i++)
            results.add(RecipeOutput.STREAM_CODEC.decode(buf));
        int aura = buf.readInt();
        return new RecipeAuraInfusion(input, results, aura);
    }

    public static void toNetwork(RegistryFriendlyByteBuf buf, RecipeAuraInfusion recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.input);
        buf.writeInt(recipe.results.size());
        recipe.results.forEach(res -> RecipeOutput.STREAM_CODEC.encode(buf, res));
        buf.writeInt(recipe.aura);
    }

    @Override
    @NotNull
    public MapCodec<RecipeAuraInfusion> codec() {
        return CODEC;
    }

    @Override
    @NotNull
    public StreamCodec<RegistryFriendlyByteBuf, RecipeAuraInfusion> streamCodec() {
        return STREAM_CODEC;
    }
}
