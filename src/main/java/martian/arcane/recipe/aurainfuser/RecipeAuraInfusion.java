package martian.arcane.recipe.aurainfuser;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import martian.arcane.ArcaneMod;
import martian.arcane.block.entity.BlockEntityAuraInfuser;
import martian.arcane.registry.ArcaneRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

// Input ItemStack must have a count of 1!
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public record RecipeAuraInfusion(ItemStack input, ItemStack result, int aura) implements Recipe<AuraInfusionContainer> {
    public static final String NAME = "aura_infusion";
    public static final ResourceLocation ID = new ResourceLocation(ArcaneMod.MODID, NAME);

    @Override
    public boolean matches(AuraInfusionContainer container, Level level) {
        return this.input.is(container.getItem().getItem()) && container.aura >= aura;
    }

    @Override
    public ItemStack assemble(AuraInfusionContainer container, RegistryAccess access) {
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
        return ID;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return new Serializer();
    }

    @Override
    public RecipeType<?> getType() {
        return ArcaneRecipeTypes.AURA_INFUSION.get();
    }

    public static Optional<RecipeAuraInfusion> getRecipeFor(Level level, AuraInfusionContainer container) {
        return getAllRecipes(level)
                .stream()
                .filter(recipe -> recipe.matches(container, level))
                .findFirst();
    }

    public static List<RecipeAuraInfusion> getAllRecipes(Level level) {
        return level.getRecipeManager().getAllRecipesFor(ArcaneRecipeTypes.AURA_INFUSION.get());
    }

    public static class Serializer implements RecipeSerializer<RecipeAuraInfusion> {
        public Serializer() {}

        @Override
        public RecipeAuraInfusion fromJson(ResourceLocation id, JsonObject json) {
            ArcaneMod.LOGGER.warn(".ARCANE. fromJson called");
            ItemStack input = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "input"));
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            int aura = GsonHelper.getAsInt(json, "aura");
            return new RecipeAuraInfusion(input, output, aura);
        }

        @Override
        public RecipeAuraInfusion fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ItemStack input = buf.readItem();
            ItemStack output = buf.readItem();
            int aura = buf.readInt();
            return new RecipeAuraInfusion(input, output, aura);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RecipeAuraInfusion recipe) {
            buf.writeItemStack(recipe.input, false);
            buf.writeItemStack(recipe.result, false);
            buf.writeInt(recipe.aura);
        }
    }
}
