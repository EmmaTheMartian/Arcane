package martian.arcane.datagen.server;

import martian.arcane.ArcaneMod;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.common.ArcaneContent;
import martian.arcane.datagen.builders.CauldronMixingBuilder;
import martian.arcane.datagen.builders.PedestalRecipeBuilder;
import martian.arcane.datagen.builders.SpellRecipeBuilder;
import martian.arcane.datagen.util.BetterRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static martian.arcane.common.ArcaneContent.*;

public class ArcaneRecipeProvider extends BetterRecipeProvider {
    public ArcaneRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    private final TagKey<Item> TAG_COPPER = Tags.Items.INGOTS_COPPER;

    protected void build() {
        helper.prefixesByProvider.put(SpellRecipeBuilder.class, "spells");
        helper.prefixesByProvider.put(PedestalRecipeBuilder.class, "pedestal");
        helper.prefixesByProvider.put(CauldronMixingBuilder.class, "cauldron_mixing");
        helper.prefixesByProvider.put(ShapelessRecipeBuilder.class, "shapeless");
        helper.prefixesByProvider.put(ShapedRecipeBuilder.class, "shaped");
        helper.prefixesByProvider.put(SimpleCookingRecipeBuilder.class, "cooking");

        buildCraftingMaterialRecipes();
        buildMachineRecipes();
        buildToolRecipes();
        buildWandRecipes();
        buildOreProcessingRecipes();
        buildSpellTabletRecipes();

        buildHammeringRecipes();
        buildCleansingRecipes();
        buildFreezingRecipes();
        buildMixingRecipes();
    }


    private void buildCraftingMaterialRecipes() {
        helper.defaultCategory = RecipeCategory.MISC;

        shapeless(ITEM_RAW_AURACHALCUM, 2, Map.of(
                ingOf(ITEMS_IDOCRASE.polished()), 1,
                ingOf(Tags.Items.OBSIDIANS), 2
        )).unlockedWith(ITEMS_IDOCRASE.polished()).save();

        shaped(ITEM_COPPER_CORE)
                .pattern(" G ", "GCG", " G ")
                .define('G', Tags.Items.GLASS_BLOCKS_COLORLESS)
                .define('C', TAG_COPPER)
                .unlockedWith(Items.COPPER_INGOT)
                .save();

        shaped(ITEM_SPELL_CIRCLE_CORE)
                .pattern(" A ", "ACA", " A ")
                .define('A', ITEM_AURAGLASS_DUST)
                .define('C', ITEM_AURACHALCUM_CORE)
                .unlockedWith(ITEM_AURACHALCUM_CORE)
                .save();

        PedestalRecipeBuilder.simpleRecipe(ingOf(TAG_COPPER), ingOf(BLOCK_AURAGLASS), ITEM_COPPER_CORE, 1).unlockedWith(Items.COPPER_INGOT).save(helper);
        PedestalRecipeBuilder.simpleRecipe(ingOf(ITEMS_LARIMAR.polished()), ingOf(BLOCK_AURAGLASS), ITEM_LARIMAR_CORE, 1).unlockedWith(ITEMS_LARIMAR.polished()).save(helper);
        PedestalRecipeBuilder.simpleRecipe(ingOf(ITEM_AURACHALCUM), ingOf(BLOCK_AURAGLASS), ITEM_AURACHALCUM_CORE, 1).unlockedWith(ITEM_AURACHALCUM).save(helper);
        PedestalRecipeBuilder.simpleRecipe(ingOf(ITEM_ELDRITCH_ALLOY), ingOf(BLOCK_AURAGLASS), ITEM_ELDRITCH_CORE, 1).unlockedWith(ITEM_ELDRITCH_ALLOY).save(helper);

        gemLine(ITEMS_LARIMAR);
        gemLine(ITEMS_FADED_LARIMAR);
        gemLine(ITEMS_IDOCRASE);

        storageBlock(ITEMS_FADED_LARIMAR.polished(), BLOCK_FADED_LARIMAR);
        storageBlock(ITEMS_LARIMAR.polished(), BLOCK_LARIMAR);
        storageBlock(ITEM_AURACHALCUM, BLOCK_AURACHALCUM);
    }

