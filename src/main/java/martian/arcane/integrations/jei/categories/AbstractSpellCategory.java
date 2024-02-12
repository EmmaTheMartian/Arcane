package martian.arcane.integrations.jei.categories;

import martian.arcane.item.ItemAuraWand;
import martian.arcane.registry.ArcaneItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
public abstract class AbstractSpellCategory<T extends Recipe<?>> implements IRecipeCategory<T> {
    private final ItemStack WAND = new ItemStack(ArcaneItems.WAND_OAK.get(), 1);
    private final IDrawable background, icon;
    private final RecipeType<T> jeiRecipeType;
    private final String translationKey;

    public AbstractSpellCategory(IGuiHelper helper, String translationKey, ResourceLocation spellId, RecipeType<T> jeiRecipeType) {
        background = helper.createBlankDrawable(142, 52);
        icon = helper.createDrawableItemStack(new ItemStack(ArcaneItems.WAND_OAK.get()));
        this.translationKey = translationKey;
        this.jeiRecipeType = jeiRecipeType;
        ((ItemAuraWand)WAND.getItem()).setSpell(spellId, WAND);
    }

    @Override
    public RecipeType<T> getRecipeType() {
        return jeiRecipeType;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(this.translationKey);
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
    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 32, 12)
                .addItemStack(getInput(recipe));

        builder.addSlot(RecipeIngredientRole.CATALYST, 50, 12)
                .addItemStack(WAND);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 12)
                .addItemStack(getResult(recipe));
    }

    protected abstract ItemStack getInput(T recipe);
    protected abstract ItemStack getResult(T recipe);
}
