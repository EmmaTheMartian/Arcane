package martian.arcane.datagen.server;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneTags;
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

    protected void build() {
        helper.prefixesByProvider.put(SpellRecipeBuilder.class, "spells");
        helper.prefixesByProvider.put(PedestalRecipeBuilder.class, "pedestal");
        helper.prefixesByProvider.put(CauldronMixingBuilder.class, "cauldron_mixing");
        helper.prefixesByProvider.put(ShapelessRecipeBuilder.class, "shapeless");
        helper.prefixesByProvider.put(ShapedRecipeBuilder.class, "shaped");
        helper.prefixesByProvider.put(SimpleCookingRecipeBuilder.class, "cooking");

        final TagKey<Item>
                TAG_COPPER      = Tags.Items.INGOTS_COPPER,
                TAG_IRON        = Tags.Items.INGOTS_IRON,
                TAG_REDSTONE    = Tags.Items.DUSTS_REDSTONE,
                TAG_GLASS       = Tags.Items.GLASS_BLOCKS_COLORLESS,
                TAG_STONE       = Tags.Items.STONES,
                TAG_OBSIDIAN    = Tags.Items.OBSIDIANS
        ;

        final Ingredient ING_DEEPSLATE = ingOf(Blocks.DEEPSLATE_BRICKS);

        // Materials
        {
            shapeless(RAW_AURACHALCUM, 2, Map.of(
                    ingOf(IDOCRASE.polished()), 1,
                    ingOf(TAG_OBSIDIAN), 2
            )).unlockedWith(IDOCRASE.polished()).save();

            shaped(COPPER_CORE)
                    .pattern(" G ", "GCG", " G ")
                    .define('G', TAG_GLASS)
                    .define('C', TAG_COPPER)
                    .unlockedWith(Items.COPPER_INGOT)
                    .save();

            shaped(SPELL_CIRCLE_CORE)
                    .pattern(" A ", "ACA", " A ")
                    .define('A', AURAGLASS_DUST)
                    .define('C', AURACHALCUM_CORE)
                    .unlockedWith(AURACHALCUM_CORE)
                    .save();

            // Hammering Recipes
            SpellRecipeBuilder.hammering()
                    .setInput(AURAGLASS)
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
            PedestalRecipeBuilder.simpleRecipe(ingOf(TAG_COPPER), ingOf(AURAGLASS), COPPER_CORE, 1).unlockedWith(Items.COPPER_INGOT).save(helper);
            PedestalRecipeBuilder.simpleRecipe(ingOf(LARIMAR.polished()), ingOf(AURAGLASS), LARIMAR_CORE, 1).unlockedWith(LARIMAR.polished()).save(helper);
            PedestalRecipeBuilder.simpleRecipe(ingOf(AURACHALCUM), ingOf(AURAGLASS), AURACHALCUM_CORE, 1).unlockedWith(AURACHALCUM).save(helper);
            PedestalRecipeBuilder.simpleRecipe(ingOf(ELDRITCH_ALLOY), ingOf(AURAGLASS), ELDRITCH_CORE, 1).unlockedWith(ELDRITCH_ALLOY).save(helper);

            // Gems
            gemLine(LARIMAR);
            gemLine(FADED_LARIMAR);
            gemLine(IDOCRASE);

            // Storage Blocks
            storageBlock(FADED_LARIMAR.polished(), FADED_LARIMAR_BLOCK);
            storageBlock(LARIMAR.polished(), LARIMAR_BLOCK);
            storageBlock(AURACHALCUM, AURACHALCUM_BLOCK);
        }

        // Misc Utility Recipes
        {
            SpellRecipeBuilder.hammering().setInput(TAG_STONE).addResult(Items.COBBLESTONE).unlockedWith(Items.STONE).save(helper);
            SpellRecipeBuilder.hammering().setInput(Items.COBBLESTONE).addResult(Items.GRAVEL).unlockedWith(Items.COBBLESTONE).save(helper);
            SpellRecipeBuilder.hammering().setInput(Items.GRAVEL).addResult(Items.SAND).unlockedWith(Items.GRAVEL).save(helper);

            SpellRecipeBuilder.cleansing().setInput(Items.GRAVEL).addResult(Items.FLINT).unlockedWith(Items.GRAVEL).save(helper);

//            SpellRecipeBuilder.freezing().setInput(Blocks.WATER).addResult(Blocks.ICE).unlockedWith(Blocks.WATER).save(helper);
//            SpellRecipeBuilder.freezing().setInput(Blocks.WATER_CAULDRON).addResult(Blocks.POWDER_SNOW_CAULDRON).unlockedWith(Blocks.WATER_CAULDRON).save(helper);
//            SpellRecipeBuilder.freezing().setInput(Blocks.MAGMA_BLOCK).addResult(Blocks.OBSIDIAN).unlockedWith(Blocks.MAGMA_BLOCK).save(helper);
            SpellRecipeBuilder.freezing().setInput(Items.CRYING_OBSIDIAN).addResult(FROZEN_OBSIDIAN_BLOCK).unlockedWith(Items.CRYING_OBSIDIAN).save(helper);
//            SpellRecipeBuilder.freezing().setInput(Blocks.LAVA).addResult(Blocks.MAGMA_BLOCK).unlockedWith(Blocks.LAVA).save(helper);
        }

        // Blocks
        {
            helper.defaultCategory = RecipeCategory.REDSTONE;

            // Machines
            shaped(PEDESTAL.block())
                    .pattern("SSS", " S ", "CBC")
                    .define('C', TAG_COPPER)
                    .define('S', ING_DEEPSLATE)
                    .define('B', COPPER_CORE)
                    .unlockedWith(COPPER_CORE)
                    .save();

            shaped(AURA_INFUSER.block())
                    .pattern("BCB", " O ", "OOO")
                    .define('B', TAG_COPPER)
                    .define('C', COPPER_CORE)
                    .define('O', ING_DEEPSLATE)
                    .unlockedWith(COPPER_CORE)
                    .save();

            shaped(AURA_BASIN.block())
                    .pattern("B B", "B B", "BCB")
                    .define('B', ING_DEEPSLATE)
                    .define('C', COPPER_CORE)
                    .unlockedWith(COPPER_CORE)
                    .save();

            shaped(AURA_CONNECTOR.block())
                    .pattern(" A ", "DDD")
                    .define('A', COPPER_CORE)
                    .define('D', ING_DEEPSLATE)
                    .unlockedWith(COPPER_CORE)
                    .save();

            shaped(HEAT_COLLECTOR.block())
                    .pattern("GIG", "FCF", "GIG")
                    .define('G', TAG_GLASS)
                    .define('I', TAG_COPPER)
                    .define('F', Items.FLINT)
                    .define('C', COPPER_CORE)
                    .unlockedWith(COPPER_CORE)
                    .save();

            shaped(AQUA_COLLECTOR.block())
                    .pattern("GIG", "ICI", "GWG")
                    .define('G', TAG_GLASS)
                    .define('I', TAG_COPPER)
                    .define('W', Items.WATER_BUCKET)
                    .define('C', COPPER_CORE)
                    .unlockedWith(COPPER_CORE)
                    .save();
        }

        // Tools
        {
            helper.defaultCategory = RecipeCategory.TOOLS;

            shaped(AURA_WRENCH)
                    .pattern("C C", " B ", " C ")
                    .define('C', TAG_COPPER)
                    .define('B', COPPER_CORE)
                    .unlockedWith(COPPER_CORE)
                    .save();

            shaped(AURA_CONFIGURATOR)
                    .pattern("B B", " I ", " B ")
                    .define('B', TAG_IRON)
                    .define('I', COPPER_CORE)
                    .unlockedWith(COPPER_CORE)
                    .save();

            shaped(GEM_SAW)
                    .pattern("  B", " BS", "BS ")
                    .define('B', TAG_COPPER)
                    .define('S', Tags.Items.NUGGETS_IRON)
                    .unlockedWith(Items.COPPER_INGOT)
                    .save();

            shaped(AURAOMETER)
                    .pattern("R", "C", "B")
                    .define('R', TAG_REDSTONE)
                    .define('B', COPPER_CORE)
                    .define('C', TAG_COPPER)
                    .unlockedWith(COPPER_CORE)
                    .save();

            shapeless(ARCANE_BLEACH, Map.of(
                    ingOf(Items.GLASS_BOTTLE), 1,
                    ingOf(Items.BONE_MEAL), 1,
                    ingOf(AURAGLASS_DUST), 1
            )).unlockedWith(AURAGLASS_DUST).save();

            shaped(SPELL_CHALK)
                    .pattern(" CB", "CSC", "BC ")
                    .define('C', Items.CLAY_BALL)
                    .define('B', Items.BONE_MEAL)
                    .define('S', SPELL_CIRCLE_CORE)
                    .unlockedWith(SPELL_CIRCLE_CORE)
                    .save();

            shaped(ENDERPACK)
                    .pattern(" P ")
                    .pattern("LEL")
                    .pattern("PPP")
                    .define('P', LARIMAR.polished())
                    .define('L', Tags.Items.LEATHERS)
                    .define('E', Items.ENDER_CHEST)
                    .unlockedWith(LARIMAR.polished())
                    .save();

            // Auraglass Bottles
            {
                shaped(AURAGLASS_BOTTLE)
                        .pattern("A A", " A ")
                        .define('A', AURAGLASS)
                        .unlockedWith(AURAGLASS)
                        .save();

                shaped(MEDIUM_AURAGLASS_BOTTLE)
                        .pattern("BAB", " B ")
                        .define('A', AURAGLASS_BOTTLE)
                        .define('B', LARIMAR.polished())
                        .unlockedWith(AURAGLASS_BOTTLE)
                        .save();

                shaped(LARGE_AURAGLASS_BOTTLE)
                        .pattern("BAB", " B ")
                        .define('A', MEDIUM_AURAGLASS_BOTTLE)
                        .define('B', AURACHALCUM)
                        .unlockedWith(MEDIUM_AURAGLASS_BOTTLE)
                        .save();

                shaped(EXTREME_AURAGLASS_BOTTLE)
                        .pattern("BAB", " B ")
                        .define('A', LARGE_AURAGLASS_BOTTLE)
                        .define('B', ELDRITCH_ALLOY)
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
                        .define('C', COPPER_CORE)
                        .define('P', wandItem)
                        .unlockedWith(COPPER_CORE)
                        .save(itemKey(output).withPrefix("wands/")));

                // Advanced Wands
                shaped(WAND_LARIMAR)
                        .pattern("  B", " W ", "C  ")
                        .define('B', LARIMAR.polished())
                        .define('C', LARIMAR_CORE)
                        .define('W', ArcaneTags.BASIC_WANDS)
                        .unlockedWith(LARIMAR_CORE)
                        .save(itemKey(WAND_LARIMAR).withPrefix("wands/"));

                // Mythical Wands
                shaped(WAND_AURACHALCUM)
                        .pattern("  D", " W ", "C  ")
                        .define('D', AURACHALCUM)
                        .define('W', ArcaneTags.ADVANCED_WANDS)
                        .define('C', AURACHALCUM_CORE)
                        .unlockedWith(AURACHALCUM_CORE)
                        .save(itemKey(WAND_AURACHALCUM).withPrefix("wands/"));

                shaped(WAND_ELDRITCH)
                        .pattern("  D", " W ", "C  ")
                        .define('D', ELDRITCH_ALLOY)
                        .define('W', ArcaneTags.ADVANCED_WANDS)
                        .define('C', ELDRITCH_CORE)
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

            Map<DeferredHolder<AbstractSpell, ?>, Ingredient> spellItems = new HashMap<>();
            spellItems.put(SPELL_BREAKING, ingOf(Items.DIAMOND_PICKAXE));
            spellItems.put(SPELL_HAMMERING, ingOf(Items.IRON_BLOCK));
            spellItems.put(SPELL_PURIFYING, ingOf(Items.EMERALD));
            spellItems.put(SPELL_CLEANSING, ingOf(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.WATER)));
            spellItems.put(SPELL_BUILDING, ingOf(Items.BRICKS));
            spellItems.put(SPELL_DASHING, ingOf(Items.FEATHER));
            spellItems.put(SPELL_CRAFTING, ingOf(Items.CRAFTING_TABLE));
            spellItems.put(SPELL_ACTIVATOR, ingOf(SPELL_CIRCLE_CORE));
            spellItems.put(SPELL_CONJURE_WATER, ingOf(Items.WATER_BUCKET));
            spellItems.put(SPELL_FREEZING, ingOf(Items.ICE));
            spellItems.put(SPELL_LIGHTING, ingOf(Items.TORCH));
            spellItems.put(SPELL_MIXING, ingOf(Items.STICK));

            spellItems.forEach((spellHolder, item) -> {
                var stack = SPELL_TABLET.get().getDefaultInstance();
                stack.set(ArcaneContent.DC_SPELL, spellHolder.getId());
                var holder = new martian.arcane.api.recipe.RecipeOutput.DataGenHolder(stack, 1);
                PedestalRecipeBuilder
                        .simpleRecipe(ingOf(SPELL_TABLET.get()), item, holder.toRecipeOutput())
                        .unlockedWith(item.getItems()[0].getItem())
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