    private void buildMachineRecipes() {
        helper.defaultCategory = RecipeCategory.REDSTONE;

        final Ingredient ING_DEEPSLATE = ingOf(Blocks.DEEPSLATE_BRICKS);

        shaped(BE_PEDESTAL.block())
                .pattern("SSS", " S ", "CBC")
                .define('C', TAG_COPPER)
                .define('S', ING_DEEPSLATE)
                .define('B', ITEM_COPPER_CORE)
                .unlockedWith(ITEM_COPPER_CORE)
                .save();

        shaped(BE_AURA_INFUSER.block())
                .pattern("BCB", " O ", "OOO")
                .define('B', TAG_COPPER)
                .define('C', ITEM_COPPER_CORE)
                .define('O', ING_DEEPSLATE)
                .unlockedWith(ITEM_COPPER_CORE)
                .save();

        shaped(BE_AURA_BASIN.block())
                .pattern("B B", "B B", "BCB")
                .define('B', ING_DEEPSLATE)
                .define('C', ITEM_COPPER_CORE)
                .unlockedWith(ITEM_COPPER_CORE)
                .save();

        shaped(BE_AURA_CONNECTOR.block())
                .pattern(" A ", "DDD")
                .define('A', ITEM_COPPER_CORE)
                .define('D', ING_DEEPSLATE)
                .unlockedWith(ITEM_COPPER_CORE)
                .save();

        shaped(BE_HEAT_COLLECTOR.block())
                .pattern("GIG", "FCF", "GIG")
                .define('G', Tags.Items.GLASS_BLOCKS_COLORLESS)
                .define('I', TAG_COPPER)
                .define('F', Items.FLINT)
                .define('C', ITEM_COPPER_CORE)
                .unlockedWith(ITEM_COPPER_CORE)
                .save();

        shaped(BE_AQUA_COLLECTOR.block())
                .pattern("GIG", "ICI", "GWG")
                .define('G', Tags.Items.GLASS_BLOCKS_COLORLESS)
                .define('I', TAG_COPPER)
                .define('W', Items.WATER_BUCKET)
                .define('C', ITEM_COPPER_CORE)
                .unlockedWith(ITEM_COPPER_CORE)
                .save();
    }

