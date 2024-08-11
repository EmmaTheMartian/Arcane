package martian.arcane.integration.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import martian.arcane.ArcaneMod;
import martian.arcane.common.ArcaneContent;
import martian.arcane.common.item.ItemWand;
import martian.arcane.common.recipe.SpellRecipe;
import martian.arcane.common.recipe.SpellRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Blocks;

import java.util.Arrays;
import java.util.function.Function;

@EmiEntrypoint
public class ArcaneEmiPlugin implements EmiPlugin {
    public static final EmiStack STACK_SPELL_HAMMERING = EmiStack.of(ItemWand.wandOfSpell(ArcaneContent.SPELL_HAMMERING.getId()));
    public static final EmiStack STACK_SPELL_CLEANSING = EmiStack.of(ItemWand.wandOfSpell(ArcaneContent.SPELL_CLEANSING.getId()));
    public static final EmiStack STACK_SPELL_PURIFYING = EmiStack.of(ItemWand.wandOfSpell(ArcaneContent.SPELL_PURIFYING.getId()));
    public static final EmiStack STACK_SPELL_FREEZING = EmiStack.of(ItemWand.wandOfSpell(ArcaneContent.SPELL_FREEZING.getId()));
    public static final EmiStack STACK_SPELL_MIXING = EmiStack.of(ItemWand.wandOfSpell(ArcaneContent.SPELL_MIXING.getId()));
    public static final EmiStack STACK_AURA_INFUSER = EmiStack.of(ArcaneContent.BE_AURA_INFUSER.block());
    public static final EmiStack STACK_PEDESTAL = EmiStack.of(ArcaneContent.BE_PEDESTAL.block());
    public static final EmiStack STACK_CAULDRON = EmiStack.of(Blocks.CAULDRON);

    public static final EmiRecipeCategory CATEGORY_SPELL_HAMMERING = new EmiRecipeCategory(ArcaneMod.id("spell_hammering"), STACK_SPELL_HAMMERING);
    public static final EmiRecipeCategory CATEGORY_SPELL_CLEANSING = new EmiRecipeCategory(ArcaneMod.id("spell_cleansing"), STACK_SPELL_CLEANSING);
    public static final EmiRecipeCategory CATEGORY_SPELL_PURIFYING = new EmiRecipeCategory(ArcaneMod.id("spell_purifying"), STACK_SPELL_PURIFYING);
    public static final EmiRecipeCategory CATEGORY_SPELL_FREEZING = new EmiRecipeCategory(ArcaneMod.id("spell_freezing"), STACK_SPELL_FREEZING);
    public static final EmiRecipeCategory CATEGORY_AURA_INFUSION = new EmiRecipeCategory(ArcaneMod.id("aura_infusion"), STACK_AURA_INFUSER);
    public static final EmiRecipeCategory CATEGORY_MIXING = new EmiRecipeCategory(ArcaneMod.id("spell_mixing"), STACK_SPELL_MIXING);
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
        addSpellCategory(CATEGORY_SPELL_FREEZING, STACK_SPELL_FREEZING, ArcaneContent.RT_FREEZING, ArcaneContent.SPELL_FREEZING.getId());

        addCategory(CATEGORY_AURA_INFUSION, STACK_AURA_INFUSER);
        addRecipesFor(ArcaneContent.RT_AURA_INFUSION, AuraInfusionEmiRecipe::new);

        addCategory(CATEGORY_PEDESTAL, STACK_PEDESTAL);
        addRecipesFor(ArcaneContent.RT_PEDESTAL, PedestalEmiRecipe::new);

        addCategory(CATEGORY_MIXING, STACK_CAULDRON, STACK_SPELL_MIXING);
        addRecipesFor(ArcaneContent.RT_CAULDRON_MIXING, CauldronMixingEmiRecipe::new);
    }

    private void addCategory(EmiRecipeCategory category, EmiStack... workstations) {
        r.addCategory(category);
        Arrays.stream(workstations).forEach(it -> r.addWorkstation(category, it));
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
