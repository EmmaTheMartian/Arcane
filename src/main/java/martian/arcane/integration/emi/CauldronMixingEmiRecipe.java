package martian.arcane.integration.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneTags;
import martian.arcane.api.recipe.BlockIngredient;
import martian.arcane.common.recipe.RecipeCauldronMixing;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public class CauldronMixingEmiRecipe extends BasicEmiRecipe {
    public static final ResourceLocation OVERLAY = ArcaneMod.id("textures/gui/recipe_browsers/cauldron_mixing_overlay.png");
    private static final int padding = 2;

    protected final BlockIngredient blockIngredient;

    public CauldronMixingEmiRecipe(RecipeHolder<RecipeCauldronMixing> recipe) {
        super(ArcaneEmiPlugin.CATEGORY_MIXING, recipe.id(), 64 + (18 * 2) + (padding * 2), (18 * 3) + (padding * 2));

        var r = recipe.value();

        r.inputs().forEach(it -> this.inputs.add(EmiIngredient.of(it)));
        this.inputs.add(EmiIngredient.of(r.cauldron().toNonBlockIngredient()));
        r.results().forEach(it -> this.outputs.add(EmiStack.of(it.getStack()).setChance(it.getChance())));
        r.resultBlock().ifPresent(it -> this.outputs.add(EmiStack.of(it.block())));
        catalysts.add(EmiIngredient.of(ArcaneTags.WANDS));
        blockIngredient = r.cauldron();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(OVERLAY, padding, padding, 64, 48, 0, 0, 64, 48, 64, 48);

        widgets.addSlot(inputs.getFirst(), 25 + padding, padding);
        widgets.addSlot(catalysts.getFirst(), 5 + padding, padding).appendTooltip(Component
                .translatable("messages.arcane.spell")
                .append(Component.translatable("spell.arcane.mixing.name"))
                .withStyle(ChatFormatting.GOLD));
        widgets.add(new BlockWidget(20 + 5 + padding, 20 + padding, blockIngredient));

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
