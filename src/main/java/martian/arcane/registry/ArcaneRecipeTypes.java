package martian.arcane.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.recipe.RecipeAuraInfusion;
import martian.arcane.recipe.RecipeHammering;
import martian.arcane.recipe.RecipePedestalCrafting;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class ArcaneRecipeTypes extends ArcaneRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ArcaneMod.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ArcaneMod.MODID);

    public static final RegistryObject<RecipeSerializer<RecipeAuraInfusion>> AURA_INFUSION_SERIALIZER =
            RECIPE_SERIALIZERS.register(RecipeAuraInfusion.NAME, RecipeAuraInfusion.Serializer::new);
    public static final RegistryObject<RecipeType<RecipeAuraInfusion>> AURA_INFUSION =
            RECIPE_TYPES.register(RecipeAuraInfusion.NAME, () -> RecipeType.simple(RecipeAuraInfusion.ID));

    public static final RegistryObject<RecipeSerializer<RecipeHammering>> HAMMERING_SERIALIZER =
            RECIPE_SERIALIZERS.register(RecipeHammering.NAME, RecipeHammering.Serializer::new);
    public static final RegistryObject<RecipeType<RecipeHammering>> HAMMERING =
            RECIPE_TYPES.register(RecipeHammering.NAME, () -> RecipeType.simple(RecipeHammering.ID));

    public static final RegistryObject<RecipeSerializer<RecipePedestalCrafting>> PEDESTAL_SERIALIZER =
            RECIPE_SERIALIZERS.register(RecipePedestalCrafting.NAME, RecipePedestalCrafting.Serializer::new);
    public static final RegistryObject<RecipeType<RecipePedestalCrafting>> PEDESTAL =
            RECIPE_TYPES.register(RecipePedestalCrafting.NAME, () -> RecipeType.simple(RecipePedestalCrafting.ID));
}
