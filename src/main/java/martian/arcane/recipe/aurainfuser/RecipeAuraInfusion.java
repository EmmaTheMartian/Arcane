package martian.arcane.recipe.aurainfuser;

import com.google.gson.JsonObject;
import martian.arcane.ArcaneMod;
import martian.arcane.api.recipe.SimpleRecipe;
import martian.arcane.block.entity.BlockEntityAuraInfuser;
import martian.arcane.registry.ArcaneRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

// Input ItemStack must have a count of 1!
public class RecipeAuraInfusion extends SimpleRecipe<AuraInfusionContainer> {
    public static final String NAME = "aura_infusion";
    public static final ResourceLocation ID = new ResourceLocation(ArcaneMod.MODID, NAME);

    public final int aura;

    public RecipeAuraInfusion(Ingredient ingredient, ItemStack result, int aura) {
        super(ID, ingredient, result);
        this.aura = aura;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(AuraInfusionContainer container, Level level) {
        return super.matches(container, level) && container.aura >= aura;
    }

    public void assemble(BlockEntityAuraInfuser infuser) {
        infuser.setItem(result.copy());
        infuser.auraProgress -= aura;
    }

    @Override
    @NotNull
    public RecipeSerializer<?> getSerializer() {
        return new Serializer();
    }

    @Override
    @NotNull
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
        @NotNull
        @ParametersAreNonnullByDefault
        public RecipeAuraInfusion fromJson(ResourceLocation id, JsonObject json) {
            ArcaneMod.LOGGER.warn(".ARCANE. fromJson called");
            Ingredient input = Ingredient.fromJson(json.get("ingredient"));
            ItemStack output = CraftingHelper.getItemStack(json.getAsJsonObject("result"), true);
            int aura = json.get("aura").getAsInt();
            return new RecipeAuraInfusion(input, output, aura);
        }

        @Override
        @ParametersAreNonnullByDefault
        public RecipeAuraInfusion fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient input = Ingredient.fromNetwork(buf);
            ItemStack output = buf.readItem();
            int aura = buf.readInt();
            return new RecipeAuraInfusion(input, output, aura);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void toNetwork(FriendlyByteBuf buf, RecipeAuraInfusion recipe) {
            recipe.ingredient.toNetwork(buf);
            buf.writeItemStack(recipe.result, false);
            buf.writeInt(recipe.aura);
        }
    }
}
