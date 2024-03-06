package martian.arcane.integration.jei.categories;

import martian.arcane.api.recipe.RecipeOutput;
import martian.arcane.common.recipe.RecipePedestalCrafting;
import martian.arcane.common.registry.ArcaneBlocks;
import martian.arcane.integration.jei.ArcaneJeiPlugin;
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
import java.util.concurrent.atomic.AtomicInteger;

@MethodsReturnNonnullByDefault
public class PedestalCategory implements IRecipeCategory<RecipePedestalCrafting> {
    private final IDrawable background, icon;

    public PedestalCategory(IGuiHelper helper) {
        background = helper.createBlankDrawable(114, 60);
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
        builder.addSlot(RecipeIngredientRole.INPUT, 12, 20)
                .addIngredients(recipe.input);

        builder.addSlot(RecipeIngredientRole.INPUT, 30, 20)
                .addIngredients(recipe.interactionItem);

        final int x = 66, y = 12, xInc = 18, yInc = 18;
        AtomicInteger curX = new AtomicInteger(0), curY = new AtomicInteger(0);
        recipe.results.forEach(r -> {
            builder.addSlot(RecipeIngredientRole.OUTPUT, x + xInc * curX.get(), y + yInc * curY.get())
                    .addItemStack(r.stack())
                    .addTooltipCallback(AbstractSpellCategory.makeTooltipCallback(r));

            curX.incrementAndGet();
            if (curX.get() % 2 == 0) {
                curY.incrementAndGet();
                curX.set(0);
            }
        });
    }
}
