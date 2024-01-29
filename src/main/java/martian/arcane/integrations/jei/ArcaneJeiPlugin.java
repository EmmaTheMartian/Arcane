package martian.arcane.integrations.jei;

import martian.arcane.ArcaneMod;
import martian.arcane.integrations.jei.categories.AuraInfusionCategory;
import martian.arcane.recipe.aurainfuser.RecipeAuraInfusion;
import martian.arcane.registry.ArcaneBlocks;
import martian.arcane.registry.ArcaneRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class ArcaneJeiPlugin implements IModPlugin {
    @Override
    @NotNull
    public ResourceLocation getPluginUid() {
        return ArcaneMod.id("arcane_jei");
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registry) {
        RecipeManager rm = getRecipeManager();
        if (rm == null) {
            ArcaneMod.LOGGER.error("Failed to get RecipeManager in ArcaneJeiPlugin#registerRecipes");
            return;
        }

        registry.addRecipes(AURA_INFUSION, rm.getAllRecipesFor(ArcaneRecipeTypes.AURA_INFUSION.get()));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new AuraInfusionCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(ArcaneBlocks.AURA_INFUSER.get()), AURA_INFUSION);
    }

    private static RecipeManager getRecipeManager() {
        ClientLevel l = Minecraft.getInstance().level;
        return l == null ? null : l.getRecipeManager();
    }

    public static final RecipeType<RecipeAuraInfusion> AURA_INFUSION = new RecipeType<>(RecipeAuraInfusion.ID, RecipeAuraInfusion.class);
}
