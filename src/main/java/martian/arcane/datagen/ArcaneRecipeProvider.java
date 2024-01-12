package martian.arcane.datagen;

import martian.arcane.registry.ArcaneBlocks;
import martian.arcane.registry.ArcaneItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

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
                .pattern("GBG")
                .pattern("B B")
                .pattern("GBG")
                .define('B', ArcaneItems.BLUE_GOLD.get())
                .define('G', Items.GLASS)
                .unlockedBy("has_item", has(ArcaneItems.BLUE_GOLD.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ArcaneItems.AURACHALCUM_CORE.get())
                .pattern("GAG")
                .pattern("A A")
                .pattern("GAG")
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

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ArcaneBlocks.AURA_EXTRACTOR.get().asItem())
                .pattern(" B ")
                .pattern("SSS")
                .define('B', ArcaneItems.BLUE_GOLD.get())
                .define('S', Tags.Items.STONE)
                .unlockedBy("has_item", has(ArcaneItems.BLUE_GOLD.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ArcaneBlocks.AURA_INSERTER.get().asItem())
                .pattern("SSS")
                .pattern(" B ")
                .define('B', ArcaneItems.BLUE_GOLD.get())
                .define('S', Tags.Items.STONE)
                .unlockedBy("has_item", has(ArcaneItems.BLUE_GOLD.get()))
                .save(writer);

        // Aura Items
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ArcaneItems.AURA_WRENCH.get())
                .pattern("B B")
                .pattern(" B ")
                .pattern(" B ")
                .define('B', ArcaneItems.BLUE_GOLD.get())
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
    }
}