    private void buildToolRecipes() {
        helper.defaultCategory = RecipeCategory.TOOLS;

        shaped(ITEM_AURA_WRENCH)
                .pattern("C C", " B ", " C ")
                .define('C', TAG_COPPER)
                .define('B', ITEM_COPPER_CORE)
                .unlockedWith(ITEM_COPPER_CORE)
                .save();

        shaped(ITEM_AURA_CONFIGURATOR)
                .pattern("B B", " I ", " B ")
                .define('B', Tags.Items.INGOTS_IRON)
                .define('I', ITEM_COPPER_CORE)
                .unlockedWith(ITEM_COPPER_CORE)
                .save();

        shaped(ITEM_GEM_SAW)
                .pattern("  B", " BS", "BS ")
                .define('B', TAG_COPPER)
                .define('S', Tags.Items.NUGGETS_IRON)
                .unlockedWith(Items.COPPER_INGOT)
                .save();

        shaped(ITEM_AURAOMETER)
                .pattern("R", "C", "B")
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('B', ITEM_COPPER_CORE)
                .define('C', TAG_COPPER)
                .unlockedWith(ITEM_COPPER_CORE)
                .save();

        shapeless(ITEM_ARCANE_BLEACH, Map.of(
                ingOf(Items.GLASS_BOTTLE), 1,
                ingOf(Items.BONE_MEAL), 1,
                ingOf(ITEM_AURAGLASS_DUST), 1
        )).unlockedWith(ITEM_AURAGLASS_DUST).save();

        shaped(ITEM_SPELL_CHALK)
                .pattern(" CB", "CSC", "BC ")
                .define('C', Items.CLAY_BALL)
                .define('B', Items.BONE_MEAL)
                .define('S', ITEM_SPELL_CIRCLE_CORE)
                .unlockedWith(ITEM_SPELL_CIRCLE_CORE)
                .save();

        shaped(ITEM_ENDERPACK)
                .pattern(" P ")
                .pattern("LEL")
                .pattern("PPP")
                .define('P', ITEMS_LARIMAR.polished())
                .define('L', Tags.Items.LEATHERS)
                .define('E', Items.ENDER_CHEST)
                .unlockedWith(ITEMS_LARIMAR.polished())
                .save();

        shaped(ITEM_AURAGLASS_BOTTLE)
                .pattern("A A", " A ")
                .define('A', BLOCK_AURAGLASS)
                .unlockedWith(BLOCK_AURAGLASS)
                .save();

        shaped(ITEM_MEDIUM_AURAGLASS_BOTTLE)
                .pattern("BAB", " B ")
                .define('A', ITEM_AURAGLASS_BOTTLE)
                .define('B', ITEMS_LARIMAR.polished())
                .unlockedWith(ITEM_AURAGLASS_BOTTLE)
                .save();

        shaped(ITEM_LARGE_AURAGLASS_BOTTLE)
                .pattern("BAB", " B ")
                .define('A', ITEM_MEDIUM_AURAGLASS_BOTTLE)
                .define('B', ITEM_AURACHALCUM)
                .unlockedWith(ITEM_MEDIUM_AURAGLASS_BOTTLE)
                .save();

        shaped(ITEM_EXTREME_AURAGLASS_BOTTLE)
                .pattern("BAB", " B ")
                .define('A', ITEM_LARGE_AURAGLASS_BOTTLE)
                .define('B', ITEM_ELDRITCH_ALLOY)
                .unlockedWith(ITEM_LARGE_AURAGLASS_BOTTLE)
                .save();

        shaped(ITEM_UPGRADE_KIT_LARIMAR)
                .pattern("CLC", "LIL", "CLC")
                .define('C', TAG_COPPER)
                .define('L', ITEMS_LARIMAR.polished())
                .define('I', Tags.Items.GEMS_AMETHYST)
                .unlockedWith(ITEMS_LARIMAR.polished())
                .save();

        shaped(ITEM_UPGRADE_KIT_AURACHALCUM)
                .pattern("LAL", "AIA", "LAL")
                .define('A', ITEM_AURACHALCUM)
                .define('L', ITEMS_LARIMAR.polished())
                .define('I', Tags.Items.GEMS_DIAMOND)
                .unlockedWith(ITEMS_LARIMAR.polished())
                .save();

        shaped(ITEM_AURA_MULTITOOL)
                .pattern("W", "H", "A")
                .define('W', ITEM_AURA_WRENCH)
                .define('H', ITEM_AURA_CONFIGURATOR)
                .define('A', ITEM_AURAOMETER)
                .unlockedWith(ITEM_AURAOMETER)
                .save();
    }

    private void buildWandRecipes() {
        helper.defaultCategory = RecipeCategory.TOOLS;

        shaped(ITEM_WANDBOOK)
                .pattern(" F ", "ABA", " A ")
                .define('F', ITEM_AURACHALCUM_CORE)
                .define('A', ITEM_AURACHALCUM)
                .define('B', Items.BOOK)
                .unlockedWith(ITEM_AURACHALCUM_CORE)
                .save(itemKey(ITEM_WANDBOOK).withPrefix("wands/"));
    }

    private void buildOreProcessingRecipes() {
        helper.defaultCategory = RecipeCategory.MISC;
        helper.moveNamespacesToModid = false;
        oreProcessingLine(Items.RAW_COPPER, ITEM_CRUSHED_RAW_COPPER, ITEM_PURIFIED_RAW_COPPER, Items.COPPER_INGOT);
        oreProcessingLine(Items.RAW_IRON, ITEM_CRUSHED_RAW_IRON, ITEM_PURIFIED_RAW_IRON, Items.IRON_INGOT);
        oreProcessingLine(Items.RAW_GOLD, ITEM_CRUSHED_RAW_GOLD, ITEM_PURIFIED_RAW_GOLD, Items.GOLD_INGOT);
        helper.moveNamespacesToModid = true;
    }

