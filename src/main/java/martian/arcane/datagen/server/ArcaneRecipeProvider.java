package martian.arcane.datagen.server;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneTags;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.common.ArcaneContent;
import martian.arcane.datagen.builders.PedestalRecipeBuilder;
import martian.arcane.datagen.builders.SpellRecipeBuilder;
import martian.arcane.datagen.util.BetterRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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

    protected void build() {
        helper.prefixesByProvider.put(SpellRecipeBuilder.class, "spells");
        helper.prefixesByProvider.put(PedestalRecipeBuilder.class, "pedestal");
        helper.prefixesByProvider.put(ShapelessRecipeBuilder.class, "shapeless");
        helper.prefixesByProvider.put(ShapedRecipeBuilder.class, "shaped");
        helper.prefixesByProvider.put(SimpleCookingRecipeBuilder.class, "cooking");

        final TagKey<Item>
                COPPER      = Tags.Items.INGOTS_COPPER,
                IRON        = Tags.Items.INGOTS_IRON,
                REDSTONE    = Tags.Items.DUSTS_REDSTONE,
                GLASS       = Tags.Items.GLASS_BLOCKS_COLORLESS,
                STONE       = Tags.Items.STONES,
                OBSIDIAN    = Tags.Items.OBSIDIANS
        ;

        final Ingredient
                DEEPSLATE               = ingOf(Blocks.DEEPSLATE_BRICKS),
                ING_AURAGLASS           = ingOf(AURAGLASS),
                ING_AURAGLASS_DUST      = ingOf(AURAGLASS_DUST),
                ING_COPPER_CORE         = ingOf(COPPER_CORE),
                ING_LARIMAR_CORE        = ingOf(LARIMAR_CORE),
                ING_AURACHALCUM_CORE    = ingOf(AURACHALCUM_CORE),
                ING_ELDRITCH_CORE       = ingOf(ELDRITCH_CORE),
                ING_SPELL_CIRCLE_CORE   = ingOf(SPELL_CIRCLE_CORE),
                ING_POLISHED_LARIMAR    = ingOf(POLISHED_LARIMAR),
                ING_FADED_LARIMAR       = ingOf(FADED_POLISHED_LARIMAR),
                ING_AURACHALCUM         = ingOf(AURACHALCUM),
                ING_ELDRITCH_ALLOY      = ingOf(ELDRITCH_ALLOY)
        ;

        // Materials
        {
            shapeless(RAW_AURACHALCUM, 2, Map.of(
                    ingOf(POLISHED_IDOCRASE), 1,
                    ingOf(OBSIDIAN), 2
            )).unlockedWith(POLISHED_IDOCRASE).save();

            shaped(COPPER_CORE)
                    .pattern(" G ", "GCG", " G ")
                    .define('G', GLASS)
                    .define('C', COPPER)
                    .unlockedWith(Items.COPPER_INGOT)
                    .save();

            shaped(SPELL_CIRCLE_CORE)
                    .pattern(" A ", "ACA", " A ")
                    .define('A', ING_AURAGLASS_DUST)
                    .define('C', ING_AURACHALCUM_CORE)
                    .unlockedWith(AURACHALCUM_CORE)
                    .save();

            // Hammering Recipes
            SpellRecipeBuilder.hammering()
                    .setInput(ING_AURAGLASS)
                    .addResult(AURAGLASS_SHARD)
                    .addResult(AURAGLASS_SHARD, 1, 0.5F)
                    .unlockedWith(AURAGLASS)
                    .save(helper);

            SpellRecipeBuilder.hammering()
                    .setInput(AURAGLASS_SHARD)
                    .addResult(AURAGLASS_DUST)
                    .addResult(AURAGLASS_DUST, 1, 0.5F)
                    .unlockedWith(AURAGLASS_SHARD)
                    .save(helper);

            SpellRecipeBuilder.hammering()
                    .setInput(Items.DEEPSLATE)
                    .addResult(Items.COAL, 1, 0.2f)
                    .addResult(Items.IRON_NUGGET, 2, 0.1f)
                    .addResult(Items.GOLD_NUGGET, 1, 0.05f)
                    .addResult(Items.REDSTONE, 2, 0.03f)
                    .addResult(Items.DIAMOND, 1, 0.01f)
                    .unlockedWith(PEDESTAL.block())
                    .save(helper);

            // Pedestal Interaction Recipes
            PedestalRecipeBuilder.simpleRecipe(ingOf(COPPER), ING_AURAGLASS, COPPER_CORE, 1).unlockedWith(Items.COPPER_INGOT).save(helper);
            PedestalRecipeBuilder.simpleRecipe(ingOf(POLISHED_LARIMAR), ING_AURAGLASS, LARIMAR_CORE, 1).unlockedWith(POLISHED_LARIMAR).save(helper);
            PedestalRecipeBuilder.simpleRecipe(ingOf(AURACHALCUM), ING_AURAGLASS, AURACHALCUM_CORE, 1).unlockedWith(AURACHALCUM).save(helper);
            PedestalRecipeBuilder.simpleRecipe(ingOf(ELDRITCH_ALLOY), ING_AURAGLASS, ELDRITCH_CORE, 1).unlockedWith(ELDRITCH_ALLOY).save(helper);

            // Gems
            cutAndPolish(RAW_LARIMAR, CUT_LARIMAR, POLISHED_LARIMAR);
            cutAndPolish(FADED_RAW_LARIMAR, FADED_CUT_LARIMAR, FADED_POLISHED_LARIMAR);
            cutAndPolish(RAW_IDOCRASE, CUT_IDOCRASE, POLISHED_IDOCRASE);

            // Storage Blocks
            storageBlock(ING_FADED_LARIMAR, FADED_LARIMAR_BLOCK);
            storageBlock(ING_POLISHED_LARIMAR, LARIMAR_BLOCK);
            storageBlock(ING_AURACHALCUM, AURACHALCUM_BLOCK);
        }

        // Misc Utility Recipes
        {
            SpellRecipeBuilder.hammering().setInput(STONE).addResult(Items.COBBLESTONE).unlockedWith(Items.STONE).save(helper);
            SpellRecipeBuilder.hammering().setInput(Items.COBBLESTONE).addResult(Items.GRAVEL).unlockedWith(Items.COBBLESTONE).save(helper);
            SpellRecipeBuilder.hammering().setInput(Items.GRAVEL).addResult(Items.SAND).unlockedWith(Items.GRAVEL).save(helper);
            SpellRecipeBuilder.cleansing().setInput(Items.GRAVEL).addResult(Items.FLINT).unlockedWith(Items.GRAVEL).save(helper);
        }

        // Blocks
        {
            helper.defaultCategory = RecipeCategory.REDSTONE;

            // Machines
            shaped(PEDESTAL.block())
                    .pattern("SSS", " S ", "CBC")
                    .define('C', COPPER)
                    .define('S', DEEPSLATE)
                    .define('B', ING_COPPER_CORE)
                    .unlockedWith(COPPER_CORE)
                    .save();

            shaped(AURA_INFUSER.block())
                    .pattern("BCB", " O ", "OOO")
                    .define('B', COPPER)
                    .define('C', ING_COPPER_CORE)
                    .define('O', DEEPSLATE)
                    .unlockedWith(COPPER_CORE)
                    .save();

            shaped(AURA_BASIN.block())
                    .pattern("B B", "B B", "BCB")
                    .define('B', DEEPSLATE)
                    .define('C', ING_COPPER_CORE)
                    .unlockedWith(COPPER_CORE)
                    .save();

            shaped(AURA_CONNECTOR.block())
                    .pattern(" A ", "DDD")
                    .define('A', ING_COPPER_CORE)
                    .define('D', DEEPSLATE)
                    .unlockedWith(COPPER_CORE)
                    .save();

            shaped(HEAT_COLLECTOR.block())
                    .pattern("GIG", "FCF", "GIG")
                    .define('G', GLASS)
                    .define('I', COPPER)
                    .define('F', ingOf(Items.FLINT))
                    .define('C', ING_COPPER_CORE)
                    .unlockedWith(COPPER_CORE)
                    .save();

            shaped(AQUA_COLLECTOR.block())
                    .pattern("GIG", "ICI", "GWG")
                    .define('G', GLASS)
                    .define('I', COPPER)
                    .define('W', ingOf(Items.WATER_BUCKET))
                    .define('C', ING_COPPER_CORE)
                    .unlockedWith(COPPER_CORE)
                    .save();
        }

        // Tools
        {
            helper.defaultCategory = RecipeCategory.TOOLS;

            shaped(AURA_WRENCH)
                    .pattern("C C", " B ", " C ")
                    .define('C', COPPER)
                    .define('B', ING_COPPER_CORE)
                    .unlockedWith(COPPER_CORE)
                    .save();

            shaped(AURA_CONFIGURATOR)
                    .pattern("B B", " I ", " B ")
                    .define('B', IRON)
                    .define('I', ING_COPPER_CORE)
                    .unlockedWith(COPPER_CORE)
                    .save();

            shaped(GEM_SAW)
                    .pattern("  B", " BS", "BS ")
                    .define('B', COPPER)
                    .define('S', Tags.Items.NUGGETS_IRON)
                    .unlockedWith(Items.COPPER_INGOT)
                    .save();

            shaped(AURAOMETER)
                    .pattern("R", "C", "B")
                    .define('R', REDSTONE)
                    .define('B', ING_COPPER_CORE)
                    .define('C', COPPER)
                    .unlockedWith(COPPER_CORE)
                    .save();

            shapeless(ARCANE_BLEACH, Map.of(
                    ingOf(Items.GLASS_BOTTLE), 1,
                    ingOf(Items.BONE_MEAL), 1,
                    ingOf(AURAGLASS_DUST), 1
            )).unlockedWith(AURAGLASS_DUST).save();

            shaped(SPELL_CHALK)
                    .pattern(" CB", "CSC", "BC ")
                    .define('C', ingOf(Items.CLAY_BALL))
                    .define('B', ingOf(Items.BONE_MEAL))
                    .define('S', ING_SPELL_CIRCLE_CORE)
                    .unlockedWith(SPELL_CIRCLE_CORE)
                    .save();

            shaped(ENDERPACK)
                    .pattern(" P ")
                    .pattern("LEL")
                    .pattern("PPP")
                    .define('P', ING_POLISHED_LARIMAR)
                    .define('L', Tags.Items.LEATHERS)
                    .define('E', ingOf(Items.ENDER_CHEST))
                    .unlockedWith(POLISHED_LARIMAR)
                    .save();

            // Auraglass Bottles
            {
                shaped(AURAGLASS_BOTTLE)
                        .pattern("A A", " A ")
                        .define('A', ING_AURAGLASS)
                        .unlockedWith(AURAGLASS)
                        .save();

                shaped(MEDIUM_AURAGLASS_BOTTLE)
                        .pattern("BAB", " B ")
                        .define('A', ingOf(AURAGLASS_BOTTLE))
                        .define('B', ING_POLISHED_LARIMAR)
                        .unlockedWith(AURAGLASS_BOTTLE)
                        .save();

                shaped(LARGE_AURAGLASS_BOTTLE)
                        .pattern("BAB", " B ")
                        .define('A', ingOf(MEDIUM_AURAGLASS_BOTTLE))
                        .define('B', ING_AURACHALCUM)
                        .unlockedWith(MEDIUM_AURAGLASS_BOTTLE)
                        .save();

                shaped(EXTREME_AURAGLASS_BOTTLE)
                        .pattern("BAB", " B ")
                        .define('A', ingOf(LARGE_AURAGLASS_BOTTLE))
                        .define('B', ING_ELDRITCH_ALLOY)
                        .unlockedWith(LARGE_AURAGLASS_BOTTLE)
                        .save();
            }

            // Wands
            {
                Map<Item, ItemLike> wands = new HashMap<>();
                wands.put(Items.ACACIA_PLANKS, WAND_ACACIA);
                wands.put(Items.BAMBOO_PLANKS, WAND_BAMBOO);
                wands.put(Items.BIRCH_PLANKS, WAND_BIRCH);
                wands.put(Items.CHERRY_PLANKS, WAND_CHERRY);
                wands.put(Items.DARK_OAK_PLANKS, WAND_DARK_OAK);
                wands.put(Items.JUNGLE_PLANKS, WAND_JUNGLE);
                wands.put(Items.MANGROVE_PLANKS, WAND_MANGROVE);
                wands.put(Items.OAK_PLANKS, WAND_OAK);
                wands.put(Items.SPRUCE_PLANKS, WAND_SPRUCE);
                wands.put(Items.WARPED_PLANKS, WAND_WARPED);
                wands.put(Items.CRIMSON_PLANKS, WAND_CRIMSON);
                wands.put(Items.COPPER_INGOT, WAND_COPPER);
                wands.forEach((wandItem, output) -> shaped(output)
                        .pattern("  P", " P ", "C  ")
                        .define('C', ING_COPPER_CORE)
                        .define('P', ingOf(wandItem))
                        .unlockedWith(COPPER_CORE)
                        .save(itemKey(output).withPrefix("wands/")));

                // Advanced Wands
                shaped(WAND_LARIMAR)
                        .pattern("  B", " W ", "C  ")
                        .define('B', ING_POLISHED_LARIMAR)
                        .define('C', ING_LARIMAR_CORE)
                        .define('W', ArcaneTags.BASIC_WANDS)
                        .unlockedWith(LARIMAR_CORE)
                        .save(itemKey(WAND_LARIMAR).withPrefix("wands/"));

                // Mythical Wands
                shaped(WAND_AURACHALCUM)
                        .pattern("  D", " W ", "C  ")
                        .define('D', ING_AURACHALCUM)
                        .define('W', ArcaneTags.ADVANCED_WANDS)
                        .define('C', ING_AURACHALCUM_CORE)
                        .unlockedWith(AURACHALCUM_CORE)
                        .save(itemKey(WAND_AURACHALCUM).withPrefix("wands/"));

                shaped(WAND_ELDRITCH)
                        .pattern("  D", " W ", "C  ")
                        .define('D', ING_ELDRITCH_ALLOY)
                        .define('W', ArcaneTags.ADVANCED_WANDS)
                        .define('C', ING_ELDRITCH_CORE)
                        .unlockedWith(ELDRITCH_CORE)
                        .save(itemKey(WAND_ELDRITCH).withPrefix("wands/"));
            }
        }

        // Ore Processing
        {
            helper.defaultCategory = RecipeCategory.MISC;
            helper.moveNamespacesToModid = false;
            oreProcessingLine(Items.RAW_COPPER, CRUSHED_RAW_COPPER, PURIFIED_RAW_COPPER, Items.COPPER_INGOT);
            oreProcessingLine(Items.RAW_IRON, CRUSHED_RAW_IRON, PURIFIED_RAW_IRON, Items.IRON_INGOT);
            oreProcessingLine(Items.RAW_GOLD, CRUSHED_RAW_GOLD, PURIFIED_RAW_GOLD, Items.GOLD_INGOT);
            helper.moveNamespacesToModid = true;
        }

        // Spell Tablets
        {
            helper.defaultCategory = RecipeCategory.TOOLS;

            PedestalRecipeBuilder.simpleRecipe(ingOf(Items.CLAY_BALL), ingOf(Items.PAPER), SPELL_TABLET, 1).unlockedWith(PEDESTAL.block()).save(helper);

            Map<DeferredHolder<AbstractSpell, ?>, ItemLike> spellItems = new HashMap<>();
            spellItems.put(SPELL_BREAKING, Items.DIAMOND_PICKAXE);
            spellItems.put(SPELL_HAMMERING, Items.IRON_BLOCK);
            spellItems.put(SPELL_PURIFYING, Items.EMERALD);
            spellItems.put(SPELL_CLEANSING, Items.WATER_BUCKET);
            spellItems.put(SPELL_BUILDING, Items.BRICKS);
            spellItems.put(SPELL_DASHING, Items.FEATHER);
            spellItems.put(SPELL_CRAFTING, Items.CRAFTING_TABLE);
            spellItems.put(SPELL_ACTIVATOR, SPELL_CIRCLE_CORE);
            spellItems.forEach((spellHolder, item) -> {
                var stack = SPELL_TABLET.get().getDefaultInstance();
                stack.set(ArcaneContent.DC_SPELL, spellHolder.getId());
                var holder = new martian.arcane.api.recipe.RecipeOutput.DataGenHolder(stack, 1);
                PedestalRecipeBuilder
                        .simpleRecipe(ingOf(SPELL_TABLET.get()), ingOf(item), holder.toRecipeOutput())
                        .unlockedWith(item)
                        .save(helper, spellHolder.getId().withPrefix("spell_tablets/"));
            });
        }
    }

    private void storageBlock(Ingredient of, ItemLike output) {
        shaped(output)
                .pattern("AAA", "AAA", "AAA")
                .define('A', of)
                .unlockedWith(of.getItems()[0].getItem())
                .save();
    }

    private void cutAndPolish(ItemLike raw, ItemLike cut, ItemLike polished) {
        shapeless(cut)
                .requires(raw)
                .requires(GEM_SAW)
                .unlockedWith(raw)
                .save();

        shapeless(polished)
                .requires(cut)
                .requires(ItemTags.SAND, 2)
                .unlockedWith(cut)
                .save();
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

    private Ingredient ingOf(ItemLike item) {
        return Ingredient.of(item);
    }

    private Ingredient ingOf(TagKey<Item> item) {
        return Ingredient.of(item);
    }
}
