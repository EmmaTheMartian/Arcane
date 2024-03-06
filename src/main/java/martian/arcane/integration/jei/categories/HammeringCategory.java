package martian.arcane.integration.jei.categories;

import martian.arcane.api.recipe.RecipeOutput;
import martian.arcane.common.recipe.RecipeHammering;
import martian.arcane.integration.jei.ArcaneJeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

@MethodsReturnNonnullByDefault
public class HammeringCategory extends AbstractSpellCategory<RecipeHammering> {
    public HammeringCategory(IGuiHelper helper) {
        super(helper, "gui.arcane.jei.hammering", ArcaneJeiPlugin.HAMMERING);
    }

    @Override
    protected Ingredient getInput(RecipeHammering recipe) {
        return recipe.input;
    }

    @Override
    protected List<RecipeOutput> getResults(RecipeHammering recipe) {
        return recipe.results;
    }
}
