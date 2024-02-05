package martian.arcane.integrations.jei;

import martian.arcane.ArcaneMod;
import martian.arcane.integrations.jei.categories.AuraInfusionCategory;
import martian.arcane.integrations.jei.categories.HammeringCategory;
import martian.arcane.integrations.jei.categories.PedestalCategory;
import martian.arcane.item.ItemSpellTablet;
import martian.arcane.recipe.RecipeAuraInfusion;
import martian.arcane.recipe.RecipeHammering;
import martian.arcane.recipe.RecipePedestalCrafting;
import martian.arcane.registry.ArcaneBlocks;
import martian.arcane.registry.ArcaneItems;
import martian.arcane.registry.ArcaneRecipeTypes;
import martian.arcane.registry.ArcaneSpells;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
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

        ArcaneMod.LOGGER.info("ArcaneJeiPlugin#registerRecipes");
        registry.addRecipes(AURA_INFUSION, rm.getAllRecipesFor(ArcaneRecipeTypes.AURA_INFUSION.get()));
        registry.addRecipes(PEDESTAL, rm.getAllRecipesFor(ArcaneRecipeTypes.PEDESTAL.get()));
        registry.addRecipes(HAMMERING, rm.getAllRecipesFor(ArcaneRecipeTypes.HAMMERING.get()));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        ArcaneMod.LOGGER.info("ArcaneJeiPlugin#registerCategories");
        registry.addRecipeCategories(new AuraInfusionCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new PedestalCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new HammeringCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        ArcaneMod.LOGGER.info("ArcaneJeiPlugin#registerRecipeCatalysts");
        registry.addRecipeCatalyst(new ItemStack(ArcaneBlocks.AURA_INFUSER.get()), AURA_INFUSION);

        registry.addRecipeCatalyst(new ItemStack(ArcaneBlocks.PEDESTAL.get()), PEDESTAL);

        registry.addRecipeCatalyst(new ItemStack(ArcaneBlocks.PEDESTAL.get()), HAMMERING);
        ItemStack hammeringSpell = new ItemStack(ArcaneItems.SPELL_TABLET.get());
        CompoundTag tag = hammeringSpell.getOrCreateTag();
        ItemSpellTablet.initNBT(tag);
        ItemSpellTablet.setSpell(hammeringSpell, ArcaneSpells.HAMMERING.getId());
        registry.addRecipeCatalyst(hammeringSpell, HAMMERING);
    }

    private static RecipeManager getRecipeManager() {
        ClientLevel l = Minecraft.getInstance().level;
        return l == null ? null : l.getRecipeManager();
    }

    public static final RecipeType<RecipeAuraInfusion> AURA_INFUSION = new RecipeType<>(RecipeAuraInfusion.ID, RecipeAuraInfusion.class);
    public static final RecipeType<RecipePedestalCrafting> PEDESTAL = new RecipeType<>(RecipePedestalCrafting.ID, RecipePedestalCrafting.class);
    public static final RecipeType<RecipeHammering> HAMMERING = new RecipeType<>(RecipeHammering.ID, RecipeHammering.class);
}
