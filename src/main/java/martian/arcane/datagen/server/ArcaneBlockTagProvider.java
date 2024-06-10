package martian.arcane.datagen.server;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneTags;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.common.registry.ArcaneBlocks;
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

public class ArcaneBlockTagProvider extends BlockTagsProvider {
    public ArcaneBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ArcaneMod.MODID, existingFileHelper);
    }

    @Override
    public void addTags(@NotNull HolderLookup.Provider provider) {
        IntrinsicTagAppender<Block>
                pickaxeBreakable = tag(BlockTags.MINEABLE_WITH_PICKAXE),
                auraWrenchBreakable = tag(ArcaneTags.AURA_WRENCH_BREAKABLE);

        ArcaneBlocks.BLOCKS.getEntries().forEach(block -> {
            pickaxeBreakable.add(block.get());

            if (block.get() instanceof AbstractAuraMachine) {
                auraWrenchBreakable.add(block.get());
            }
        });

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ArcaneBlocks.LARIMAR_ORE.get())
                .add(ArcaneBlocks.FADING_LARIMAR_ORE.get())
                .add(ArcaneBlocks.FADED_LARIMAR_ORE.get())
                .add(ArcaneBlocks.DEEPSLATE_LARIMAR_ORE.get())
                .add(ArcaneBlocks.FADING_DEEPSLATE_LARIMAR_ORE.get())
                .add(ArcaneBlocks.FADED_DEEPSLATE_LARIMAR_ORE.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ArcaneBlocks.LARIMAR_BLOCK.get())
                .add(ArcaneBlocks.FADING_LARIMAR_ORE.get())
                .add(ArcaneBlocks.FADED_LARIMAR_BLOCK.get())
                .add(ArcaneBlocks.AURACHALCUM_BLOCK.get());

        tag(ArcaneTags.AURA_BASINS)
                .add(ArcaneBlocks.AURA_BASIN.get());
//                .add(ArcaneBlocks.COPPER_AURA_BASIN.get())
//                .add(ArcaneBlocks.LARIMAR_AURA_BASIN.get())
//                .add(ArcaneBlocks.AURACHALCUM_AURA_BASIN.get());

        tag(ArcaneTags.BLOCKS_AURA_FLOW)
                .add(Blocks.COPPER_BLOCK)
                .add(Blocks.WAXED_COPPER_BLOCK)
                .add(Blocks.CUT_COPPER)
                .add(Blocks.WAXED_CUT_COPPER)
                .add(Blocks.RAW_COPPER_BLOCK)
                .add(ArcaneBlocks.LARIMAR_BLOCK.get())
                .add(ArcaneBlocks.FADING_LARIMAR_BLOCK.get())
                .add(ArcaneBlocks.FADED_LARIMAR_BLOCK.get())
                .add(ArcaneBlocks.AURACHALCUM_BLOCK.get());
    }
}
