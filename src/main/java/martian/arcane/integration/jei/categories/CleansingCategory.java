package martian.arcane.integration.jei.categories;

import martian.arcane.integration.jei.ArcaneJeiPlugin;
import martian.arcane.common.recipe.RecipeCleansing;
import martian.arcane.common.registry.ArcaneSpells;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;

@MethodsReturnNonnullByDefault
public class CleansingCategory extends AbstractSpellCategory<RecipeCleansing> {
    public CleansingCategory(IGuiHelper helper) {
        super(helper, "gui.arcane.jei.cleansing", ArcaneSpells.CLEANSING.getId(), ArcaneJeiPlugin.CLEANSING);
    }

    @Override
    protected ItemStack getInput(RecipeCleansing recipe) {
        return recipe.input;
    }

    @Override
    protected ItemStack getResult(RecipeCleansing recipe) {
        return recipe.result;
    }
}
