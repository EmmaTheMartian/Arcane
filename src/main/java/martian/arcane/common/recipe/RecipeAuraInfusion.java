package martian.arcane.common.recipe;

import com.google.gson.JsonObject;
import martian.arcane.ArcaneMod;
import martian.arcane.api.item.ItemHelpers;
import martian.arcane.api.recipe.RecipeOutput;
import martian.arcane.api.recipe.SimpleContainer;
import martian.arcane.api.recipe.SimpleRecipe;
import martian.arcane.common.block.aura.infuser.BlockEntityAuraInfuser;
import martian.arcane.common.registry.ArcaneRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RecipeAuraInfusion extends SimpleRecipe<RecipeAuraInfusion.Container> {
    public static final String NAME = "aura_infusion";
    public static final ResourceLocation ID = ArcaneMod.id(NAME);

    public final int aura;

    public RecipeAuraInfusion(ResourceLocation id, Ingredient input, NonNullList<RecipeOutput> results, int aura) {
        super(ArcaneRecipeTypes.AURA_INFUSION.get(), id, input, results);
        this.aura = aura;
    }

    @Override
    public boolean matches(Container container, Level level) {
        return this.matchesWithoutAuraCost(container, level) && container.aura >= this.aura;
    }

    public boolean matchesWithoutAuraCost(Container container, Level ignoredLevel) {
        return this.input.test(container.getItem());
    }

    @Override
    @Deprecated
    public ItemStack assemble(Container container, RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    public void assemble(BlockEntityAuraInfuser infuser) {
        var stacks = results.stream()
                .map(RecipeOutput::roll)
                .filter(stack -> !stack.isEmpty())
                .toList();

        if (stacks.isEmpty())
            return;

        if (stacks.size() == 1) {
            infuser.setItem(stacks.get(0));
        } else {
            infuser.setItem(ItemStack.EMPTY);
            Level level = infuser.getLevel();
            BlockPos pos = infuser.getBlockPos().above();
            stacks.forEach(result -> ItemHelpers.addItemEntity(level, result, pos));
        }

        infuser.auraProgress -= aura;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return new Serializer();
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
        @Override
        @NotNull
        public RecipeAuraInfusion fromJson(ResourceLocation id, JsonObject json) {
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
            NonNullList<RecipeOutput> results = NonNullList.create();
            GsonHelper.getAsJsonArray(json, "results").forEach(result ->
                    results.add(RecipeOutput.fromJson(result.getAsJsonObject())));
            int aura = GsonHelper.getAsInt(json, "aura");
            return new RecipeAuraInfusion(id, input, results, aura);
        }

        @Override
        public @Nullable RecipeAuraInfusion fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient input = Ingredient.fromNetwork(buf);
            int resultsLength = buf.readInt();
            NonNullList<RecipeOutput> results = NonNullList.create();
            for (int i = 0; i < resultsLength; i++)
                results.add(RecipeOutput.fromNetwork(buf));
            int aura = buf.readInt();
            return new RecipeAuraInfusion(id, input, results, aura);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RecipeAuraInfusion recipe) {
            recipe.input.toNetwork(buf);
            buf.writeInt(recipe.results.size());
            recipe.results.forEach(res -> res.toNetwork(buf));
            buf.writeInt(recipe.aura);
        }
    }

    public static class Container extends SimpleContainer {
        public final int aura;

        public Container(ItemStack item, int aura) {
            super(item);
            this.aura = aura;
        }
    }
}
