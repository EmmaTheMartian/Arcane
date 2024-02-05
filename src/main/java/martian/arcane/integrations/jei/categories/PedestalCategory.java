package martian.arcane.integrations.jei.categories;

import martian.arcane.integrations.jei.ArcaneJeiPlugin;
import martian.arcane.recipe.RecipePedestalCrafting;
import martian.arcane.registry.ArcaneBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
public class PedestalCategory implements IRecipeCategory<RecipePedestalCrafting> {
    private final IDrawable background, icon;

    public PedestalCategory(IGuiHelper helper) {
        background = helper.createBlankDrawable(142, 52);
        icon = helper.createDrawableItemStack(new ItemStack(ArcaneBlocks.PEDESTAL.get()));
    }

    @Override
    public RecipeType<RecipePedestalCrafting> getRecipeType() {
        return ArcaneJeiPlugin.PEDESTAL;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.arcane.jei.pedestal");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setRecipe(IRecipeLayoutBuilder builder, RecipePedestalCrafting recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 32, 12)
                .addItemStack(recipe.input);

        builder.addSlot(RecipeIngredientRole.INPUT, 50, 12)
                .addItemStack(recipe.interactionItem);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 12)
                .addItemStack(recipe.result);
    }
}
