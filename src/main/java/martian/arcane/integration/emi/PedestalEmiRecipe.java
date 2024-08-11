package martian.arcane.integration.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import martian.arcane.ArcaneMod;
import martian.arcane.common.ArcaneContent;
import martian.arcane.common.recipe.RecipePedestalCrafting;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public class PedestalEmiRecipe extends BasicEmiRecipe {
    public static final ResourceLocation OVERLAY = ArcaneMod.id("textures/gui/recipe_browsers/pedestal_interaction_overlay.png");
    private static final int padding = 2;

    public PedestalEmiRecipe(RecipeHolder<RecipePedestalCrafting> recipe) {
        super(ArcaneEmiPlugin.CATEGORY_PEDESTAL, recipe.id(), 64 + (18 * 2) + (padding * 2), (18 * 3) + (padding * 2));

        var r = recipe.value();

        this.inputs.add(EmiIngredient.of(r.input));
        this.inputs.add(EmiIngredient.of(r.interactionItem));
        r.results.forEach(it -> this.outputs.add(EmiStack.of(it.getStack()).setChance(it.getChance())));
        catalysts.add(EmiStack.of(ArcaneContent.BE_PEDESTAL.block()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(OVERLAY, padding, padding, 64, 48, 0, 0, 64, 48, 64, 48);

        widgets.addSlot(inputs.getFirst(), 25 + padding, padding);
        widgets.addSlot(inputs.get(1), 5 + padding, padding);

        final int
                x = 55 + padding,
                y = outputs.size() > 2 ? padding : 20 + padding,
                xInc = 18,
                yInc = 18;
        int curX = 0, curY = 0;
        for (EmiStack stack : outputs) {
            widgets.addSlot(stack, x + xInc * curX, y + yInc * curY).appendTooltip(Component
                    .translatable("messages.arcane.chance")
                    .append(Float.toString(stack.getChance() * 100))
                    .append("%")
                    .withStyle(ChatFormatting.GOLD));

            curX++;
            if (curX % 2 == 0) {
                curY++;
                curX = 0;
            }
        }
    }
}
