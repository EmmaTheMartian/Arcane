package martian.arcane.integrations.jei.categories;

import com.mojang.blaze3d.systems.RenderSystem;
import martian.arcane.integrations.jei.ArcaneJeiPlugin;
import martian.arcane.recipe.RecipeAuraInfusion;
import martian.arcane.registry.ArcaneBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
public class AuraInfusionCategory implements IRecipeCategory<RecipeAuraInfusion> {
    private final IDrawable background, icon;

    public AuraInfusionCategory(IGuiHelper helper) {
        background = helper.createBlankDrawable(142, 52);
        icon = helper.createDrawableItemStack(new ItemStack(ArcaneBlocks.AURA_INFUSER.get()));
    }

    @Override
    public RecipeType<RecipeAuraInfusion> getRecipeType() {
        return ArcaneJeiPlugin.AURA_INFUSION;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.arcane.jei.aura_infusion");
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
    public void draw(RecipeAuraInfusion recipe, IRecipeSlotsView slots, GuiGraphics gui, double mouseX, double mouseY) {
        RenderSystem.enableBlend();
        // 16733695 is the result of ChatFormatting.LIGHT_PURPLE.getColor()
        gui.drawString(Minecraft.getInstance().font, "Aura: %s".formatted(recipe.aura), 48, 34, 16733695);
        RenderSystem.disableBlend();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeAuraInfusion recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 32, 12)
                .addItemStack(recipe.input);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 12)
                .addItemStack(recipe.result);
    }
}
