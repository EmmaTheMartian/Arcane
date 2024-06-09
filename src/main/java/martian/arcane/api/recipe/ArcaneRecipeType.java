package martian.arcane.api.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public interface ArcaneRecipeType<T extends Recipe<?>> extends RecipeType<T>, RecipeSerializer<T> {
}
