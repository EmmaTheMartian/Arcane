package martian.arcane.integration.jei.categories;

import martian.arcane.integration.jei.ArcaneJeiPlugin;
import martian.arcane.common.recipe.RecipePurifying;
import martian.arcane.common.registry.ArcaneSpells;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;

@MethodsReturnNonnullByDefault
public class PurifyingCategory extends AbstractSpellCategory<RecipePurifying> {
    public PurifyingCategory(IGuiHelper helper) {
        super(helper, "gui.arcane.jei.purifying", ArcaneSpells.PURIFYING.getId(), ArcaneJeiPlugin.PURIFYING);
    }

    @Override
    protected ItemStack getInput(RecipePurifying recipe) {
        return recipe.input;
    }

    @Override
    protected ItemStack getResult(RecipePurifying recipe) {
        return recipe.result;
    }
}
