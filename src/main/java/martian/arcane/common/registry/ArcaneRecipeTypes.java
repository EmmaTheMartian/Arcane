package martian.arcane.common.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.api.recipe.ArcaneRecipeType;
import martian.arcane.common.recipe.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unused")
public class ArcaneRecipeTypes extends ArcaneRegistry {
    public ArcaneRecipeTypes() { super(TYPES, SERIALIZERS); }

    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, ArcaneMod.MODID);
    private static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, ArcaneMod.MODID);

    public static final AuraInfusionType AURA_INFUSION = create("aura_infusion", RecipeAuraInfusion.TYPE);
    public static final SpellRecipeType HAMMERING = create("hammering", new SpellRecipeType());
    public static final SpellRecipeType CLEANSING = create("cleansing", new SpellRecipeType());
    public static final SpellRecipeType PURIFYING = create("purifying", new SpellRecipeType());
    public static final RecipePedestalType PEDESTAL = create("pedestal", RecipePedestalCrafting.TYPE);

    private static <T extends ArcaneRecipeType<?>> T create(String name, T it) {
        SERIALIZERS.register(name, () -> it);
        TYPES.register(name, () -> it);
        return it;
    }
}
