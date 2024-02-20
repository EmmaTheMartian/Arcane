package martian.arcane.datagen.server;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneTags;
import martian.arcane.api.NBTHelpers;
import martian.arcane.common.registry.ArcaneBlocks;
import martian.arcane.common.registry.ArcaneItems;
import martian.arcane.common.registry.ArcaneSpells;
import martian.arcane.datagen.builders.CleansingRecipeBuilder;
import martian.arcane.datagen.builders.HammeringRecipeBuilder;
import martian.arcane.datagen.builders.PedestalRecipeBuilder;
import martian.arcane.datagen.builders.PurifyingRecipeBuilder;
import martian.arcane.datagen.util.RecipeDataHelper;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ArcaneRecipeProvider extends RecipeProvider {
    public ArcaneRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> writer) {
        RecipeDataHelper helper = new RecipeDataHelper(writer, ArcaneMod.MODID, RecipeCategory.MISC);

        // Materials
        {
            helper.shapeless(ArcaneItems.BLUE_GOLD.get(), 4, Map.of(
                    Ingredient.of(Items.GOLD_INGOT), 3,
                    Ingredient.of(Tags.Items.INGOTS_IRON), 1
            )).unlockedWith(Items.GOLD_INGOT).save();

            helper.shapeless(ArcaneItems.RAW_AURACHALCUM.get(), 2, Map.of(
                    Ingredient.of(ArcaneItems.BLUE_GOLD.get()), 1,
                    Ingredient.of(Items.OBSIDIAN), 1
            )).unlockedWith(ArcaneItems.BLUE_GOLD.get()).save();

            helper.shaped(ArcaneItems.BLUE_GOLD_CORE.get())
                    .pattern(" G ", "GBG", " G ")
                    .define('G', Tags.Items.GLASS_COLORLESS)
                    .define('B', ArcaneItems.BLUE_GOLD.get())
                    .unlockedWith(ArcaneItems.BLUE_GOLD.get())
                    .save();

            helper.shaped(ArcaneItems.AURACHALCUM_CORE.get())
                    .pattern(" G ", "GBG", " G ")
                    .define('G', Tags.Items.GLASS_COLORLESS)
                    .define('B', ArcaneItems.AURACHALCUM.get())
                    .unlockedWith(ArcaneItems.AURACHALCUM.get())
                    .save();

            helper.shaped(ArcaneItems.SPELL_CIRCLE_CORE.get())
                    .pattern(" A ", "ACA", " A ")
                    .define('A', ArcaneItems.AURAGLASS_DUST.get())
                    .define('C', ArcaneItems.AURACHALCUM_CORE.get())
                    .unlockedWith(ArcaneItems.AURACHALCUM_CORE.get())
                    .save();

            // Hammering Recipes
            HammeringRecipeBuilder.hammering(Ingredient.of(ArcaneBlocks.AURAGLASS.get().asItem()), ArcaneItems.AURAGLASS_SHARD.get(), 2)
                    .unlockedBy("has_item", has(ArcaneBlocks.AURAGLASS.get().asItem()))
                    .save(writer);

            HammeringRecipeBuilder.hammering(Ingredient.of(ArcaneItems.AURAGLASS_SHARD.get()), ArcaneItems.AURAGLASS_DUST.get(), 2)
                    .unlockedBy("has_item", has(ArcaneItems.AURAGLASS_SHARD.get()))
                    .save(writer);

            PedestalRecipeBuilder.pedestalCrafting(Ingredient.of(ArcaneItems.ELDRITCH_ALLOY.get()), Ingredient.of(ArcaneBlocks.AURAGLASS.get().asItem()), true, ArcaneItems.ELDRITCH_CORE.get(), 1, null)
                    .unlockedBy("has_item", has(ArcaneItems.ELDRITCH_ALLOY.get()))
                    .save(writer);
        }

        // Misc Utility Recipes
        {
            helper.wrap(HammeringRecipeBuilder.hammering(Ingredient.of(Items.STONE), Items.COBBLESTONE, 1))
                    .unlockedWith(Items.STONE).save();

            helper.wrap(HammeringRecipeBuilder.hammering(Ingredient.of(Items.COBBLESTONE), Items.GRAVEL, 1))
                    .unlockedWith(Items.COBBLESTONE).save();

            helper.wrap(HammeringRecipeBuilder.hammering(Ingredient.of(Items.GRAVEL), Items.SAND, 1))
                    .unlockedWith(Items.GRAVEL).save();

            helper.wrap(CleansingRecipeBuilder.cleansing(Ingredient.of(Items.GRAVEL), Items.FLINT, 1))
                    .unlockedWith(Items.GRAVEL).save();
        }

        // Blocks
        {
            helper.defaultCategory = RecipeCategory.REDSTONE;
            helper.shaped(ArcaneBlocks.AURA_BASIN.get().asItem())
                    .pattern("B B", "B B", "BBB")
                    .define('B', ArcaneItems.BLUE_GOLD.get())
                    .unlockedWith(ArcaneItems.BLUE_GOLD.get())
                    .save();

            helper.shaped(ArcaneBlocks.AURA_INFUSER.get().asItem())
                    .pattern("BCB", "OOO")
                    .define('B', ArcaneItems.BLUE_GOLD.get())
                    .define('C', ArcaneItems.BLUE_GOLD_CORE.get())
                    .define('O', Tags.Items.OBSIDIAN)
                    .unlockedWith(ArcaneItems.BLUE_GOLD.get())
                    .save();

            helper.shaped(ArcaneBlocks.IGNIS_COLLECTOR.get().asItem())
                    .pattern("GIG", "FCF", "GIG")
                    .define('G', Tags.Items.GLASS_COLORLESS)
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('F', Items.FLINT)
                    .define('C', ArcaneItems.BLUE_GOLD_CORE.get())
                    .unlockedWith(ArcaneItems.BLUE_GOLD_CORE.get())
                    .save();

            helper.shaped(ArcaneBlocks.AQUA_COLLECTOR.get().asItem())
                    .pattern("GIG", "ICI", "GWG")
                    .define('G', Tags.Items.GLASS_COLORLESS)
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('W', Items.WATER_BUCKET)
                    .define('C', ArcaneItems.BLUE_GOLD_CORE.get())
                    .unlockedWith(ArcaneItems.BLUE_GOLD_CORE.get())
                    .save();

            helper.shaped(ArcaneBlocks.PEDESTAL.get().asItem())
                    .pattern("BBB", " L ", " S ")
                    .define('B', ArcaneItems.BLUE_GOLD.get())
                    .define('L', Tags.Items.STONE)
                    .define('S', Items.STONE_SLAB)
                    .unlockedWith(ArcaneItems.BLUE_GOLD.get())
                    .save();

            // Inserters and Extractors
            helper.shaped(ArcaneBlocks.AURA_EXTRACTOR.get().asItem())
                    .pattern(" B ", "SSS")
                    .define('B', ArcaneItems.BLUE_GOLD.get())
                    .define('S', Tags.Items.STONE)
                    .unlockedWith(ArcaneItems.BLUE_GOLD.get())
                    .save();

            helper.shaped(ArcaneBlocks.AURA_INSERTER.get().asItem())
                    .pattern("SSS", " B ")
                    .define('B', ArcaneItems.BLUE_GOLD.get())
                    .define('S', Tags.Items.STONE)
                    .unlockedWith(ArcaneItems.BLUE_GOLD.get())
                    .save();

            helper.shaped(ArcaneBlocks.IMPROVED_AURA_EXTRACTOR.get().asItem())
                    .pattern(" B ", "SSS")
                    .define('B', ArcaneBlocks.AURA_EXTRACTOR.get().asItem())
                    .define('S', ArcaneItems.AURACHALCUM.get())
                    .unlockedWith(ArcaneItems.AURACHALCUM.get())
                    .save();

            helper.shaped(ArcaneBlocks.IMPROVED_AURA_INSERTER.get().asItem())
                    .pattern("SSS", " B ")
                    .define('B', ArcaneBlocks.AURA_INSERTER.get().asItem())
                    .define('S', ArcaneItems.AURACHALCUM.get())
                    .unlockedWith(ArcaneItems.AURACHALCUM.get())
                    .save();

            // Alt recipes for extractors and inserters
            helper.swappable(ArcaneBlocks.AURA_EXTRACTOR.get().asItem(), ArcaneBlocks.AURA_INSERTER.get().asItem());
            helper.swappable(ArcaneBlocks.IMPROVED_AURA_EXTRACTOR.get().asItem(), ArcaneBlocks.IMPROVED_AURA_INSERTER.get().asItem());
        }

        // Tools
        {
            helper.defaultCategory = RecipeCategory.TOOLS;
            helper.shaped(ArcaneItems.AURA_WRENCH.get())
                    .pattern("B B", " B ", " B ")
                    .define('B', ArcaneItems.BLUE_GOLD.get())
                    .unlockedWith(ArcaneItems.BLUE_GOLD.get())
                    .save();

            helper.shaped(ArcaneItems.AURA_CONFIGURATOR.get())
                    .pattern("B B", " I ", " B ")
                    .define('B', ArcaneItems.BLUE_GOLD.get())
                    .define('I', Tags.Items.INGOTS_IRON)
                    .unlockedWith(ArcaneItems.BLUE_GOLD.get())
                    .save();

            helper.shaped(ArcaneItems.AURAOMETER.get())
                    .pattern("R", "B", "B")
                    .define('R', Tags.Items.DUSTS_REDSTONE)
                    .define('B', ArcaneItems.BLUE_GOLD.get())
                    .unlockedWith(ArcaneItems.BLUE_GOLD.get())
                    .save();

            helper.shapeless(ArcaneItems.ARCANE_BLEACH.get(), Map.of(
                    Ingredient.of(Items.GLASS_BOTTLE), 1,
                    Ingredient.of(Items.BONE_MEAL), 1,
                    Ingredient.of(ArcaneItems.AURAGLASS_DUST.get()), 1
            )).unlockedWith(ArcaneItems.AURAGLASS_DUST.get()).save();

            helper.shaped(ArcaneItems.SPELL_CHALK.get())
                    .pattern(" CB", "CSC", "BC ")
                    .define('C', Items.CLAY_BALL)
                    .define('B', Items.BONE_MEAL)
                    .define('S', ArcaneItems.SPELL_CIRCLE_CORE.get())
                    .unlockedWith(ArcaneItems.SPELL_CIRCLE_CORE.get())
                    .save();

            // Auraglass Bottles
            {
                helper.shaped(ArcaneItems.AURAGLASS_BOTTLE.get())
                        .pattern("A A", " A ")
                        .define('A', ArcaneBlocks.AURAGLASS.get().asItem())
                        .unlockedWith(ArcaneBlocks.AURAGLASS.get().asItem())
                        .save();

                helper.shaped(ArcaneItems.MEDIUM_AURAGLASS_BOTTLE.get())
                        .pattern("BAB", " B ")
                        .define('A', ArcaneItems.AURAGLASS_BOTTLE.get())
                        .define('B', ArcaneItems.BLUE_GOLD.get())
                        .unlockedWith(ArcaneItems.AURAGLASS_BOTTLE.get())
                        .save();

                helper.shaped(ArcaneItems.LARGE_AURAGLASS_BOTTLE.get())
                        .pattern("BAB", " B ")
                        .define('A', ArcaneItems.MEDIUM_AURAGLASS_BOTTLE.get())
                        .define('B', ArcaneItems.BLUE_GOLD.get())
                        .unlockedWith(ArcaneItems.MEDIUM_AURAGLASS_BOTTLE.get())
                        .save();

                helper.shaped(ArcaneItems.EXTREME_AURAGLASS_BOTTLE.get())
                        .pattern("BAB", " B ")
                        .define('A', ArcaneItems.LARGE_AURAGLASS_BOTTLE.get())
                        .define('B', ArcaneItems.ELDRITCH_ALLOY.get())
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
                wands.forEach((wandItem, output) -> helper.shaped(output)
                        .pattern("  P", " P ", "C  ")
                        .define('C', ArcaneItems.BLUE_GOLD_CORE.get())
                        .define('P', wandItem)
                        .unlockedWith(ArcaneItems.BLUE_GOLD_CORE.get())
                        .save(itemKey(output).withPrefix("wands/")));

                // Advanced and Mythical Wands
                helper.shaped(ArcaneItems.WAND_BLUE_GOLD.get())
                        .pattern("  B", " W ", "C  ")
                        .define('B', ArcaneItems.BLUE_GOLD.get())
                        .define('C', ArcaneItems.BLUE_GOLD_CORE.get())
                        .define('W', ArcaneTags.BASIC_WANDS)
                        .unlockedWith(ArcaneItems.BLUE_GOLD_CORE.get())
                        .save(itemKey(ArcaneItems.WAND_BLUE_GOLD.get()).withPrefix("wands/"));

                helper.shaped(ArcaneItems.WAND_AURACHALCUM.get())
                        .pattern("  D", " W ", "C  ")
                        .define('D', ArcaneItems.AURACHALCUM.get())
                        .define('W', ArcaneTags.ADVANCED_WANDS)
                        .define('C', ArcaneItems.AURACHALCUM_CORE.get())
                        .unlockedWith(ArcaneItems.AURACHALCUM_CORE.get())
                        .save(itemKey(ArcaneItems.WAND_AURACHALCUM.get()).withPrefix("wands/"));

                helper.shaped(ArcaneItems.WAND_ELDRITCH.get())
                        .pattern("  D", " W ", "C  ")
                        .define('D', ArcaneItems.ELDRITCH_ALLOY.get())
                        .define('W', ArcaneTags.ADVANCED_WANDS)
                        .define('C', ArcaneItems.ELDRITCH_CORE.get())
                        .unlockedWith(ArcaneItems.ELDRITCH_CORE.get())
                        .save(itemKey(ArcaneItems.WAND_ELDRITCH.get()).withPrefix("wands/"));
            }
        }

        // Ore Processing
        {
            helper.defaultCategory = RecipeCategory.MISC;
            helper.moveNamespacesToModid = false;
            oreProcessingLine(helper, Items.RAW_COPPER, ArcaneItems.CRUSHED_RAW_COPPER.get(), ArcaneItems.PURIFIED_RAW_COPPER.get(), Items.COPPER_INGOT);
            oreProcessingLine(helper, Items.RAW_IRON, ArcaneItems.CRUSHED_RAW_IRON.get(), ArcaneItems.PURIFIED_RAW_IRON.get(), Items.IRON_INGOT);
            oreProcessingLine(helper, Items.RAW_GOLD, ArcaneItems.CRUSHED_RAW_GOLD.get(), ArcaneItems.PURIFIED_RAW_GOLD.get(), Items.GOLD_INGOT);
            helper.moveNamespacesToModid = true;
        }

        // Spell Tablets
        {
            helper.defaultCategory = RecipeCategory.TOOLS;

            helper.wrap(PedestalRecipeBuilder.pedestalCrafting(Ingredient.of(Items.CLAY_BALL), Ingredient.of(Items.PAPER), true, ArcaneItems.SPELL_TABLET.get(), 1, null))
                    .unlockedWith(ArcaneBlocks.PEDESTAL.get().asItem()).save();

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
                CompoundTag tag = new CompoundTag();
                tag.putString(NBTHelpers.KEY_SPELL, id.toString());
                helper.wrap(PedestalRecipeBuilder.pedestalCrafting(Ingredient.of(ArcaneItems.SPELL_TABLET.get()), Ingredient.of(item), true, ArcaneItems.SPELL_TABLET.get(), 1, tag))
                        .unlockedWith(item).save(id.withPrefix("spell_tablets/"));
            });
        }
    }

    private void oreProcessingLine(RecipeDataHelper helper, Item raw, Item crushed, Item purified, Item ingot) {
        helper.wrap(HammeringRecipeBuilder.hammering(Ingredient.of(raw), crushed, 1))
                .unlockedWith(raw).save(new ResourceLocation(ArcaneMod.MODID, "ore_processing/hammering/" + itemKey(raw).getPath()));

        helper.wrap(PurifyingRecipeBuilder.purifying(Ingredient.of(crushed), purified, 2))
                .unlockedWith(crushed).save(itemKey(crushed).withPrefix("ore_processing/purifying/"));

        helper.wrap(SimpleCookingRecipeBuilder.blasting(Ingredient.of(purified), RecipeCategory.MISC, ingot, 0.7f, 100))
                .unlockedWith(raw).save(itemKey(purified).withPrefix("ore_processing/blasting/"));
    }

    private ResourceLocation itemKey(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }
}
