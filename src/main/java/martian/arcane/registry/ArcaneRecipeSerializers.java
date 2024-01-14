package martian.arcane.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.recipe.aurainfuser.RecipeAuraInfusion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class ArcaneRecipeSerializers extends ArcaneRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ArcaneMod.MODID);

    public static final RegistryObject<?> AURA_INFUSION = SERIALIZERS.register(RecipeAuraInfusion.NAME, RecipeAuraInfusion.Serializer::new);
}
