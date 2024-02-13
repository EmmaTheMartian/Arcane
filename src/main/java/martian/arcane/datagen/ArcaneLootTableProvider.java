package martian.arcane.datagen;

import martian.arcane.registry.ArcaneBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
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
            ArcaneBlocks.BLOCKS.getEntries().forEach(block -> {
                if (block == ArcaneBlocks.CONJURED_BLOCK || block == ArcaneBlocks.CONJURED_CRAFTING_TABLE)
                    return;

                dropSelf(block.get());
            });
        }

        @Override
        public void generate(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
            generate();
            map.forEach(consumer);
        }
    }
}
