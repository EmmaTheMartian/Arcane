package martian.arcane.datagen.server;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneTags;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.common.registry.ArcaneBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
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
    }
}