    private void buildSpellTabletRecipes() {
        helper.defaultCategory = RecipeCategory.TOOLS;

        PedestalRecipeBuilder.simpleRecipe(ingOf(Items.CLAY_BALL), ingOf(Items.PAPER), ITEM_SPELL_TABLET, 1).unlockedWith(BE_PEDESTAL.block()).save(helper);

        Map<DeferredHolder<AbstractSpell, ?>, Ingredient> spellItems = new HashMap<>();
        spellItems.put(SPELL_BREAKING, ingOf(Items.DIAMOND_PICKAXE));
        spellItems.put(SPELL_HAMMERING, ingOf(Items.IRON_BLOCK));
        spellItems.put(SPELL_PURIFYING, ingOf(Items.EMERALD));
        spellItems.put(SPELL_CLEANSING, ingOf(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.WATER)));
        spellItems.put(SPELL_BUILDING, ingOf(Items.BRICKS));
        spellItems.put(SPELL_DASHING, ingOf(Items.FEATHER));
        spellItems.put(SPELL_CRAFTING, ingOf(Items.CRAFTING_TABLE));
        spellItems.put(SPELL_ACTIVATOR, ingOf(ITEM_SPELL_CIRCLE_CORE));
        spellItems.put(SPELL_CONJURE_WATER, ingOf(Items.WATER_BUCKET));
        spellItems.put(SPELL_FREEZING, ingOf(Items.ICE));
        spellItems.put(SPELL_LIGHTING, ingOf(Items.TORCH));
        spellItems.put(SPELL_MIXING, ingOf(Items.STICK));

