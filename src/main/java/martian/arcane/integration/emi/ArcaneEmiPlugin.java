package martian.arcane.integration.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import martian.arcane.ArcaneMod;
import martian.arcane.common.item.wand.ItemAuraWand;
import martian.arcane.common.recipe.SpellRecipe;
import martian.arcane.common.recipe.SpellRecipeType;
import martian.arcane.common.registry.ArcaneBlocks;
import martian.arcane.common.registry.ArcaneRecipeTypes;
import martian.arcane.common.registry.ArcaneSpells;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Function;

@EmiEntrypoint
public class ArcaneEmiPlugin implements EmiPlugin {
    public static final EmiStack STACK_AURA_INFUSER = EmiStack.of(ArcaneBlocks.AURA_INFUSER);
    public static final EmiStack STACK_SPELL_HAMMERING = EmiStack.of(ItemAuraWand.oakWandOfSpell(ArcaneSpells.HAMMERING.getId()));
    public static final EmiStack STACK_SPELL_CLEANSING = EmiStack.of(ItemAuraWand.oakWandOfSpell(ArcaneSpells.CLEANSING.getId()));
    public static final EmiStack STACK_SPELL_PURIFYING = EmiStack.of(ItemAuraWand.oakWandOfSpell(ArcaneSpells.PURIFYING.getId()));
    public static final EmiStack STACK_PEDESTAL = EmiStack.of(ArcaneBlocks.PEDESTAL);

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

        addSpellCategory(CATEGORY_SPELL_HAMMERING, STACK_SPELL_HAMMERING, ArcaneRecipeTypes.HAMMERING, ArcaneSpells.HAMMERING.getId());
        addSpellCategory(CATEGORY_SPELL_CLEANSING, STACK_SPELL_CLEANSING, ArcaneRecipeTypes.CLEANSING, ArcaneSpells.CLEANSING.getId());
        addSpellCategory(CATEGORY_SPELL_PURIFYING, STACK_SPELL_PURIFYING, ArcaneRecipeTypes.PURIFYING, ArcaneSpells.PURIFYING.getId());

        addCategory(CATEGORY_AURA_INFUSION, STACK_AURA_INFUSER);
        addCategory(CATEGORY_PEDESTAL, STACK_PEDESTAL);

        addRecipesFor(ArcaneRecipeTypes.AURA_INFUSION, AuraInfusionEmiRecipe::new);
        addRecipesFor(ArcaneRecipeTypes.PEDESTAL, PedestalEmiRecipe::new);
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
