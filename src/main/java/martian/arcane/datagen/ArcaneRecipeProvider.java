package martian.arcane.datagen;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneTags;
import martian.arcane.registry.ArcaneBlocks;
import martian.arcane.registry.ArcaneItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
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
        // Materials
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ArcaneItems.BLUE_GOLD.get(), 4)
                .requires(Items.GOLD_INGOT, 3)
                .requires(Items.IRON_INGOT, 1)
                .unlockedBy("has_item", has(Items.GOLD_INGOT))
                .save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ArcaneItems.RAW_AURACHALCUM.get(), 2)
                .requires(ArcaneItems.BLUE_GOLD.get())
                .requires(Items.OBSIDIAN)
                .unlockedBy("has_item", has(ArcaneItems.BLUE_GOLD.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ArcaneItems.BLUE_GOLD_CORE.get())
                .pattern(" G ")
                .pattern("GBG")
                .pattern(" G ")
                .define('B', ArcaneItems.BLUE_GOLD.get())
                .define('G', Items.GLASS)
                .unlockedBy("has_item", has(ArcaneItems.BLUE_GOLD.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ArcaneItems.AURACHALCUM_CORE.get())
                .pattern(" G ")
                .pattern("GAG")
                .pattern(" G ")
                .define('A', ArcaneItems.AURACHALCUM.get())
                .define('G', ArcaneBlocks.AURAGLASS.get().asItem())
                .unlockedBy("has_item", has(ArcaneItems.AURACHALCUM.get()))
                .save(writer);

        // Aura Machines/Blocks
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ArcaneBlocks.AURA_BASIN.get().asItem())
                .pattern("B B")
                .pattern("B B")
                .pattern("BBB")
                .define('B', ArcaneItems.BLUE_GOLD.get())
                .unlockedBy("has_item", has(ArcaneItems.BLUE_GOLD.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ArcaneBlocks.AURA_INFUSER.get().asItem())
                .pattern("BCB")
                .pattern("OOO")
                .define('B', ArcaneItems.BLUE_GOLD.get())
                .define('C', ArcaneItems.BLUE_GOLD_CORE.get())
                .define('O', Items.OBSIDIAN)
                .unlockedBy("has_item", has(ArcaneItems.BLUE_GOLD.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ArcaneBlocks.AURA_EXTRACTOR.get().asItem())
                .pattern(" B ")
                .pattern("SSS")
                .define('B', ArcaneItems.BLUE_GOLD.get())
                .define('S', Tags.Items.STONE)
                .unlockedBy("has_item", has(ArcaneItems.BLUE_GOLD.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ArcaneBlocks.IGNIS_COLLECTOR.get().asItem())
                .pattern("GIG")
                .pattern("FCF")
                .pattern("GIG")
                .define('G', Items.GLASS)
                .define('I', Items.IRON_INGOT)
                .define('F', Items.FLINT)
                .define('C', ArcaneItems.BLUE_GOLD_CORE.get())
                .unlockedBy("has_item", has(ArcaneItems.BLUE_GOLD_CORE.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ArcaneBlocks.AQUA_COLLECTOR.get().asItem())
                .pattern("GIG")
                .pattern("ICI")
                .pattern("GWG")
                .define('G', Items.GLASS)
                .define('I', Items.IRON_INGOT)
                .define('W', Items.WATER_BUCKET)
                .define('C', ArcaneItems.BLUE_GOLD_CORE.get())
                .unlockedBy("has_item", has(ArcaneItems.BLUE_GOLD_CORE.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ArcaneBlocks.AURA_INSERTER.get().asItem())
                .pattern("SSS")
                .pattern(" B ")
                .define('B', ArcaneItems.BLUE_GOLD.get())
                .define('S', Tags.Items.STONE)
                .unlockedBy("has_item", has(ArcaneItems.BLUE_GOLD.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ArcaneBlocks.IMPROVED_AURA_EXTRACTOR.get().asItem())
                .pattern(" E ")
                .pattern("AAA")
                .define('E', ArcaneBlocks.AURA_EXTRACTOR.get().asItem())
                .define('A', ArcaneItems.AURACHALCUM.get())
                .unlockedBy("has_item", has(ArcaneItems.AURACHALCUM.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ArcaneBlocks.IMPROVED_AURA_INSERTER.get().asItem())
                .pattern("AAA")
                .pattern(" E ")
                .define('E', ArcaneBlocks.AURA_INSERTER.get().asItem())
                .define('A', ArcaneItems.AURACHALCUM.get())
                .unlockedBy("has_item", has(ArcaneItems.AURACHALCUM.get()))
                .save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, ArcaneBlocks.AURA_EXTRACTOR.get().asItem())
                .requires(ArcaneBlocks.AURA_INFUSER.get().asItem())
                .unlockedBy("has_item", has(ArcaneBlocks.AURA_INFUSER.get()))
                .save(writer, ArcaneMod.id("aura_extractor_alt"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, ArcaneBlocks.AURA_INSERTER.get().asItem())
                .requires(ArcaneBlocks.AURA_EXTRACTOR.get().asItem())
                .unlockedBy("has_item", has(ArcaneBlocks.AURA_EXTRACTOR.get()))
                .save(writer, ArcaneMod.id("aura_inserter_alt"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, ArcaneBlocks.IMPROVED_AURA_EXTRACTOR.get().asItem())
                .requires(ArcaneBlocks.IMPROVED_AURA_INSERTER.get().asItem())
                .unlockedBy("has_item", has(ArcaneBlocks.IMPROVED_AURA_INSERTER.get()))
                .save(writer, ArcaneMod.id("improved_aura_extractor_alt"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, ArcaneBlocks.IMPROVED_AURA_INSERTER.get().asItem())
                .requires(ArcaneBlocks.IMPROVED_AURA_EXTRACTOR.get().asItem())
                .unlockedBy("has_item", has(ArcaneBlocks.IMPROVED_AURA_EXTRACTOR.get()))
                .save(writer, ArcaneMod.id("improved_aura_inserter_alt"));

        // Aura Items
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ArcaneItems.AURA_WRENCH.get())
                .pattern("B B")
                .pattern(" B ")
                .pattern(" B ")
                .define('B', ArcaneItems.BLUE_GOLD.get())
                .unlockedBy("has_item", has(ArcaneItems.BLUE_GOLD.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ArcaneItems.AURA_CONFIGURATOR.get())
                .pattern("B B")
                .pattern(" I ")
                .pattern(" B ")
                .define('B', ArcaneItems.BLUE_GOLD.get())
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_item", has(ArcaneItems.BLUE_GOLD.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ArcaneItems.AURAOMETER.get())
                .pattern(" R ")
                .pattern(" B ")
                .pattern(" B ")
                .define('R', Items.REDSTONE)
                .define('B', ArcaneItems.BLUE_GOLD.get())
                .unlockedBy("has_item", has(ArcaneItems.BLUE_GOLD.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ArcaneItems.AURAGLASS_BOTTLE.get())
                .pattern("A A")
                .pattern(" A ")
                .define('A', ArcaneBlocks.AURAGLASS.get().asItem())
                .unlockedBy("has_item", has(ArcaneBlocks.AURAGLASS.get().asItem()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ArcaneItems.MEDIUM_AURAGLASS_BOTTLE.get())
                .pattern("BAB")
                .pattern(" B ")
                .define('A', ArcaneItems.AURAGLASS_BOTTLE.get())
                .define('B', ArcaneItems.BLUE_GOLD.get())
                .unlockedBy("has_item", has(ArcaneItems.AURAGLASS_BOTTLE.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ArcaneItems.LARGE_AURAGLASS_BOTTLE.get())
                .pattern("BAB")
                .pattern(" B ")
                .define('A', ArcaneItems.MEDIUM_AURAGLASS_BOTTLE.get())
                .define('B', ArcaneItems.AURACHALCUM.get())
                .unlockedBy("has_item", has(ArcaneItems.MEDIUM_AURAGLASS_BOTTLE.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ArcaneItems.EXTREME_AURAGLASS_BOTTLE.get())
                .pattern("BAB")
                .pattern(" B ")
                .define('A', ArcaneItems.LARGE_AURAGLASS_BOTTLE.get())
                .define('B', ArcaneItems.AURACHALCUM_CORE.get())
                .unlockedBy("has_item", has(ArcaneItems.LARGE_AURAGLASS_BOTTLE.get()))
                .save(writer);

        { // Basic Wands
            Map<Item, Item> wands = new HashMap<>();
            wands.put(Items.ACACIA_PLANKS, ArcaneItems.WAND_ACACIA_AURA.get());
            wands.put(Items.BAMBOO_PLANKS, ArcaneItems.WAND_BAMBOO_AURA.get());
            wands.put(Items.BIRCH_PLANKS, ArcaneItems.WAND_BIRCH.get());
            wands.put(Items.CHERRY_PLANKS, ArcaneItems.WAND_CHERRY.get());
            wands.put(Items.DARK_OAK_PLANKS, ArcaneItems.WAND_DARK_OAK.get());
            wands.put(Items.JUNGLE_PLANKS, ArcaneItems.WAND_JUNGLE.get());
            wands.put(Items.MANGROVE_PLANKS, ArcaneItems.WAND_MANGROVE.get());
            wands.put(Items.OAK_PLANKS, ArcaneItems.WAND_OAK.get());
            wands.put(Items.SPRUCE_PLANKS, ArcaneItems.WAND_SPRUCE.get());
            wands.put(Items.WARPED_PLANKS, ArcaneItems.WAND_WARPED.get());
            wands.put(Items.CRIMSON_PLANKS, ArcaneItems.WAND_CRIMSON.get());
            wands.forEach((wandItem, output) -> wandRecipe(ArcaneItems.BLUE_GOLD_CORE.get(), wandItem, output, writer));
        }

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ArcaneItems.WAND_BLUE_GOLD.get())
                .pattern("  D")
                .pattern(" W ")
                .pattern("C  ")
                .define('D', Items.DIAMOND)
                .define('W', ArcaneTags.BASIC_WANDS)
                .define('C', ArcaneItems.AURACHALCUM_CORE.get())
                .unlockedBy("has_item", has(ArcaneItems.AURACHALCUM_CORE.get()))
                .save(writer);
    }

    private void wandRecipe(Item coreItem, Item stickItem, Item output, @NotNull Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output)
                .pattern("  P")
                .pattern(" P ")
                .pattern("C  ")
                .define('C', coreItem)
                .define('P', stickItem)
                .unlockedBy("has_item", has(coreItem))
                .save(writer);
    }
}
