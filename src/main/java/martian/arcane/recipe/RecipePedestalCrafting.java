package martian.arcane.recipe;

import com.google.gson.JsonObject;
import martian.arcane.ArcaneMod;
import martian.arcane.api.recipe.SimpleContainer;
import martian.arcane.block.entity.BlockEntityPedestal;
import martian.arcane.registry.ArcaneRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

// Input and interactionItem stacks must have a count of 1!
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RecipePedestalCrafting implements Recipe<SimpleContainer> {
    public static final String NAME = "pedestal";
    public static final ResourceLocation ID = ArcaneMod.id(NAME);

    public final ResourceLocation id;
    public final ItemStack input;
    public final ItemStack interactionItem;
    public final boolean consumeInteractionItem;
    public final ItemStack result;

    public RecipePedestalCrafting(ResourceLocation id, ItemStack input, ItemStack interactionItem, boolean consumeInteractionItem, ItemStack result) {
        this.id = id;
        this.input = input;
        this.interactionItem = interactionItem;
        this.consumeInteractionItem = consumeInteractionItem;
        this.result = result;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        return this.input.is(container.getItem().getItem());
    }

    public boolean matches(ItemStack pedestalStack, ItemStack interactionStack) {
        return this.input.is(pedestalStack.getItem()) && this.interactionItem.is(interactionStack.getItem());
    }

    public void assemble(BlockEntityPedestal pedestal, ItemStack interactionItem) {
        if (matches(pedestal.getItem(), interactionItem)) {
            pedestal.setItem(getResultItem());
            if (consumeInteractionItem) {
                interactionItem.shrink(1);
            }
        }
    }

    public ItemStack getResultItem() {
        return result.copy();
    }

    @Override
    @Deprecated
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
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
            ArcaneMod.LOGGER.info("pedestal " + id);
            ItemStack input = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "input"));
            ItemStack interactionItem = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "interaction_input"));
            boolean consumed = GsonHelper.getAsBoolean(json, "consume");
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            return new RecipePedestalCrafting(id, input, interactionItem, consumed, result);
        }

        @Override
        public @Nullable RecipePedestalCrafting fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            return new RecipePedestalCrafting(id, buf.readItem(), buf.readItem(), buf.readBoolean(), buf.readItem());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RecipePedestalCrafting recipe) {
            buf.writeItemStack(recipe.input, true);
            buf.writeItemStack(recipe.interactionItem, true);
            buf.writeBoolean(recipe.consumeInteractionItem);
            buf.writeItemStack(recipe.result, false);
        }
    }
}