        spellItems.forEach((spellHolder, item) -> {
            var stack = ITEM_SPELL_TABLET.get().getDefaultInstance();
            stack.set(ArcaneContent.DC_SPELL, spellHolder.getId());
            var holder = new martian.arcane.api.recipe.RecipeOutput.DataGenHolder(stack, 1);
            PedestalRecipeBuilder
                    .simpleRecipe(ingOf(ITEM_SPELL_TABLET.get()), item, holder.toRecipeOutput())
                    .unlockedWith(item.getItems()[0].getItem())
                    .save(helper, spellHolder.getId().withPrefix("spell_tablets/"));
        });
    }

    private void buildHammeringRecipes() {
        helper.defaultCategory = RecipeCategory.MISC;

        SpellRecipeBuilder.hammering()
                .setInput(BLOCK_AURAGLASS)
                .addResult(ITEM_AURAGLASS_SHARD)
                .addResult(ITEM_AURAGLASS_SHARD, 1, 0.5F)
                .unlockedWith(BLOCK_AURAGLASS)
                .save(helper);

        SpellRecipeBuilder.hammering()
                .setInput(ITEM_AURAGLASS_SHARD)
                .addResult(ITEM_AURAGLASS_DUST)
                .addResult(ITEM_AURAGLASS_DUST, 1, 0.5F)
                .unlockedWith(ITEM_AURAGLASS_SHARD)
                .save(helper);

        SpellRecipeBuilder.hammering()
                .setInput(Items.DEEPSLATE)
                .addResult(Items.COAL, 1, 0.2f)
                .addResult(Items.IRON_NUGGET, 2, 0.1f)
                .addResult(Items.GOLD_NUGGET, 1, 0.05f)
                .addResult(Items.REDSTONE, 2, 0.03f)
                .addResult(Items.DIAMOND, 1, 0.01f)
                .unlockedWith(BE_PEDESTAL.block())
                .save(helper);

        SpellRecipeBuilder.hammering()
                .setInput(Tags.Items.STONES)
                .addResult(Items.COBBLESTONE)
                .unlockedWith(Items.STONE)
                .save(helper);

        SpellRecipeBuilder.hammering()
                .setInput(Items.COBBLESTONE)
                .addResult(Items.GRAVEL)
                .unlockedWith(Items.COBBLESTONE)
                .save(helper);

        SpellRecipeBuilder.hammering()
                .setInput(Items.GRAVEL)
                .addResult(Items.SAND)
                .unlockedWith(Items.GRAVEL)
                .save(helper);
    }

    private void buildCleansingRecipes() {
        helper.defaultCategory = RecipeCategory.MISC;

        SpellRecipeBuilder.cleansing()
                .setInput(Items.GRAVEL)
                .addResult(Items.FLINT)
                .unlockedWith(Items.GRAVEL)
                .save(helper);
    }

    private void buildFreezingRecipes() {
        helper.defaultCategory = RecipeCategory.MISC;

        SpellRecipeBuilder.freezing()
                .setInput(Blocks.WATER_CAULDRON)
                .addResult(Blocks.POWDER_SNOW_CAULDRON)
                .unlockedWith(Items.CAULDRON)
                .save(helper);

        SpellRecipeBuilder.freezing()
                .setInput(Blocks.MAGMA_BLOCK)
                .addResult(Blocks.OBSIDIAN)
                .unlockedWith(Items.MAGMA_BLOCK)
                .save(helper);

        SpellRecipeBuilder.freezing()
                .setInput(Blocks.CRYING_OBSIDIAN)
                .addResult(BLOCK_FROZEN_OBSIDIAN)
                .unlockedWith(Items.CRYING_OBSIDIAN)
                .save(helper);
    }

    private void buildMixingRecipes() {
        helper.defaultCategory = RecipeCategory.MISC;
    }


    private void storageBlock(Ingredient of, ItemLike output) {
        shaped(output)
                .pattern("AAA", "AAA", "AAA")
                .define('A', of)
                .unlockedWith(of.getItems()[0].getItem())
                .save();
    }

    private void storageBlock(ItemLike of, ItemLike output) {
        storageBlock(Ingredient.of(of), output);
    }

    private void gemLine(DeferredGemItems gems) {
        CauldronMixingBuilder.cauldronMixing()
                .addInput(gems.rough())
                .addInput(Items.GRAVEL)
                .addInput(Items.GRAVEL)
                .setCauldron(Blocks.WATER_CAULDRON)
                .setFluidAmount(1)
                .addResult(gems.smooth())
                .unlockedWith(gems.rough())
                .save(helper);

        CauldronMixingBuilder.cauldronMixing()
                .addInput(gems.smooth())
                .addInput(Items.SAND)
                .addInput(Items.SAND)
                .setCauldron(Blocks.WATER_CAULDRON)
                .setFluidAmount(1)
                .addResult(gems.sandyPolished())
                .unlockedWith(gems.smooth())
                .save(helper);

        SpellRecipeBuilder.cleansing()
                .setInput(gems.sandyPolished())
                .addResult(gems.polished())
                .unlockedWith(gems.sandyPolished())
                .save(helper);

        //TODO: Exquisite
    }

    private void oreProcessingLine(ItemLike raw, ItemLike crushed, ItemLike purified, ItemLike ingot) {
        SpellRecipeBuilder.hammering()
                .setInput(raw)
                .addResult(crushed)
                .unlockedWith(raw)
                .save(helper, new ResourceLocation(ArcaneMod.MODID, "ore_processing/" + itemKey(raw).getPath()));

        SpellRecipeBuilder.purifying()
                .setInput(crushed)
                .addResult(purified, 2)
                .unlockedWith(crushed)
                .save(helper, itemKey(crushed).withPrefix("ore_processing/"));

        wrap(SimpleCookingRecipeBuilder.blasting(ingOf(purified), RecipeCategory.MISC, ingot, 0.7f, 100))
                .unlockedWith(raw)
                .save(itemKey(purified).withPrefix("ore_processing/"));
    }

    private ResourceLocation itemKey(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem());
    }

    private Ingredient ingOf(ItemLike... item) {
        return Ingredient.of(item);
    }

    private Ingredient ingOf(ItemStack... item) {
        return Ingredient.of(item);
    }

    private Ingredient ingOf(TagKey<Item> item) {
        return Ingredient.of(item);
    }
}
