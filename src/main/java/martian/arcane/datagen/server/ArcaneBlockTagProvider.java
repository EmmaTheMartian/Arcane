package martian.arcane.datagen.server;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneTags;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.common.ArcaneContent;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static martian.arcane.common.ArcaneContent.*;

public class ArcaneBlockTagProvider extends BlockTagsProvider {
    public ArcaneBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ArcaneMod.MODID, existingFileHelper);
    }

    @Override
    public void addTags(@NotNull HolderLookup.Provider provider) {
        IntrinsicTagAppender<Block>
                pickaxeBreakable = tag(BlockTags.MINEABLE_WITH_PICKAXE),
                auraWrenchBreakable = tag(ArcaneTags.AURA_WRENCH_BREAKABLE);

        ArcaneContent.BLOCKS.getEntries().forEach(block -> {
            pickaxeBreakable.add(block.get());

            if (block.get() instanceof AbstractAuraMachine) {
                auraWrenchBreakable.add(block.get());
            }
        });

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(LARIMAR_ORE.get())
                .add(FADING_LARIMAR_ORE.get())
                .add(FADED_LARIMAR_ORE.get())
                .add(DEEPSLATE_LARIMAR_ORE.get())
                .add(FADING_DEEPSLATE_LARIMAR_ORE.get())
                .add(FADED_DEEPSLATE_LARIMAR_ORE.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(LARIMAR_BLOCK.get())
                .add(FADING_LARIMAR_ORE.get())
                .add(FADED_LARIMAR_BLOCK.get())
                .add(AURACHALCUM_BLOCK.get());

        tag(ArcaneTags.AURA_BASINS)
                .add(AURA_BASIN.block().get());

        tag(ArcaneTags.BLOCKS_AURA_FLOW)
                .add(Blocks.COPPER_BLOCK)
                .add(Blocks.WAXED_COPPER_BLOCK)
                .add(Blocks.CUT_COPPER)
                .add(Blocks.WAXED_CUT_COPPER)
                .add(Blocks.RAW_COPPER_BLOCK)
                .add(LARIMAR_BLOCK.get())
                .add(FADING_LARIMAR_BLOCK.get())
                .add(FADED_LARIMAR_BLOCK.get())
                .add(AURACHALCUM_BLOCK.get());

        tag(ArcaneTags.MIXING_CAULDRONS)
                .add(Blocks.CAULDRON)
                .add(Blocks.WATER_CAULDRON)
                .add(Blocks.LAVA_CAULDRON)
                .add(Blocks.POWDER_SNOW_CAULDRON);
    }
}
