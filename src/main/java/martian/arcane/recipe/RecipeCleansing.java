package martian.arcane.recipe;

import com.google.gson.JsonObject;
import martian.arcane.ArcaneMod;
import martian.arcane.api.recipe.SimpleContainer;
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

// Input ItemStack must have a count of 1!
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RecipeCleansing implements Recipe<SimpleContainer> {
    public static final String NAME = "cleansing";
    public static final ResourceLocation ID = ArcaneMod.id(NAME);

    public final ResourceLocation id;
    public final ItemStack input;
    public final ItemStack result;

    public RecipeCleansing(ResourceLocation id, ItemStack input, ItemStack result) {
        this.id = id;
        this.input = input;
        this.result = result;
    }

    public ItemStack getResultItem() {
        return result.copy();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return getResultItem();
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        return this.input.is(container.getItem().getItem());
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
    public RecipeSerializer<?> getSerializer() {
        return new Serializer();
    }

    @Override
    public RecipeType<?> getType() {
        return ArcaneRecipeTypes.CLEANSING.get();
    }

    @Override
    @Deprecated
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        throw new RuntimeException("Cannot invoke RecipeCleansing#assemble(SimpleContainer, RegistryAccess)");
    }

    public static Optional<RecipeCleansing> getRecipeFor(Level level, SimpleContainer container) {
        return getAllRecipes(level)
                .stream()
                .filter(recipe -> recipe.matches(container, level))
                .findFirst();
    }

    public static List<RecipeCleansing> getAllRecipes(Level level) {
        return level.getRecipeManager().getAllRecipesFor(ArcaneRecipeTypes.CLEANSING.get());
    }

    public static class Serializer implements RecipeSerializer<RecipeCleansing> {
        @Override
        public RecipeCleansing fromJson(ResourceLocation id, JsonObject json) {
            ItemStack input = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "input"));
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            return new RecipeCleansing(id, input, result);
        }

        @Override
        public @Nullable RecipeCleansing fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            return new RecipeCleansing(id, buf.readItem(), buf.readItem());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RecipeCleansing recipe) {
            buf.writeItemStack(recipe.input, false);
            buf.writeItemStack(recipe.result, false);
        }
    }
}
