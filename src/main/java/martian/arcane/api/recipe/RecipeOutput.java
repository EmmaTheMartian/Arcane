package martian.arcane.api.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public record RecipeOutput(ItemStack stack, float chance) {
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

    public JsonElement toJson() {
        JsonObject o = new JsonObject();
        o.add("item", ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, stack).getOrThrow(false, errorMessage -> {}));
        o.addProperty("chance", chance);
        return o;
    }

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeItem(stack);
        buf.writeFloat(chance);
    }

    public static RecipeOutput fromJson(JsonObject o) {
        return new RecipeOutput(
                ItemStack.CODEC.decode(JsonOps.INSTANCE, o.getAsJsonObject("item"))
                        .getOrThrow(false, errorMessage -> {})
                        .getFirst(),
                GsonHelper.getAsFloat(o, "chance")
        );
    }

    public static RecipeOutput fromNetwork(FriendlyByteBuf buf) {
        return new RecipeOutput(buf.readItem(), buf.readFloat());
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
