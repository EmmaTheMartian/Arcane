package martian.arcane.api.recipe;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class SimpleRecipe<T extends SimpleContainer> implements Recipe<T> {
    public final Ingredient input;
    public final NonNullList<RecipeOutput> results;

    public SimpleRecipe(Ingredient input, NonNullList<RecipeOutput> results) {
        this.input = input;
        this.results = results;
    }

    public final Ingredient input() {
        return input;
    }

    public final NonNullList<RecipeOutput> results() {
        return results;
    }

    public NonNullList<ItemStack> getResultItems() {
        NonNullList<ItemStack> list = NonNullList.create();
        list.addAll(results.stream().map(RecipeOutput::roll).toList());
        return list;
    }

    @Override
    @Deprecated
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return results.getFirst().stack();
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        return this.input.test(container.getItem());
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    @Deprecated
    public ItemStack assemble(SimpleContainer container, HolderLookup.Provider registries) throws RuntimeException {
        throw new RuntimeException("Cannot invoke SimpleRecipe#assemble(SimpleContainer, HolderLookup.Provider)");
    }
}
