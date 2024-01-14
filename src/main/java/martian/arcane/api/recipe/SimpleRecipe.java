package martian.arcane.api.recipe;

import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
public abstract class SimpleRecipe<C extends SimpleContainer> implements Recipe<C> {
    public final ResourceLocation id;
    public final Ingredient ingredient;
    public final ItemStack result;

    public SimpleRecipe(ResourceLocation id, Ingredient ingredients, ItemStack results) {
        this.id = id;
        this.ingredient = ingredients;
        this.result = results;
    }

    @Override
    public boolean matches(C container, Level level) {
        return this.ingredient.test(container.getItem());
    }

    @Override
    @NotNull
    public ItemStack assemble(C container, RegistryAccess access) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    @Override
    @NotNull
    public ItemStack getResultItem(RegistryAccess access) {
        return this.result.copy();
    }

    @Override
    @NotNull
    public ResourceLocation getId() {
        return this.id;
    }
}
