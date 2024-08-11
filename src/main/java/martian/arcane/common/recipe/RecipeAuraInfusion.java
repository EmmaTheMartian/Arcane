package martian.arcane.common.recipe;

import martian.arcane.api.item.ItemHelpers;
import martian.arcane.api.recipe.RecipeOutput;
import martian.arcane.api.recipe.SingleItemContainer;
import martian.arcane.api.recipe.SimpleRecipe;
import martian.arcane.common.block.infuser.BlockEntityAuraInfuser;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RecipeAuraInfusion extends SimpleRecipe<RecipeAuraInfusion.Container> {
    public static final RecipeAuraInfusionType TYPE = new RecipeAuraInfusionType();

    public final int aura;

    public RecipeAuraInfusion(Ingredient input, NonNullList<RecipeOutput> results, int aura) {
        super(input, results);
        this.aura = aura;
    }

    @Override
    public boolean matches(Container container, Level level) {
        return this.matchesWithoutAuraCost(container, level) && container.aura >= this.aura;
    }

    public boolean matchesWithoutAuraCost(Container container, Level ignoredLevel) {
        return this.input.test(container.getItem());
    }

    public final int aura() { return aura; }

    @Override
    @Deprecated
    public ItemStack assemble(Container container, HolderLookup.Provider access) {
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
            infuser.setItem(stacks.getFirst());
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
        return TYPE;
    }

    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }

    public static Optional<RecipeHolder<RecipeAuraInfusion>> getRecipeFor(Level level, Container container) {
        return getRecipeFor(level, container, false);
    }

    public static Optional<RecipeHolder<RecipeAuraInfusion>> getRecipeFor(Level level, Container container, boolean ignoreAuraCost) {
        return getAllRecipes(level)
                .stream()
                .filter(recipe -> ignoreAuraCost ?
                        recipe.value().matchesWithoutAuraCost(container, level)
                        : recipe.value().matches(container, level))
                .findFirst();
    }

    public static List<RecipeHolder<RecipeAuraInfusion>> getAllRecipes(Level level) {
        return level.getRecipeManager().getAllRecipesFor(TYPE);
    }

    public static class Container extends SingleItemContainer {
        public final int aura;

        public Container(ItemStack item, int aura) {
            super(item);
            this.aura = aura;
        }
    }
}
