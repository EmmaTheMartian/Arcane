package martian.arcane.datagen.server;

import martian.arcane.common.registry.ArcaneBlocks;
import martian.arcane.common.registry.ArcaneItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class ArcaneLootTableProvider extends LootTableProvider {
    public ArcaneLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK)
        ));
    }

    private static class BlockLoot extends BlockLootSubProvider {
        protected BlockLoot() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        @NotNull
        protected Iterable<Block> getKnownBlocks() {
            return ArcaneBlocks.BLOCKS.getEntries()
                    .stream()
                    .flatMap(RegistryObject::stream)
                    ::iterator;
        }

        @Override
        protected void generate() {
            // General Blocks
            dropSelf(ArcaneBlocks.SOUL_MAGMA.get());

            // Machines
            dropSelf(ArcaneBlocks.COPPER_AURA_EXTRACTOR.get());
            dropSelf(ArcaneBlocks.LARIMAR_AURA_EXTRACTOR.get());
            dropSelf(ArcaneBlocks.AURACHALCUM_AURA_EXTRACTOR.get());

            dropSelf(ArcaneBlocks.COPPER_AURA_INSERTER.get());
            dropSelf(ArcaneBlocks.LARIMAR_AURA_INSERTER.get());
            dropSelf(ArcaneBlocks.AURACHALCUM_AURA_INSERTER.get());

            dropSelf(ArcaneBlocks.COPPER_AURA_BASIN.get());
            dropSelf(ArcaneBlocks.LARIMAR_AURA_BASIN.get());
            dropSelf(ArcaneBlocks.AURACHALCUM_AURA_BASIN.get());

            dropSelf(ArcaneBlocks.PEDESTAL.get());
            dropSelf(ArcaneBlocks.AURA_INFUSER.get());

            // Generators
            dropSelf(ArcaneBlocks.IGNIS_COLLECTOR.get());
            dropSelf(ArcaneBlocks.AQUA_COLLECTOR.get());

            // Ores
            ore(ArcaneBlocks.LARIMAR_ORE.get(), ArcaneItems.RAW_LARIMAR.get());
            ore(ArcaneBlocks.FADING_LARIMAR_ORE.get(), ArcaneItems.RAW_LARIMAR.get());
            ore(ArcaneBlocks.FADED_LARIMAR_ORE.get(), ArcaneItems.FADED_RAW_LARIMAR.get());
            ore(ArcaneBlocks.DEEPSLATE_LARIMAR_ORE.get(), ArcaneItems.RAW_LARIMAR.get());
            ore(ArcaneBlocks.FADING_DEEPSLATE_LARIMAR_ORE.get(), ArcaneItems.RAW_LARIMAR.get());
            ore(ArcaneBlocks.FADED_DEEPSLATE_LARIMAR_ORE.get(), ArcaneItems.FADED_RAW_LARIMAR.get());
            ore(ArcaneBlocks.IDOCRASE_ORE.get(), ArcaneItems.RAW_IDOCRASE.get());
            ore(ArcaneBlocks.DEEPSLATE_IDOCRASE_ORE.get(), ArcaneItems.RAW_IDOCRASE.get());
            ore(ArcaneBlocks.NETHER_IDOCRASE_ORE.get(), ArcaneItems.RAW_IDOCRASE.get());
            ore(ArcaneBlocks.BLACKSTONE_IDOCRASE_ORE.get(), ArcaneItems.RAW_IDOCRASE.get());

            // Storage Blocks
            dropSelf(ArcaneBlocks.LARIMAR_BLOCK.get());
            dropSelf(ArcaneBlocks.FADING_LARIMAR_BLOCK.get());
            dropSelf(ArcaneBlocks.FADED_LARIMAR_BLOCK.get());
            dropSelf(ArcaneBlocks.AURACHALCUM_BLOCK.get());
        }

        private void ore(Block block, Item item) {
            add(block, b -> createOreDrop(block, item));
        }

        @Override
        public void generate(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
            generate();
            map.forEach(consumer);
        }
    }
}
