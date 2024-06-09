package martian.arcane.integration.jei.categories;

/*
import martian.arcane.ArcaneTags;
import martian.arcane.api.recipe.RecipeOutput;
import martian.arcane.common.registry.ArcaneItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@MethodsReturnNonnullByDefault
public abstract class AbstractSpellCategory<T extends Recipe<?>> implements IRecipeCategory<T> {
    private final IDrawable background, icon;
    private final RecipeType<T> jeiRecipeType;
    private final String translationKey;

    public AbstractSpellCategory(IGuiHelper helper, String translationKey, RecipeType<T> jeiRecipeType) {
        background = helper.createBlankDrawable(114, 60);
        icon = helper.createDrawableItemStack(new ItemStack(ArcaneItems.WAND_OAK.get()));
        this.translationKey = translationKey;
        this.jeiRecipeType = jeiRecipeType;
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
        builder.addSlot(RecipeIngredientRole.INPUT, 12, 20)
                .addIngredients(getInput(recipe));

        builder.addSlot(RecipeIngredientRole.CATALYST, 30, 20)
                .addIngredients(Ingredient.of(ArcaneTags.WANDS));

        final int x = 66, y = 12, xInc = 18, yInc = 18;
        AtomicInteger curX = new AtomicInteger(0), curY = new AtomicInteger(0);
        getResults(recipe).forEach(r -> {
            builder.addSlot(RecipeIngredientRole.OUTPUT, x + xInc * curX.get(), y + yInc * curY.get())
                    .addItemStack(r.stack())
                    .addTooltipCallback(makeTooltipCallback(r));

            curX.incrementAndGet();
            if (curX.get() % 2 == 0) {
                curY.incrementAndGet();
                curX.set(0);
            }
        });
    }

    protected abstract Ingredient getInput(T recipe);
    protected abstract List<RecipeOutput> getResults(T recipe);

    public static IRecipeSlotTooltipCallback makeTooltipCallback(RecipeOutput recipe) {
        if (recipe.chance() >= 1)
            return (view, tooltip) -> {};

        return (view, tooltip) ->
                tooltip.add(Component
                        .translatable("messages.arcane.chance")
                        .append(Float.toString(recipe.chance() * 100))
                        .append("%")
                        .withStyle(ChatFormatting.GOLD));
    }
}

 */
