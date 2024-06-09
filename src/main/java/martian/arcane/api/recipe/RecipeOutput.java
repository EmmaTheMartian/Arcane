package martian.arcane.api.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public record RecipeOutput(ItemStack stack, float chance) {
    public RecipeOutput(Item item, int count, float chance) {
        this(new ItemStack(item, count), chance);
    }

    public static final Codec<RecipeOutput> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.OPTIONAL_CODEC.fieldOf("stack").forGetter(RecipeOutput::stack),
            Codec.FLOAT.fieldOf("chance").forGetter(RecipeOutput::chance)
    ).apply(instance, RecipeOutput::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeOutput> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC, RecipeOutput::stack,
            ByteBufCodecs.FLOAT, RecipeOutput::chance,
            RecipeOutput::new);

    private static final Random random = new Random();

    public ItemStack roll() {
        int count = 0;
        for (int i = 0; i < stack.getCount(); i++) {
            if (random.nextFloat() < chance)
                count++;
        }

        if (count == 0)
            return ItemStack.EMPTY;

        return stack.copyWithCount(count);
    }

    public ItemStack getStack() {
        return stack;
    }

    public float getChance() {
        return chance;
    }

    public static final class DataGenHolder {
        private final ItemStack stack;
        private final float chance;

        public DataGenHolder(Item item, int count, float chance) {
            this.stack = new ItemStack(item, count);
            this.chance = chance;
        }

        public DataGenHolder(ItemStack stack, float chance) {
            this.stack = stack;
            this.chance = chance;
        }

        public RecipeOutput toRecipeOutput() {
            return new RecipeOutput(stack, chance);
        }

        public Item item() {
            return stack.getItem();
        }

        public int count() {
            return stack.getCount();
        }

        public float chance() {
            return chance;
        }
    }
}
