package martian.arcane.datagen.server;

import martian.arcane.common.ArcaneContent;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static martian.arcane.common.ArcaneContent.*;

public class ArcaneLootTableProvider extends LootTableProvider {
    public ArcaneLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK)
        ), registries);
    }

    private static class BlockLoot extends BlockLootSubProvider {
        protected BlockLoot() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        @NotNull
        protected Iterable<Block> getKnownBlocks() {
            return ArcaneContent.BLOCKS.getEntries()
                    .stream()
                    .map(it -> (Block) it.value())
                    .toList();
        }

        @Override
        protected void generate() {
            // General Blocks
            dropSelf(SOUL_MAGMA.get());

            // Machines
            dropSelf(AURA_CONNECTOR.block().get());
            dropSelf(AURA_BASIN.block().get());
            dropSelf(PEDESTAL.block().get());
            dropSelf(AURA_INFUSER.block().get());
            dropSelf(HEAT_COLLECTOR.block().get());
            dropSelf(AQUA_COLLECTOR.block().get());

            // Ores
            ore(LARIMAR_ORE.get(), LARIMAR.rough().get());
            ore(FADING_LARIMAR_ORE.get(), LARIMAR.rough().get());
            ore(FADED_LARIMAR_ORE.get(), FADED_LARIMAR.rough().get());
            ore(DEEPSLATE_LARIMAR_ORE.get(), LARIMAR.rough().get());
            ore(FADING_DEEPSLATE_LARIMAR_ORE.get(), LARIMAR.rough().get());
            ore(FADED_DEEPSLATE_LARIMAR_ORE.get(), FADED_LARIMAR.rough().get());
            ore(IDOCRASE_ORE.get(), IDOCRASE.rough().get());
            ore(DEEPSLATE_IDOCRASE_ORE.get(), IDOCRASE.rough().get());
            ore(NETHER_IDOCRASE_ORE.get(), IDOCRASE.rough().get());
            ore(BLACKSTONE_IDOCRASE_ORE.get(), IDOCRASE.rough().get());

            // Storage Blocks
            dropSelf(LARIMAR_BLOCK.get());
            dropSelf(FADING_LARIMAR_BLOCK.get());
            dropSelf(FADED_LARIMAR_BLOCK.get());
            dropSelf(AURACHALCUM_BLOCK.get());
        }

        private void ore(Block block, Item item) {
            add(block, b -> createOreDrop(block, item));
        }

        @Override
        @ParametersAreNonnullByDefault
        public void generate(HolderLookup.Provider registries,  BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            generate();
            map.forEach(consumer);
        }
    }
}
