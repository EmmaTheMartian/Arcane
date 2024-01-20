package martian.arcane.recipe.spellcrafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import martian.arcane.ArcaneMod;
import martian.arcane.block.entity.BlockEntitySpellcraftingCore;
import martian.arcane.registry.ArcaneRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

// Input ItemStack must have a count of 1!
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RecipeSpellcrafting implements Recipe<SpellcraftingContainer> {
    public static final String NAME = "spellcrafting";
    public static final ResourceLocation ID = new ResourceLocation(ArcaneMod.MODID, NAME);
    public static int spellcraftingAuraCost = 256;

    public final ResourceLocation id;
    public final Ingredient centerIngredient;
    public final NonNullList<Ingredient> pylonIngredients;
    public final ItemStack result;

    public RecipeSpellcrafting(ResourceLocation id, Ingredient centerIngredient, NonNullList<Ingredient> pylonIngredients, ItemStack result) {
        this.id = id;
        this.centerIngredient = centerIngredient;
        this.pylonIngredients = pylonIngredients;
        this.result = result;
    }

    @Override
    public boolean matches(SpellcraftingContainer container, Level level) {
        return this.testCenter(container) && this.testPylons(container);
    }

    public boolean testCenter(SpellcraftingContainer container) {
        return this.centerIngredient.test(container.getCenterItem());
    }

    public boolean testPylons(SpellcraftingContainer container) {
        for (Ingredient ing : pylonIngredients) {
            if (!ingredientIsPresent(ing, container))
                return false;
        }
        return true;
    }

    public boolean ingredientIsPresent(Ingredient ingredient, SpellcraftingContainer container) {
        for (ItemStack stack : container.getPylonItems()) {
            if (ingredient.test(stack))
                return true;
        }
        return false;
    }

    @Override
    public ItemStack assemble(SpellcraftingContainer container, RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    public void assemble(BlockEntitySpellcraftingCore core) {
        core.mapAuraStorage(aura -> {
            aura.setAura(aura.getAura() - spellcraftingAuraCost);
            return 0;
        });
        core.clearPylonItems();
        core.setItem(result.copy());
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
        return ArcaneRecipeTypes.SPELLCRAFTING.get();
    }

    public static Optional<RecipeSpellcrafting> getRecipeFor(Level level, SpellcraftingContainer container) {
        return getAllRecipes(level)
                .stream()
                .filter(recipe -> recipe.matches(container, level))
                .findFirst();
    }

    public static List<RecipeSpellcrafting> getAllRecipes(Level level) {
        return level.getRecipeManager().getAllRecipesFor(ArcaneRecipeTypes.SPELLCRAFTING.get());
    }

    public static class Serializer implements RecipeSerializer<RecipeSpellcrafting> {
        public Serializer() {}

        @Override
        public RecipeSpellcrafting fromJson(ResourceLocation id, JsonObject json) {
            Ingredient center = Ingredient.fromJson(json.get("center"));
            NonNullList<Ingredient> pylons = NonNullList.create();
            for (JsonElement element : GsonHelper.getAsJsonArray(json, "ingredients").asList())
                pylons.add(Ingredient.fromJson(element));

            if (pylons.size() != 6)
                throw new JsonParseException("ingredients must contain exactly 6 elements. For empty ingredients, use `minecraft:air` (In " + id + ")");

            ItemStack result = CraftingHelper.getItemStack(json.getAsJsonObject("result"), true);

            return new RecipeSpellcrafting(id, center, pylons, result);
        }

        @Override
        public RecipeSpellcrafting fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient center = Ingredient.fromNetwork(buf);
            int pylonIngredients = buf.readInt();
            NonNullList<Ingredient> pylons = NonNullList.create();
            for (int i = 0; i < pylonIngredients; i++)
                pylons.add(Ingredient.fromNetwork(buf));
            ItemStack output = buf.readItem();
            return new RecipeSpellcrafting(id, center, pylons, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RecipeSpellcrafting recipe) {
            recipe.centerIngredient.toNetwork(buf);
            buf.writeInt(recipe.pylonIngredients.size());
            for (Ingredient i : recipe.pylonIngredients)
                i.toNetwork(buf);
            buf.writeItemStack(recipe.result, false);
        }
    }
}
