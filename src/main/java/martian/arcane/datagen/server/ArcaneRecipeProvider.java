package martian.arcane.datagen.server;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneTags;
import martian.arcane.api.ListHelpers;
import martian.arcane.api.NBTHelpers;
import martian.arcane.api.recipe.RecipeOutput;
import martian.arcane.common.registry.ArcaneBlocks;
import martian.arcane.common.registry.ArcaneItems;
import martian.arcane.common.registry.ArcaneSpells;
import martian.arcane.datagen.builders.CleansingRecipeBuilder;
import martian.arcane.datagen.builders.HammeringRecipeBuilder;
import martian.arcane.datagen.builders.PedestalRecipeBuilder;
import martian.arcane.datagen.builders.PurifyingRecipeBuilder;
import martian.arcane.datagen.util.RecipeDataHelper;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
        helper.prefixesByProvider.put(CleansingRecipeBuilder.class, "cleansing");
        helper.prefixesByProvider.put(HammeringRecipeBuilder.class, "hammering");
        helper.prefixesByProvider.put(PurifyingRecipeBuilder.class, "purifying");
        helper.prefixesByProvider.put(PedestalRecipeBuilder.class, "pedestal");
        helper.prefixesByProvider.put(ShapelessRecipeBuilder.class, "shapeless");
        helper.prefixesByProvider.put(ShapedRecipeBuilder.class, "shaped");
        helper.prefixesByProvider.put(SimpleCookingRecipeBuilder.class, "cooking");

        final TagKey<Item>
                COPPER      = Tags.Items.INGOTS_COPPER,
                IRON        = Tags.Items.INGOTS_IRON,
                REDSTONE    = Tags.Items.DUSTS_REDSTONE,
                GLASS       = Tags.Items.GLASS_COLORLESS,
                STONE       = Tags.Items.STONE,
                OBSIDIAN    = Tags.Items.OBSIDIAN
        ;

        final Ingredient
                AURAGLASS           = ingOf(ArcaneBlocks.AURAGLASS.get().asItem()),
                AURAGLASS_DUST      = ingOf(ArcaneItems.AURAGLASS_DUST.get()),
                COPPER_CORE         = ingOf(ArcaneItems.COPPER_CORE.get()),
                LARIMAR_CORE        = ingOf(ArcaneItems.LARIMAR_CORE.get()),
                AURACHALCUM_CORE    = ingOf(ArcaneItems.AURACHALCUM_CORE.get()),
                ELDRITCH_CORE       = ingOf(ArcaneItems.ELDRITCH_CORE.get()),
                SPELL_CIRCLE_CORE   = ingOf(ArcaneItems.SPELL_CIRCLE_CORE.get()),
                POLISHED_LARIMAR    = ingOf(ArcaneItems.POLISHED_LARIMAR.get()),
                FADED_LARIMAR       = ingOf(ArcaneItems.FADED_POLISHED_LARIMAR.get()),
                AURACHALCUM         = ingOf(ArcaneItems.AURACHALCUM.get()),
                ELDRITCH_ALLOY      = ingOf(ArcaneItems.ELDRITCH_ALLOY.get())
        ;

        // Materials
        {
            helper.shapeless(ArcaneItems.RAW_AURACHALCUM.get(), 2, Map.of(
                    ingOf(ArcaneItems.POLISHED_IDOCRASE.get()), 1,
                    ingOf(OBSIDIAN), 2
            )).unlockedWith(ArcaneItems.POLISHED_IDOCRASE.get()).save();

            helper.shaped(ArcaneItems.COPPER_CORE.get())
                    .pattern(" G ", "GCG", " G ")
                    .define('G', GLASS)
                    .define('C', COPPER)
                    .unlockedWith(Items.COPPER_INGOT)
                    .save();

            helper.shaped(ArcaneItems.SPELL_CIRCLE_CORE.get())
                    .pattern(" A ", "ACA", " A ")
                    .define('A', AURAGLASS_DUST)
                    .define('C', AURACHALCUM_CORE)
                    .unlockedWith(ArcaneItems.AURACHALCUM_CORE.get())
                    .save();

            // Hammering Recipes
            helper.wrap(HammeringRecipeBuilder.hammering(AURAGLASS, ListHelpers.nonNullListOf(
                    new RecipeOutput.DataGenHolder(ArcaneItems.AURAGLASS_SHARD.get(), 1, 1),
                    new RecipeOutput.DataGenHolder(ArcaneItems.AURAGLASS_SHARD.get(), 1, 0.5F)
            ))).unlockedWith(ArcaneBlocks.AURAGLASS.get().asItem()).save();

            helper.wrap(HammeringRecipeBuilder.hammering(ingOf(ArcaneItems.AURAGLASS_SHARD.get()), ListHelpers.nonNullListOf(
                    new RecipeOutput.DataGenHolder(ArcaneItems.AURAGLASS_DUST.get(), 1, 1),
                    new RecipeOutput.DataGenHolder(ArcaneItems.AURAGLASS_DUST.get(), 1, 0.5F)
            ))).unlockedWith(ArcaneItems.AURAGLASS_SHARD.get()).save();

            // Pedestal Interaction Recipes
            helper.wrap(PedestalRecipeBuilder.simpleRecipe(ingOf(COPPER), AURAGLASS, ArcaneItems.COPPER_CORE.get(), 1))
                    .unlockedWith(Items.COPPER_INGOT).save();
            helper.wrap(PedestalRecipeBuilder.simpleRecipe(ingOf(ArcaneItems.POLISHED_LARIMAR.get()), AURAGLASS, ArcaneItems.LARIMAR_CORE.get(), 1))
                    .unlockedWith(ArcaneItems.POLISHED_LARIMAR.get()).save();
            helper.wrap(PedestalRecipeBuilder.simpleRecipe(ingOf(ArcaneItems.AURACHALCUM.get()), AURAGLASS, ArcaneItems.AURACHALCUM_CORE.get(), 1))
                    .unlockedWith(ArcaneItems.AURACHALCUM.get()).save();
            helper.wrap(PedestalRecipeBuilder.simpleRecipe(ingOf(ArcaneItems.ELDRITCH_ALLOY.get()), AURAGLASS, ArcaneItems.ELDRITCH_CORE.get(), 1))
                    .unlockedWith(ArcaneItems.ELDRITCH_ALLOY.get()).save();

            // Gems
            cutAndPolish(helper, ArcaneItems.RAW_LARIMAR.get(), ArcaneItems.CUT_LARIMAR.get(), ArcaneItems.POLISHED_LARIMAR.get());
            cutAndPolish(helper, ArcaneItems.FADED_RAW_LARIMAR.get(), ArcaneItems.FADED_CUT_LARIMAR.get(), ArcaneItems.FADED_POLISHED_LARIMAR.get());
            cutAndPolish(helper, ArcaneItems.RAW_IDOCRASE.get(), ArcaneItems.CUT_IDOCRASE.get(), ArcaneItems.POLISHED_IDOCRASE.get());

            // Storage Blocks
            storageBlock(helper, FADED_LARIMAR, ArcaneBlocks.FADED_LARIMAR_BLOCK.get().asItem());
            storageBlock(helper, POLISHED_LARIMAR, ArcaneBlocks.LARIMAR_BLOCK.get().asItem());
            storageBlock(helper, AURACHALCUM, ArcaneBlocks.AURACHALCUM_BLOCK.get().asItem());
        }

        // Misc Utility Recipes
        {
            helper.wrap(HammeringRecipeBuilder.hammering(ingOf(STONE), Items.COBBLESTONE, 1))
                    .unlockedWith(Items.STONE).save();

            helper.wrap(HammeringRecipeBuilder.hammering(ingOf(Items.COBBLESTONE), Items.GRAVEL, 1))
                    .unlockedWith(Items.COBBLESTONE).save();

            helper.wrap(HammeringRecipeBuilder.hammering(ingOf(Items.GRAVEL), Items.SAND, 1))
                    .unlockedWith(Items.GRAVEL).save();

            helper.wrap(CleansingRecipeBuilder.cleansing(ingOf(Items.GRAVEL), Items.FLINT, 1))
                    .unlockedWith(Items.GRAVEL).save();
        }

        // Blocks
        {
            helper.defaultCategory = RecipeCategory.REDSTONE;

            helper.shaped(ArcaneBlocks.HEAT_COLLECTOR.get().asItem())
                    .pattern("GIG", "FCF", "GIG")
                    .define('G', GLASS)
                    .define('I', COPPER)
                    .define('F', ingOf(Items.FLINT))
                    .define('C', COPPER_CORE)
                    .unlockedWith(ArcaneItems.COPPER_CORE.get())
                    .save();

            helper.shaped(ArcaneBlocks.AQUA_COLLECTOR.get().asItem())
                    .pattern("GIG", "ICI", "GWG")
                    .define('G', GLASS)
                    .define('I', COPPER)
                    .define('W', ingOf(Items.WATER_BUCKET))
                    .define('C', COPPER_CORE)
                    .unlockedWith(ArcaneItems.COPPER_CORE.get())
                    .save();

            helper.shaped(ArcaneBlocks.PEDESTAL.get().asItem())
                    .pattern("CBC", " S ", "SSS")
                    .define('C', COPPER)
                    .define('S', STONE)
                    .define('B', COPPER_CORE)
                    .unlockedWith(ArcaneItems.COPPER_CORE.get())
                    .save();

            // Machines
            {
                helper.shaped(ArcaneBlocks.AURA_INFUSER.get().asItem())
                        .pattern("BCB", " O ", "OOO")
                        .define('B', COPPER)
                        .define('C', COPPER_CORE)
                        .define('O', OBSIDIAN)
                        .unlockedWith(ArcaneItems.COPPER_CORE.get())
                        .save();

                makeBasin(helper, ArcaneBlocks.COPPER_AURA_BASIN.get().asItem(), COPPER_CORE, Ingredient.of(COPPER));
                makeBasin(helper, ArcaneBlocks.LARIMAR_AURA_BASIN.get().asItem(), LARIMAR_CORE, POLISHED_LARIMAR);
                makeBasin(helper, ArcaneBlocks.AURACHALCUM_AURA_BASIN.get().asItem(), AURACHALCUM_CORE, AURACHALCUM);

                makeExtractor(helper, ArcaneBlocks.COPPER_AURA_EXTRACTOR.get().asItem(), COPPER_CORE, Ingredient.of(STONE));
                makeExtractor(helper, ArcaneBlocks.LARIMAR_AURA_EXTRACTOR.get().asItem(), LARIMAR_CORE, Ingredient.of(STONE));
                makeExtractor(helper, ArcaneBlocks.AURACHALCUM_AURA_EXTRACTOR.get().asItem(), AURACHALCUM_CORE, Ingredient.of(OBSIDIAN));

                makeInserter(helper, ArcaneBlocks.COPPER_AURA_INSERTER.get().asItem(), COPPER_CORE, Ingredient.of(STONE));
                makeInserter(helper, ArcaneBlocks.LARIMAR_AURA_INSERTER.get().asItem(), LARIMAR_CORE, Ingredient.of(STONE));
                makeInserter(helper, ArcaneBlocks.AURACHALCUM_AURA_INSERTER.get().asItem(), AURACHALCUM_CORE, Ingredient.of(OBSIDIAN));

                // Alt recipes for extractors and inserters
                helper.swappable(ArcaneBlocks.COPPER_AURA_EXTRACTOR.get().asItem(), ArcaneBlocks.COPPER_AURA_INSERTER.get().asItem());
                helper.swappable(ArcaneBlocks.LARIMAR_AURA_EXTRACTOR.get().asItem(), ArcaneBlocks.LARIMAR_AURA_INSERTER.get().asItem());
                helper.swappable(ArcaneBlocks.AURACHALCUM_AURA_EXTRACTOR.get().asItem(), ArcaneBlocks.AURACHALCUM_AURA_INSERTER.get().asItem());
            }
        }

        // Tools
        {
            helper.defaultCategory = RecipeCategory.TOOLS;

            helper.shaped(ArcaneItems.AURA_WRENCH.get())
                    .pattern("C C", " B ", " C ")
                    .define('C', COPPER)
                    .define('B', COPPER_CORE)
                    .unlockedWith(ArcaneItems.COPPER_CORE.get())
                    .save();

            helper.shaped(ArcaneItems.AURA_CONFIGURATOR.get())
                    .pattern("B B", " I ", " B ")
                    .define('B', IRON)
                    .define('I', COPPER_CORE)
                    .unlockedWith(ArcaneItems.COPPER_CORE.get())
                    .save();

            helper.shaped(ArcaneItems.GEM_SAW.get())
                    .pattern("  B", " BS", "BS ")
                    .define('B', COPPER)
                    .define('S', Tags.Items.NUGGETS_IRON)
                    .unlockedWith(Items.COPPER_INGOT)
                    .save();

            helper.shaped(ArcaneItems.AURAOMETER.get())
                    .pattern("R", "C", "B")
                    .define('R', REDSTONE)
                    .define('B', COPPER_CORE)
                    .define('C', COPPER)
                    .unlockedWith(ArcaneItems.COPPER_CORE.get())
                    .save();

            helper.shapeless(ArcaneItems.ARCANE_BLEACH.get(), Map.of(
                    ingOf(Items.GLASS_BOTTLE), 1,
                    ingOf(Items.BONE_MEAL), 1,
                    ingOf(ArcaneItems.AURAGLASS_DUST.get()), 1
            )).unlockedWith(ArcaneItems.AURAGLASS_DUST.get()).save();

            helper.shaped(ArcaneItems.SPELL_CHALK.get())
                    .pattern(" CB", "CSC", "BC ")
                    .define('C', ingOf(Items.CLAY_BALL))
                    .define('B', ingOf(Items.BONE_MEAL))
                    .define('S', SPELL_CIRCLE_CORE)
                    .unlockedWith(ArcaneItems.SPELL_CIRCLE_CORE.get())
                    .save();

            helper.shaped(ArcaneItems.ENDERPACK.get())
                    .pattern(" P ")
                    .pattern("LEL")
                    .pattern("PPP")
                    .define('P', POLISHED_LARIMAR)
                    .define('L', Tags.Items.LEATHER)
                    .define('E', ingOf(Items.ENDER_CHEST))
                    .unlockedWith(ArcaneItems.POLISHED_LARIMAR.get())
                    .save();

            // Auraglass Bottles
            {
                helper.shaped(ArcaneItems.AURAGLASS_BOTTLE.get())
                        .pattern("A A", " A ")
                        .define('A', AURAGLASS)
                        .unlockedWith(ArcaneBlocks.AURAGLASS.get().asItem())
                        .save();

                helper.shaped(ArcaneItems.MEDIUM_AURAGLASS_BOTTLE.get())
                        .pattern("BAB", " B ")
                        .define('A', ingOf(ArcaneItems.AURAGLASS_BOTTLE.get()))
                        .define('B', POLISHED_LARIMAR)
                        .unlockedWith(ArcaneItems.AURAGLASS_BOTTLE.get())
                        .save();

                helper.shaped(ArcaneItems.LARGE_AURAGLASS_BOTTLE.get())
                        .pattern("BAB", " B ")
                        .define('A', ingOf(ArcaneItems.MEDIUM_AURAGLASS_BOTTLE.get()))
                        .define('B', AURACHALCUM)
                        .unlockedWith(ArcaneItems.MEDIUM_AURAGLASS_BOTTLE.get())
                        .save();

                helper.shaped(ArcaneItems.EXTREME_AURAGLASS_BOTTLE.get())
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
                wands.forEach((wandItem, output) -> helper.shaped(output)
                        .pattern("  P", " P ", "C  ")
                        .define('C', COPPER_CORE)
                        .define('P', ingOf(wandItem))
                        .unlockedWith(ArcaneItems.COPPER_CORE.get())
                        .save(itemKey(output).withPrefix("wands/")));

                // Advanced Wands
                helper.shaped(ArcaneItems.WAND_LARIMAR.get())
                        .pattern("  B", " W ", "C  ")
                        .define('B', POLISHED_LARIMAR)
                        .define('C', LARIMAR_CORE)
                        .define('W', ArcaneTags.BASIC_WANDS)
                        .unlockedWith(ArcaneItems.LARIMAR_CORE.get())
                        .save(itemKey(ArcaneItems.WAND_LARIMAR.get()).withPrefix("wands/"));

                // Mythical Wands
                helper.shaped(ArcaneItems.WAND_AURACHALCUM.get())
                        .pattern("  D", " W ", "C  ")
                        .define('D', AURACHALCUM)
                        .define('W', ArcaneTags.ADVANCED_WANDS)
                        .define('C', AURACHALCUM_CORE)
                        .unlockedWith(ArcaneItems.AURACHALCUM_CORE.get())
                        .save(itemKey(ArcaneItems.WAND_AURACHALCUM.get()).withPrefix("wands/"));

                helper.shaped(ArcaneItems.WAND_ELDRITCH.get())
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
            oreProcessingLine(helper, Items.RAW_COPPER, ArcaneItems.CRUSHED_RAW_COPPER.get(), ArcaneItems.PURIFIED_RAW_COPPER.get(), Items.COPPER_INGOT);
            oreProcessingLine(helper, Items.RAW_IRON, ArcaneItems.CRUSHED_RAW_IRON.get(), ArcaneItems.PURIFIED_RAW_IRON.get(), Items.IRON_INGOT);
            oreProcessingLine(helper, Items.RAW_GOLD, ArcaneItems.CRUSHED_RAW_GOLD.get(), ArcaneItems.PURIFIED_RAW_GOLD.get(), Items.GOLD_INGOT);
            helper.moveNamespacesToModid = true;
        }

        // Spell Tablets
        {
            helper.defaultCategory = RecipeCategory.TOOLS;

            helper.wrap(PedestalRecipeBuilder.simpleRecipe(ingOf(Items.CLAY_BALL), ingOf(Items.PAPER), ArcaneItems.SPELL_TABLET.get(), 1))
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
                RecipeOutput.DataGenHolder holder = new RecipeOutput.DataGenHolder(new ItemStack(ArcaneItems.SPELL_TABLET.get(), 1, tag), 1);
                helper.wrap(PedestalRecipeBuilder.simpleRecipe(ingOf(ArcaneItems.SPELL_TABLET.get()), ingOf(item), holder))
                        .unlockedWith(item).save(id.withPrefix("spell_tablets/"));
            });
        }
    }

    private void storageBlock(RecipeDataHelper helper, Ingredient of, Item output) {
        helper.shaped(output)
                .pattern("AAA", "AAA", "AAA")
                .define('A', of)
                .unlockedWith(of.getItems()[0].getItem())
                .save();
    }

    private void cutAndPolish(RecipeDataHelper helper, Item raw, Item cut, Item polished) {
        helper.shapeless(cut, 1)
                .requires(raw)
                .requires(ArcaneItems.GEM_SAW.get())
                .unlockedWith(raw)
                .save();

        helper.shapeless(polished, 1)
                .requires(cut)
                .requires(ItemTags.SAND, 2)
                .unlockedWith(cut)
                .save();
    }

    private void makeBasin(RecipeDataHelper helper, Item result, Ingredient core, Ingredient base) {
        helper.shaped(result)
                .pattern("B B", "B B", "BCB")
                .define('B', base)
                .define('C', core)
                .unlockedWith(core.getItems()[0].getItem())
                .save();
    }

    private void makeInserter(RecipeDataHelper helper, Item result, Ingredient core, Ingredient base) {
        helper.shaped(result)
                .pattern(" B ", "SSS")
                .define('B', core)
                .define('S', base)
                .unlockedWith(core.getItems()[0].getItem())
                .save();
    }

    private void makeExtractor(RecipeDataHelper helper, Item result, Ingredient core, Ingredient base) {
        helper.shaped(result)
                .pattern("SSS", " B ")
                .define('B', core)
                .define('S', base)
                .unlockedWith(core.getItems()[0].getItem())
                .save();
    }

    private void oreProcessingLine(RecipeDataHelper helper, Item raw, Item crushed, Item purified, Item ingot) {
        helper.wrap(HammeringRecipeBuilder.hammering(ingOf(raw), crushed, 1))
                .unlockedWith(raw).save(new ResourceLocation(ArcaneMod.MODID, "ore_processing/" + itemKey(raw).getPath()));

        helper.wrap(PurifyingRecipeBuilder.purifying(ingOf(crushed), purified, 2))
                .unlockedWith(crushed).save(itemKey(crushed).withPrefix("ore_processing/"));

        helper.wrap(SimpleCookingRecipeBuilder.blasting(ingOf(purified), RecipeCategory.MISC, ingot, 0.7f, 100))
                .unlockedWith(raw).save(itemKey(purified).withPrefix("ore_processing/"));
    }

    private ResourceLocation itemKey(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }

    private Ingredient ingOf(Item item) {
        return Ingredient.of(item);
    }

    private Ingredient ingOf(TagKey<Item> item) {
        return Ingredient.of(item);
    }
}
