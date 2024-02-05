package martian.arcane.integrations.jei.categories;

import martian.arcane.integrations.jei.ArcaneJeiPlugin;
import martian.arcane.item.ItemAuraWand;
import martian.arcane.recipe.RecipeHammering;
import martian.arcane.registry.ArcaneItems;
import martian.arcane.registry.ArcaneSpells;
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
public class HammeringCategory implements IRecipeCategory<RecipeHammering> {
    private static final ItemStack HAMMERING_WAND = new ItemStack(ArcaneItems.WAND_OAK.get(), 1);

    static {
        ((ItemAuraWand)HAMMERING_WAND.getItem()).setSpell(ArcaneSpells.HAMMERING.getId(), HAMMERING_WAND);
    }

    private final IDrawable background, icon;

    public HammeringCategory(IGuiHelper helper) {
        background = helper.createBlankDrawable(142, 52);
        icon = helper.createDrawableItemStack(new ItemStack(ArcaneItems.WAND_OAK.get()));
    }

    @Override
    public RecipeType<RecipeHammering> getRecipeType() {
        return ArcaneJeiPlugin.HAMMERING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.arcane.jei.hammering");
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHammering recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 32, 12)
                .addItemStack(recipe.input);

        builder.addSlot(RecipeIngredientRole.CATALYST, 50, 12)
                .addItemStack(HAMMERING_WAND);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 12)
                .addItemStack(recipe.result);
    }
}
