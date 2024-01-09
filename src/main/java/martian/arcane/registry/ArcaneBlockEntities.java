package martian.arcane.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.block.entity.BlockEntityAuraBasin;
import martian.arcane.block.entity.BlockEntityAuraExtractor;
import martian.arcane.block.entity.BlockEntityAuraInserter;
import martian.arcane.block.entity.BlockEntityAuraNodi;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// DataFlowIssue gets rid of the warnings when passing `null` for BlockEntityType.Builder#build
@SuppressWarnings({"unused", "DataFlowIssue"})
public class ArcaneBlockEntities extends ArcaneRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ArcaneMod.MODID);

    public static final RegistryObject<BlockEntityType<BlockEntityAuraNodi>> AURA_NODI_BE =
            BLOCK_ENTITIES.register("be_aura_nodi", () -> BlockEntityType.Builder
                .of(BlockEntityAuraNodi::new, ArcaneBlocks.AURA_NODI.get())
                .build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityAuraExtractor>> AURA_EXTRACTOR_BE =
            BLOCK_ENTITIES.register("be_aura_extractor", () -> BlockEntityType.Builder
                    .of(BlockEntityAuraExtractor::new, ArcaneBlocks.AURA_EXTRACTOR.get())
                    .build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityAuraInserter>> AURA_INSERTER_BE =
            BLOCK_ENTITIES.register("be_aura_inserter", () -> BlockEntityType.Builder
                    .of(BlockEntityAuraInserter::new, ArcaneBlocks.AURA_INSERTER.get())
                    .build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityAuraBasin>> AURA_BASIN_BE =
            BLOCK_ENTITIES.register("be_aura_basin", () -> BlockEntityType.Builder
                    .of(BlockEntityAuraBasin::new, ArcaneBlocks.AURA_BASIN.get())
                    .build(null));
}
