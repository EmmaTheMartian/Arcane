package martian.arcane.datagen.server;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneTags;
import martian.arcane.common.registry.ArcaneBlocks;
import martian.arcane.common.registry.ArcaneDataComponents;
import martian.arcane.common.registry.ArcaneItems;
import martian.arcane.common.registry.ArcaneSpells;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
                DEEPSLATE           = ingOf(Blocks.DEEPSLATE_BRICKS.asItem()),
                AURAGLASS           = ingOf(ArcaneBlocks.AURAGLASS.asItem()),
                AURAGLASS_DUST      = ingOf(ArcaneItems.AURAGLASS_DUST),
                COPPER_CORE         = ingOf(ArcaneItems.COPPER_CORE),
                LARIMAR_CORE        = ingOf(ArcaneItems.LARIMAR_CORE),
                AURACHALCUM_CORE    = ingOf(ArcaneItems.AURACHALCUM_CORE),
                ELDRITCH_CORE       = ingOf(ArcaneItems.ELDRITCH_CORE),
                SPELL_CIRCLE_CORE   = ingOf(ArcaneItems.SPELL_CIRCLE_CORE),
                POLISHED_LARIMAR    = ingOf(ArcaneItems.POLISHED_LARIMAR),
                FADED_LARIMAR       = ingOf(ArcaneItems.FADED_POLISHED_LARIMAR),
                AURACHALCUM         = ingOf(ArcaneItems.AURACHALCUM),
                ELDRITCH_ALLOY      = ingOf(ArcaneItems.ELDRITCH_ALLOY)
        ;

        // Materials
        {
            shapeless(ArcaneItems.RAW_AURACHALCUM.get(), 2, Map.of(
                    ingOf(ArcaneItems.POLISHED_IDOCRASE.get()), 1,
                    ingOf(OBSIDIAN), 2
            )).unlockedWith(ArcaneItems.POLISHED_IDOCRASE.get()).save();

            shaped(ArcaneItems.COPPER_CORE.get())
                    .pattern(" G ", "GCG", " G ")
                    .define('G', GLASS)
                    .define('C', COPPER)
                    .unlockedWith(Items.COPPER_INGOT)
                    .save();

            shaped(ArcaneItems.SPELL_CIRCLE_CORE.get())
                    .pattern(" A ", "ACA", " A ")
                    .define('A', AURAGLASS_DUST)
                    .define('C', AURACHALCUM_CORE)
                    .unlockedWith(ArcaneItems.AURACHALCUM_CORE.get())
                    .save();

            // Hammering Recipes
            SpellRecipeBuilder.hammering()
                    .setInput(AURAGLASS)
                    .addResult(ArcaneItems.AURAGLASS_SHARD.get())
                    .addResult(ArcaneItems.AURAGLASS_SHARD.get(), 1, 0.5F)
                    .unlockedWith(ArcaneBlocks.AURAGLASS)
                    .save(helper);

            SpellRecipeBuilder.hammering()
                    .setInput(ArcaneItems.AURAGLASS_SHARD.get())
                    .addResult(ArcaneItems.AURAGLASS_DUST.get())
                    .addResult(ArcaneItems.AURAGLASS_DUST.get(), 1, 0.5F)
                    .unlockedWith(ArcaneItems.AURAGLASS_SHARD)
                    .save(helper);

            SpellRecipeBuilder.hammering()
                    .setInput(Items.DEEPSLATE)
                    .addResult(Items.COAL, 1, 0.2f)
                    .addResult(Items.IRON_NUGGET, 2, 0.1f)
                    .addResult(Items.GOLD_NUGGET, 1, 0.05f)
                    .addResult(Items.REDSTONE, 2, 0.03f)
                    .addResult(Items.DIAMOND, 1, 0.01f)
                    .unlockedWith(ArcaneBlocks.PEDESTAL)
                    .save(helper);

            // Pedestal Interaction Recipes
            PedestalRecipeBuilder.simpleRecipe(ingOf(COPPER), AURAGLASS, ArcaneItems.COPPER_CORE.get(), 1)
                    .unlockedWith(Items.COPPER_INGOT).save(helper);
            PedestalRecipeBuilder.simpleRecipe(ingOf(ArcaneItems.POLISHED_LARIMAR.get()), AURAGLASS, ArcaneItems.LARIMAR_CORE.get(), 1)
                    .unlockedWith(ArcaneItems.POLISHED_LARIMAR.get()).save(helper);
            PedestalRecipeBuilder.simpleRecipe(ingOf(ArcaneItems.AURACHALCUM.get()), AURAGLASS, ArcaneItems.AURACHALCUM_CORE.get(), 1)
                    .unlockedWith(ArcaneItems.AURACHALCUM.get()).save(helper);
            PedestalRecipeBuilder.simpleRecipe(ingOf(ArcaneItems.ELDRITCH_ALLOY.get()), AURAGLASS, ArcaneItems.ELDRITCH_CORE.get(), 1)
                    .unlockedWith(ArcaneItems.ELDRITCH_ALLOY.get()).save(helper);

            // Gems
            cutAndPolish(ArcaneItems.RAW_LARIMAR.get(), ArcaneItems.CUT_LARIMAR.get(), ArcaneItems.POLISHED_LARIMAR.get());
            cutAndPolish(ArcaneItems.FADED_RAW_LARIMAR.get(), ArcaneItems.FADED_CUT_LARIMAR.get(), ArcaneItems.FADED_POLISHED_LARIMAR.get());
            cutAndPolish(ArcaneItems.RAW_IDOCRASE.get(), ArcaneItems.CUT_IDOCRASE.get(), ArcaneItems.POLISHED_IDOCRASE.get());

            // Storage Blocks
            storageBlock(FADED_LARIMAR, ArcaneBlocks.FADED_LARIMAR_BLOCK.get().asItem());
            storageBlock(POLISHED_LARIMAR, ArcaneBlocks.LARIMAR_BLOCK.get().asItem());
            storageBlock(AURACHALCUM, ArcaneBlocks.AURACHALCUM_BLOCK.get().asItem());
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
            shaped(ArcaneBlocks.PEDESTAL.get().asItem())
                    .pattern("CBC", " S ", "SSS")
                    .define('C', COPPER)
                    .define('S', DEEPSLATE)
                    .define('B', COPPER_CORE)
                    .unlockedWith(ArcaneItems.COPPER_CORE.get())
                    .save();

            shaped(ArcaneBlocks.AURA_INFUSER.asItem())
                    .pattern("BCB", " O ", "OOO")
                    .define('B', COPPER)
                    .define('C', COPPER_CORE)
                    .define('O', DEEPSLATE)
                    .unlockedWith(ArcaneItems.COPPER_CORE.get())
                    .save();

            shaped(ArcaneBlocks.AURA_BASIN.asItem())
                    .pattern("B B", "B B", "BCB")
                    .define('B', DEEPSLATE)
                    .define('C', COPPER_CORE)
                    .unlockedWith(ArcaneItems.COPPER_CORE.get())
                    .save();

            shaped(ArcaneBlocks.AURA_CONNECTOR.asItem())
                    .pattern(" A ", "DDD")
                    .define('A', COPPER_CORE)
                    .define('D', DEEPSLATE)
                    .unlockedWith(ArcaneItems.COPPER_CORE.get())
                    .save();

            shaped(ArcaneBlocks.HEAT_COLLECTOR.get().asItem())
                    .pattern("GIG", "FCF", "GIG")
                    .define('G', GLASS)
                    .define('I', COPPER)
                    .define('F', ingOf(Items.FLINT))
                    .define('C', COPPER_CORE)
                    .unlockedWith(ArcaneItems.COPPER_CORE.get())
                    .save();

            shaped(ArcaneBlocks.AQUA_COLLECTOR.get().asItem())
                    .pattern("GIG", "ICI", "GWG")
                    .define('G', GLASS)
                    .define('I', COPPER)
                    .define('W', ingOf(Items.WATER_BUCKET))
                    .define('C', COPPER_CORE)
                    .unlockedWith(ArcaneItems.COPPER_CORE.get())
                    .save();
        }

        // Tools
        {
            helper.defaultCategory = RecipeCategory.TOOLS;

            shaped(ArcaneItems.AURA_WRENCH.get())
                    .pattern("C C", " B ", " C ")
                    .define('C', COPPER)
                    .define('B', COPPER_CORE)
                    .unlockedWith(ArcaneItems.COPPER_CORE.get())
                    .save();

            shaped(ArcaneItems.AURA_CONFIGURATOR.get())
                    .pattern("B B", " I ", " B ")
                    .define('B', IRON)
                    .define('I', COPPER_CORE)
                    .unlockedWith(ArcaneItems.COPPER_CORE.get())
                    .save();

            shaped(ArcaneItems.GEM_SAW.get())
                    .pattern("  B", " BS", "BS ")
                    .define('B', COPPER)
                    .define('S', Tags.Items.NUGGETS_IRON)
                    .unlockedWith(Items.COPPER_INGOT)
                    .save();

            shaped(ArcaneItems.AURAOMETER.get())
                    .pattern("R", "C", "B")
                    .define('R', REDSTONE)
                    .define('B', COPPER_CORE)
                    .define('C', COPPER)
                    .unlockedWith(ArcaneItems.COPPER_CORE.get())
                    .save();

            shapeless(ArcaneItems.ARCANE_BLEACH.get(), Map.of(
                    ingOf(Items.GLASS_BOTTLE), 1,
                    ingOf(Items.BONE_MEAL), 1,
                    ingOf(ArcaneItems.AURAGLASS_DUST.get()), 1
            )).unlockedWith(ArcaneItems.AURAGLASS_DUST.get()).save();

            shaped(ArcaneItems.SPELL_CHALK.get())
                    .pattern(" CB", "CSC", "BC ")
                    .define('C', ingOf(Items.CLAY_BALL))
                    .define('B', ingOf(Items.BONE_MEAL))
                    .define('S', SPELL_CIRCLE_CORE)
                    .unlockedWith(ArcaneItems.SPELL_CIRCLE_CORE.get())
                    .save();

            shaped(ArcaneItems.ENDERPACK.get())
                    .pattern(" P ")
                    .pattern("LEL")
                    .pattern("PPP")
                    .define('P', POLISHED_LARIMAR)
                    .define('L', Tags.Items.LEATHERS)
                    .define('E', ingOf(Items.ENDER_CHEST))
                    .unlockedWith(ArcaneItems.POLISHED_LARIMAR.get())
                    .save();

            // Auraglass Bottles
            {
                shaped(ArcaneItems.AURAGLASS_BOTTLE.get())
                        .pattern("A A", " A ")
                        .define('A', AURAGLASS)
                        .unlockedWith(ArcaneBlocks.AURAGLASS.get().asItem())
                        .save();

                shaped(ArcaneItems.MEDIUM_AURAGLASS_BOTTLE.get())
                        .pattern("BAB", " B ")
                        .define('A', ingOf(ArcaneItems.AURAGLASS_BOTTLE.get()))
                        .define('B', POLISHED_LARIMAR)
                        .unlockedWith(ArcaneItems.AURAGLASS_BOTTLE.get())
                        .save();

                shaped(ArcaneItems.LARGE_AURAGLASS_BOTTLE.get())
                        .pattern("BAB", " B ")
                        .define('A', ingOf(ArcaneItems.MEDIUM_AURAGLASS_BOTTLE.get()))
                        .define('B', AURACHALCUM)
                        .unlockedWith(ArcaneItems.MEDIUM_AURAGLASS_BOTTLE.get())
                        .save();

                shaped(ArcaneItems.EXTREME_AURAGLASS_BOTTLE.get())
                        .pattern("BAB", " B ")
                        .define('A', ingOf(ArcaneItems.LARGE_AURAGLASS_BOTTLE.get()))
                        .define('B', ELDRITCH_ALLOY)
                        .unlockedWith(ArcaneItems.LARGE_AURAGLASS_BOTTLE.get())
                        .save();
            }

            // Wands
            {
                Map<Item, Item> wands = new HashMap<>();
                wands.put(Items.ACACIA_PLANKS, ArcaneItems.WAND_ACACIA.get());
                wands.put(Items.BAMBOO_PLANKS, ArcaneItems.WAND_BAMBOO.get());
                wands.put(Items.BIRCH_PLANKS, ArcaneItems.WAND_BIRCH.get());
                wands.put(Items.CHERRY_PLANKS, ArcaneItems.WAND_CHERRY.get());
                wands.put(Items.DARK_OAK_PLANKS, ArcaneItems.WAND_DARK_OAK.get());
                wands.put(Items.JUNGLE_PLANKS, ArcaneItems.WAND_JUNGLE.get());
                wands.put(Items.MANGROVE_PLANKS, ArcaneItems.WAND_MANGROVE.get());
                wands.put(Items.OAK_PLANKS, ArcaneItems.WAND_OAK.get());
                wands.put(Items.SPRUCE_PLANKS, ArcaneItems.WAND_SPRUCE.get());
                wands.put(Items.WARPED_PLANKS, ArcaneItems.WAND_WARPED.get());
                wands.put(Items.CRIMSON_PLANKS, ArcaneItems.WAND_CRIMSON.get());
                wands.put(Items.COPPER_INGOT, ArcaneItems.WAND_COPPER.get());
                wands.forEach((wandItem, output) -> shaped(output)
                        .pattern("  P", " P ", "C  ")
                        .define('C', COPPER_CORE)
                        .define('P', ingOf(wandItem))
                        .unlockedWith(ArcaneItems.COPPER_CORE.get())
                        .save(itemKey(output).withPrefix("wands/")));

                // Advanced Wands
                shaped(ArcaneItems.WAND_LARIMAR.get())
                        .pattern("  B", " W ", "C  ")
                        .define('B', POLISHED_LARIMAR)
                        .define('C', LARIMAR_CORE)
                        .define('W', ArcaneTags.BASIC_WANDS)
                        .unlockedWith(ArcaneItems.LARIMAR_CORE.get())
                        .save(itemKey(ArcaneItems.WAND_LARIMAR.get()).withPrefix("wands/"));

                // Mythical Wands
                shaped(ArcaneItems.WAND_AURACHALCUM.get())
                        .pattern("  D", " W ", "C  ")
                        .define('D', AURACHALCUM)
                        .define('W', ArcaneTags.ADVANCED_WANDS)
                        .define('C', AURACHALCUM_CORE)
                        .unlockedWith(ArcaneItems.AURACHALCUM_CORE.get())
                        .save(itemKey(ArcaneItems.WAND_AURACHALCUM.get()).withPrefix("wands/"));

                shaped(ArcaneItems.WAND_ELDRITCH.get())
                        .pattern("  D", " W ", "C  ")
                        .define('D', ELDRITCH_ALLOY)
                        .define('W', ArcaneTags.ADVANCED_WANDS)
                        .define('C', ELDRITCH_CORE)
                        .unlockedWith(ArcaneItems.ELDRITCH_CORE.get())
                        .save(itemKey(ArcaneItems.WAND_ELDRITCH.get()).withPrefix("wands/"));
            }
        }

        // Ore Processing
        {
            helper.defaultCategory = RecipeCategory.MISC;
            helper.moveNamespacesToModid = false;
            oreProcessingLine(Items.RAW_COPPER, ArcaneItems.CRUSHED_RAW_COPPER.get(), ArcaneItems.PURIFIED_RAW_COPPER.get(), Items.COPPER_INGOT);
            oreProcessingLine(Items.RAW_IRON, ArcaneItems.CRUSHED_RAW_IRON.get(), ArcaneItems.PURIFIED_RAW_IRON.get(), Items.IRON_INGOT);
            oreProcessingLine(Items.RAW_GOLD, ArcaneItems.CRUSHED_RAW_GOLD.get(), ArcaneItems.PURIFIED_RAW_GOLD.get(), Items.GOLD_INGOT);
            helper.moveNamespacesToModid = true;
        }

        // Spell Tablets
        {
            helper.defaultCategory = RecipeCategory.TOOLS;

            PedestalRecipeBuilder.simpleRecipe(ingOf(Items.CLAY_BALL), ingOf(Items.PAPER), ArcaneItems.SPELL_TABLET.get(), 1)
                    .unlockedWith(ArcaneBlocks.PEDESTAL.get().asItem()).save(helper);

            Map<ResourceLocation, Item> spellItems = new HashMap<>();
            spellItems.put(ArcaneSpells.BREAKING.getId(), Items.DIAMOND_PICKAXE);
            spellItems.put(ArcaneSpells.HAMMERING.getId(), Items.IRON_BLOCK);
            spellItems.put(ArcaneSpells.PURIFYING.getId(), Items.EMERALD);
            spellItems.put(ArcaneSpells.CLEANSING.getId(), Items.WATER_BUCKET);
            spellItems.put(ArcaneSpells.BUILDING.getId(), Items.BRICKS);
            spellItems.put(ArcaneSpells.DASHING.getId(), Items.FEATHER);
            spellItems.put(ArcaneSpells.CRAFTING.getId(), Items.CRAFTING_TABLE);
            spellItems.put(ArcaneSpells.ACTIVATOR.getId(), ArcaneItems.SPELL_CIRCLE_CORE.get());
            spellItems.forEach((id, item) -> {
                var stack = new ItemStack(ArcaneItems.SPELL_TABLET.get());
                stack.set(ArcaneDataComponents.SPELL, id);
                var holder = new martian.arcane.api.recipe.RecipeOutput.DataGenHolder(stack, 1);
                PedestalRecipeBuilder.simpleRecipe(ingOf(ArcaneItems.SPELL_TABLET.get()), ingOf(item), holder.toRecipeOutput())
                        .unlockedWith(item).save(helper, id.withPrefix("spell_tablets/"));
            });
        }
    }

    private void storageBlock(Ingredient of, Item output) {
        shaped(output)
                .pattern("AAA", "AAA", "AAA")
                .define('A', of)
                .unlockedWith(of.getItems()[0].getItem())
                .save();
    }

    private void cutAndPolish(Item raw, Item cut, Item polished) {
        shapeless(cut, 1)
                .requires(raw)
                .requires(ArcaneItems.GEM_SAW.get())
                .unlockedWith(raw)
                .save();

        shapeless(polished, 1)
                .requires(cut)
                .requires(ItemTags.SAND, 2)
                .unlockedWith(cut)
                .save();
    }

    private void oreProcessingLine(Item raw, Item crushed, Item purified, Item ingot) {
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

    private ResourceLocation itemKey(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    private Ingredient ingOf(ItemLike item) {
        return Ingredient.of(item);
    }

    private Ingredient ingOf(TagKey<Item> item) {
        return Ingredient.of(item);
    }
}
