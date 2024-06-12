package martian.arcane.integration.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import martian.arcane.ArcaneMod;
import martian.arcane.common.ArcaneContent;
import martian.arcane.common.item.ItemAuraWand;
import martian.arcane.common.recipe.SpellRecipe;
import martian.arcane.common.recipe.SpellRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Function;

@EmiEntrypoint
public class ArcaneEmiPlugin implements EmiPlugin {
    public static final EmiStack STACK_AURA_INFUSER = EmiStack.of(ArcaneContent.AURA_INFUSER.block());
    public static final EmiStack STACK_SPELL_HAMMERING = EmiStack.of(ItemAuraWand.oakWandOfSpell(ArcaneContent.SPELL_HAMMERING.getId()));
    public static final EmiStack STACK_SPELL_CLEANSING = EmiStack.of(ItemAuraWand.oakWandOfSpell(ArcaneContent.SPELL_CLEANSING.getId()));
    public static final EmiStack STACK_SPELL_PURIFYING = EmiStack.of(ItemAuraWand.oakWandOfSpell(ArcaneContent.SPELL_PURIFYING.getId()));
    public static final EmiStack STACK_PEDESTAL = EmiStack.of(ArcaneContent.PEDESTAL.block());

    public static final EmiRecipeCategory CATEGORY_AURA_INFUSION = new EmiRecipeCategory(ArcaneMod.id("aura_infusion"), STACK_AURA_INFUSER);
    public static final EmiRecipeCategory CATEGORY_SPELL_HAMMERING = new EmiRecipeCategory(ArcaneMod.id("spell_hammering"), STACK_SPELL_HAMMERING);
    public static final EmiRecipeCategory CATEGORY_SPELL_CLEANSING = new EmiRecipeCategory(ArcaneMod.id("spell_cleansing"), STACK_SPELL_CLEANSING);
    public static final EmiRecipeCategory CATEGORY_SPELL_PURIFYING = new EmiRecipeCategory(ArcaneMod.id("spell_purifying"), STACK_SPELL_PURIFYING);
    public static final EmiRecipeCategory CATEGORY_PEDESTAL = new EmiRecipeCategory(ArcaneMod.id("pedestal"), STACK_PEDESTAL);

    private EmiRegistry r;
    private RecipeManager rm;

    @Override
    public void register(EmiRegistry registry) {
        this.r = registry;
        this.rm = registry.getRecipeManager();

        addSpellCategory(CATEGORY_SPELL_HAMMERING, STACK_SPELL_HAMMERING, ArcaneContent.RT_HAMMERING, ArcaneContent.SPELL_HAMMERING.getId());
        addSpellCategory(CATEGORY_SPELL_CLEANSING, STACK_SPELL_CLEANSING, ArcaneContent.RT_CLEANSING, ArcaneContent.SPELL_CLEANSING.getId());
        addSpellCategory(CATEGORY_SPELL_PURIFYING, STACK_SPELL_PURIFYING, ArcaneContent.RT_PURIFYING, ArcaneContent.SPELL_PURIFYING.getId());

        addCategory(CATEGORY_AURA_INFUSION, STACK_AURA_INFUSER);
        addCategory(CATEGORY_PEDESTAL, STACK_PEDESTAL);

        addRecipesFor(ArcaneContent.RT_AURA_INFUSION, AuraInfusionEmiRecipe::new);
        addRecipesFor(ArcaneContent.RT_PEDESTAL, PedestalEmiRecipe::new);
    }

    private void addCategory(EmiRecipeCategory category, EmiStack stack) {
        r.addCategory(category);
        r.addWorkstation(category, stack);
    }

    private void addSpellCategory(EmiRecipeCategory category, EmiStack stack, SpellRecipeType type, ResourceLocation spellId) {
        addCategory(category, stack);
        r.addWorkstation(category, STACK_PEDESTAL);

        for (RecipeHolder<SpellRecipe> recipe : rm.getAllRecipesFor(type)) {
            r.addRecipe(new SpellEmiRecipe(category, spellId, recipe));
        }
    }

    private <C extends Container, T extends Recipe<C>> void addRecipesFor(RecipeType<T> type, Function<RecipeHolder<T>, EmiRecipe> func) {
        rm.getAllRecipesFor(type).forEach(it -> r.addRecipe(func.apply(it)));
    }
}
