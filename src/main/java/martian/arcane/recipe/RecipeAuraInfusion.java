package martian.arcane.recipe;

import com.google.gson.JsonObject;
import martian.arcane.ArcaneMod;
import martian.arcane.api.recipe.SimpleContainer;
import martian.arcane.block.entity.BlockEntityAuraInfuser;
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
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

// Input ItemStack must have a count of 1!
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RecipeAuraInfusion implements Recipe<RecipeAuraInfusion.Container> {
    public static final String NAME = "aura_infusion";
    public static final ResourceLocation ID = ArcaneMod.id(NAME);

    public final ResourceLocation id;
    public final ItemStack input;
    public final ItemStack result;
    public final int aura;

    public RecipeAuraInfusion(ResourceLocation id, ItemStack input, ItemStack result, int aura) {
        this.id = id;
        this.input = input;
        this.result = result;
        this.aura = aura;
    }

    @Override
    public boolean matches(Container container, Level level) {
        return this.matchesWithoutAuraCost(container, level) && container.aura >= this.aura;
    }

    public boolean matchesWithoutAuraCost(Container container, Level ignoredLevel) {
        return this.input.is(container.getItem().getItem());
    }

    @Override
    @Deprecated
    public ItemStack assemble(Container container, RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    public void assemble(BlockEntityAuraInfuser infuser) {
        infuser.setItem(result.copy());
        infuser.auraProgress -= aura;
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
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
        return ArcaneRecipeTypes.AURA_INFUSION.get();
    }

    public static Optional<RecipeAuraInfusion> getRecipeFor(Level level, Container container) {
        return getRecipeFor(level, container, false);
    }

    public static Optional<RecipeAuraInfusion> getRecipeFor(Level level, Container container, boolean ignoreAuraCost) {
        return getAllRecipes(level)
                .stream()
                .filter(recipe -> ignoreAuraCost ?
                        recipe.matchesWithoutAuraCost(container, level)
                        : recipe.matches(container, level))
                .findFirst();
    }

    public static List<RecipeAuraInfusion> getAllRecipes(Level level) {
        return level.getRecipeManager().getAllRecipesFor(ArcaneRecipeTypes.AURA_INFUSION.get());
    }

    public static class Serializer implements RecipeSerializer<RecipeAuraInfusion> {
        public Serializer() {}

        @Override
        public RecipeAuraInfusion fromJson(ResourceLocation id, JsonObject json) {
            ItemStack input = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "input"));
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            int aura = GsonHelper.getAsInt(json, "aura");
            return new RecipeAuraInfusion(id, input, output, aura);
        }

        @Override
        public RecipeAuraInfusion fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ItemStack input = buf.readItem();
            ItemStack output = buf.readItem();
            int aura = buf.readInt();
            return new RecipeAuraInfusion(id, input, output, aura);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RecipeAuraInfusion recipe) {
            buf.writeItemStack(recipe.input, false);
            buf.writeItemStack(recipe.result, false);
            buf.writeInt(recipe.aura);
        }
    }

    public static class Container extends SimpleContainer {
        public final int aura;

        public Container(IItemHandlerModifiable items, int aura) {
            super(items);
            this.aura = aura;
        }
    }
}
