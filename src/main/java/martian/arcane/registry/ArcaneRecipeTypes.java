package martian.arcane.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.recipe.aurainfuser.RecipeAuraInfusion;
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
}