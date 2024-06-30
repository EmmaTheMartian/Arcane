package martian.arcane.common.recipe;

import martian.arcane.api.recipe.BlockIngredient;
import martian.arcane.api.item.ItemHelpers;
import martian.arcane.api.recipe.BlockOutput;
import martian.arcane.api.recipe.ItemEntityContainer;
import martian.arcane.api.recipe.RecipeOutput;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record RecipeCauldronMixing(
        NonNullList<Ingredient> inputs,
        NonNullList<RecipeOutput> results,
        BlockIngredient cauldron,
        Optional<BlockOutput> resultBlock,
        int fluidAmount
) implements Recipe<ItemEntityContainer> {
    public static final RecipeCauldronMixingType TYPE = new RecipeCauldronMixingType();

    @Override
    @Deprecated
    public boolean matches(ItemEntityContainer container, Level level) {
        throw new UnsupportedOperationException("Cannot invoke RecipeCauldronMixing#matches(ItemEntityContainer, Level)");
    }

    public boolean matches(ItemEntityContainer container, BlockState state) {
        if (!cauldron.test(state))
            return false;

        if (fluidAmount > 0 && state.hasProperty(LayeredCauldronBlock.LEVEL) && state.getValue(LayeredCauldronBlock.LEVEL) == 0)
            return false;

        for (Ingredient ingredient : inputs)
            if (!container.hasAnyMatching(ingredient))
                return false;

        return true;
    }

    private int findByIngredient(ItemEntityContainer container, Ingredient ingredient) {
        for (int i = 0; i < container.getContainerSize(); i++)
            if (ingredient.test(container.getItem(i)))
                return i;
        return -1;
    }

    @Override
    @Deprecated
    public ItemStack assemble(ItemEntityContainer container, HolderLookup.Provider registries) {
        throw new UnsupportedOperationException("Cannot invoke RecipeCauldronMixing#assemble(Container, HolderLookup.Provider)");
    }

    public void assemble(ItemEntityContainer container, Level level, BlockPos pos) {
        for (Ingredient ingredient : inputs) {
            container.removeItem(findByIngredient(container, ingredient), 1);
        }

        for (RecipeOutput output : results) {
            ItemHelpers.addItemEntity(level, output.roll(), pos);
        }

        if (fluidAmount > 0) {
            for (int i = 0; i < fluidAmount; i++) {
                if (level.getBlockState(pos).hasProperty(LayeredCauldronBlock.LEVEL))
                    LayeredCauldronBlock.lowerFillLevel(level.getBlockState(pos), level, pos);
                else if (level.getBlockState(pos).is(Blocks.LAVA_CAULDRON)) {
                    level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
                    break;
                }
            }
        }

        resultBlock.ifPresent(block -> level.setBlockAndUpdate(pos, block.block().defaultBlockState()));
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TYPE;
    }

    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }

    public static Optional<RecipeHolder<RecipeCauldronMixing>> getRecipeFor(Level level, ItemEntityContainer container, BlockState state) {
        return getAllRecipes(level)
                .stream()
                .filter(recipe -> recipe.value().matches(container, state))
                .findFirst();
    }

    public static List<RecipeHolder<RecipeCauldronMixing>> getAllRecipes(Level level) {
        return level.getRecipeManager().getAllRecipesFor(TYPE);
    }
}
