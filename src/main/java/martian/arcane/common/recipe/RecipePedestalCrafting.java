package martian.arcane.common.recipe;

import com.google.gson.JsonObject;
import martian.arcane.ArcaneMod;
import martian.arcane.api.item.ItemHelpers;
import martian.arcane.api.recipe.RecipeOutput;
import martian.arcane.api.recipe.SimpleContainer;
import martian.arcane.api.recipe.SimpleRecipe;
import martian.arcane.common.block.entity.BlockEntityPedestal;
import martian.arcane.common.registry.ArcaneRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RecipePedestalCrafting extends SimpleRecipe<SimpleContainer> {
    public static final String NAME = "pedestal";
    public static final ResourceLocation ID = ArcaneMod.id(NAME);

    public final Ingredient interactionItem;
    public final boolean consumeInteractionItem;

    public RecipePedestalCrafting(ResourceLocation id, Ingredient input, Ingredient interactionItem, boolean consumeInteractionItem, NonNullList<RecipeOutput> results) {
        super(ArcaneRecipeTypes.PEDESTAL.get(), id, input, results);
        this.interactionItem = interactionItem;
        this.consumeInteractionItem = consumeInteractionItem;
    }

    public boolean matches(ItemStack pedestalStack, ItemStack interactionStack) {
        return this.input.test(pedestalStack) && this.interactionItem.test(interactionStack);
    }

    public void assemble(BlockEntityPedestal pedestal, ItemStack interactionItem) {
        if (matches(pedestal.getItem(), interactionItem)) {
            NonNullList<ItemStack> stacks = NonNullList.create();
            results.stream()
                    .map(RecipeOutput::roll)
                    .filter(stack -> !stack.isEmpty())
                    .forEach(stacks::add);

            if (stacks.size() == 1)
                pedestal.setItem(stacks.get(0));
            else {
                pedestal.setItem(ItemStack.EMPTY);
                Level level = pedestal.getLevel();
                BlockPos pos = pedestal.getBlockPos().above();
                stacks.forEach(result -> ItemHelpers.addItemEntity(level, result, pos));
            }

            if (consumeInteractionItem)
                interactionItem.shrink(1);
        }
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return new Serializer();
    }

    @Override
    public RecipeType<?> getType() {
        return ArcaneRecipeTypes.PEDESTAL.get();
    }

    public static Optional<RecipePedestalCrafting> getRecipeFor(Level level, ItemStack pedestalStack, ItemStack interactionStack) {
        return getAllRecipes(level)
                .stream()
                .filter(recipe -> recipe.matches(pedestalStack, interactionStack))
                .findFirst();
    }

    public static List<RecipePedestalCrafting> getAllRecipes(Level level) {
        return level.getRecipeManager().getAllRecipesFor(ArcaneRecipeTypes.PEDESTAL.get());
    }

    public static class Serializer implements RecipeSerializer<RecipePedestalCrafting> {
        @Override
        public RecipePedestalCrafting fromJson(ResourceLocation id, JsonObject json) {
            SerializerDataHolder holder = SerializerDataHolder.fromJson(json);
            return new RecipePedestalCrafting(
                    id,
                    holder.input(),
                    Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "interaction_input")),
                    GsonHelper.getAsBoolean(json, "consume"),
                    holder.results()
            );
        }

        @Override
        public @Nullable RecipePedestalCrafting fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            SerializerDataHolder holder = SerializerDataHolder.fromNetwork(buf);
            return new RecipePedestalCrafting(id, holder.input(), Ingredient.fromNetwork(buf), buf.readBoolean(), holder.results());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RecipePedestalCrafting recipe) {
            new SerializerDataHolder(recipe.input, recipe.results).toNetwork(buf);
            recipe.interactionItem.toNetwork(buf);
            buf.writeBoolean(recipe.consumeInteractionItem);
        }
    }
}
