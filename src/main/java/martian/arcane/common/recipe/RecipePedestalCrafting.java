package martian.arcane.common.recipe;

import martian.arcane.api.item.ItemHelpers;
import martian.arcane.api.recipe.RecipeOutput;
import martian.arcane.api.recipe.SimpleContainer;
import martian.arcane.api.recipe.SimpleRecipe;
import martian.arcane.common.block.pedestal.BlockEntityPedestal;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
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

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RecipePedestalCrafting extends SimpleRecipe<SimpleContainer> {
    public static final RecipePedestalType TYPE = new RecipePedestalType();

    public final Ingredient interactionItem;
    public final boolean consumeInteractionItem;

    public RecipePedestalCrafting(Ingredient input, Ingredient interactionItem, boolean consumeInteractionItem, NonNullList<RecipeOutput> results) {
        super(input, results);
        this.interactionItem = interactionItem;
        this.consumeInteractionItem = consumeInteractionItem;
    }

    public final Ingredient interactionItem() { return interactionItem; }
    public final boolean consumeInteractionItem() { return consumeInteractionItem; }

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
                pedestal.setItem(stacks.getFirst());
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
        return TYPE;
    }

    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }

    public static Optional<RecipeHolder<RecipePedestalCrafting>> getRecipeFor(Level level, ItemStack pedestalStack, ItemStack interactionStack) {
        return getAllRecipes(level)
                .stream()
                .filter(recipe -> recipe.value().matches(pedestalStack, interactionStack))
                .findFirst();
    }

    public static List<RecipeHolder<RecipePedestalCrafting>> getAllRecipes(Level level) {
        return level.getRecipeManager().getAllRecipesFor(TYPE);
    }
}
