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

public class RecipePedestalType implements ArcaneRecipeType<RecipePedestalCrafting> {
    public static final MapCodec<RecipePedestalCrafting> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(RecipePedestalCrafting::input),
            Ingredient.CODEC_NONEMPTY.fieldOf("interactionItem").forGetter(RecipePedestalCrafting::interactionItem),
            Codec.BOOL.fieldOf("consumeInteractionItem").forGetter(RecipePedestalCrafting::consumeInteractionItem),
            NonNullList.codecOf(RecipeOutput.CODEC).fieldOf("results").forGetter(RecipePedestalCrafting::results)
    ).apply(instance, RecipePedestalCrafting::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RecipePedestalCrafting> STREAM_CODEC = StreamCodec.of(RecipePedestalType::toNetwork, RecipePedestalType::fromNetwork);

    public static RecipePedestalCrafting fromNetwork(RegistryFriendlyByteBuf buf) {
        Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
        Ingredient interactionItem = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
        boolean consumeInteractionItem = buf.readBoolean();
        int resultsLength = buf.readInt();
        NonNullList<RecipeOutput> results = NonNullList.create();
        for (int i = 0; i < resultsLength; i++)
            results.add(RecipeOutput.STREAM_CODEC.decode(buf));
        return new RecipePedestalCrafting(input, interactionItem, consumeInteractionItem, results);
    }

    public static void toNetwork(RegistryFriendlyByteBuf buf, RecipePedestalCrafting recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.input);
        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.interactionItem);
        buf.writeBoolean(recipe.consumeInteractionItem);
        buf.writeInt(recipe.results.size());
        recipe.results.forEach(res -> RecipeOutput.STREAM_CODEC.encode(buf, res));
    }

    @Override
    @NotNull
    public MapCodec<RecipePedestalCrafting> codec() {
        return CODEC;
    }

    @Override
    @NotNull
    public StreamCodec<RegistryFriendlyByteBuf, RecipePedestalCrafting> streamCodec() {
        return STREAM_CODEC;
    }
}
