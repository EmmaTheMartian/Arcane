package martian.arcane.integration.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import martian.arcane.ArcaneMod;
import martian.arcane.common.recipe.RecipeAuraInfusion;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public class AuraInfusionEmiRecipe extends BasicEmiRecipe {
    public static final ResourceLocation OVERLAY = ArcaneMod.id("textures/gui/recipe_browsers/infusion_overlay.png");
    private static final int padding = 2;

    public AuraInfusionEmiRecipe(RecipeHolder<RecipeAuraInfusion> recipe) {
        super(ArcaneEmiPlugin.CATEGORY_AURA_INFUSION, recipe.id(), 32 + (18 * 2) + (padding * 2), (18 * 3) + (padding * 2));

        var r = recipe.value();

        this.inputs.add(EmiIngredient.of(r.input));
        r.results.forEach(it -> this.outputs.add(EmiStack.of(it.getStack()).setChance(it.getChance())));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(OVERLAY, padding, padding, 32, 34, 0, 0, 32, 34, 32, 34);

        widgets.addSlot(inputs.getFirst(), padding, padding);

        final int
                x = 30 + padding,
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
