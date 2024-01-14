package martian.arcane.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.recipe.aurainfuser.RecipeAuraInfusion;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")
public class ArcaneRecipeTypes extends ArcaneRegistry {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ArcaneMod.MODID);

    public enum Types {
        AURA_INFUSION(RecipeAuraInfusion.NAME, RecipeAuraInfusion.TYPE);

        Types(String name, RecipeType<?> type) {
            RECIPE_TYPES.register(name, () -> type);
        }
    }
}
