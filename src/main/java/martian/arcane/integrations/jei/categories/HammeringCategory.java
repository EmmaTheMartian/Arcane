package martian.arcane.integrations.jei.categories;

import martian.arcane.integrations.jei.ArcaneJeiPlugin;
import martian.arcane.recipe.RecipeHammering;
import martian.arcane.registry.ArcaneSpells;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;

@MethodsReturnNonnullByDefault
public class HammeringCategory extends AbstractSpellCategory<RecipeHammering> {
    public HammeringCategory(IGuiHelper helper) {
        super(helper, "gui.arcane.jei.hammering", ArcaneSpells.HAMMERING.getId(), ArcaneJeiPlugin.HAMMERING);
    }

    @Override
    protected ItemStack getInput(RecipeHammering recipe) {
        return recipe.input;
    }

    @Override
    protected ItemStack getResult(RecipeHammering recipe) {
        return recipe.result;
    }
}
