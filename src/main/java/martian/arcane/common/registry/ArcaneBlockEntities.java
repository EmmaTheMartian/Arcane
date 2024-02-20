package martian.arcane.common.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.common.block.entity.*;
import martian.arcane.common.block.entity.machines.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// DataFlowIssue gets rid of the warnings when passing `null` for BlockEntityType.Builder#build
@SuppressWarnings({"unused", "DataFlowIssue"})
public class ArcaneBlockEntities extends ArcaneRegistry {
    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ArcaneMod.MODID);

    public static final RegistryObject<BlockEntityType<BlockEntityAuraNodi>> AURA_NODI = REGISTER.register("be_aura_nodi",
        () -> of(BlockEntityAuraNodi::new, ArcaneBlocks.AURA_NODI.get()));

    // Logistics
    public static final RegistryObject<BlockEntityType<BlockEntityAuraExtractor>> AURA_EXTRACTOR = REGISTER.register("be_aura_extractor",
            () -> of(BlockEntityAuraExtractor::new, ArcaneBlocks.AURA_EXTRACTOR.get(), ArcaneBlocks.IMPROVED_AURA_EXTRACTOR.get()));
    public static final RegistryObject<BlockEntityType<BlockEntityAuraInserter>> AURA_INSERTER = REGISTER.register("be_aura_inserter",
            () -> of(BlockEntityAuraInserter::new, ArcaneBlocks.AURA_INSERTER.get(), ArcaneBlocks.IMPROVED_AURA_INSERTER.get()));

    // Storage
    public static final RegistryObject<BlockEntityType<BlockEntityAuraBasin>> AURA_BASIN = REGISTER.register("be_aura_basin",
            () -> of(BlockEntityAuraBasin::new, ArcaneBlocks.AURA_BASIN.get()));
    public static final RegistryObject<BlockEntityType<BlockEntityPedestal>> PEDESTAL = REGISTER.register("be_pedestal",
            () -> of(BlockEntityPedestal::new, ArcaneBlocks.PEDESTAL.get()));

    // Machines
    public static final RegistryObject<BlockEntityType<BlockEntityAuraInfuser>> AURA_INFUSER = REGISTER.register("be_aura_infuser",
            () -> of(BlockEntityAuraInfuser::new, ArcaneBlocks.AURA_INFUSER.get()));

    // Spell Circles
    public static final RegistryObject<BlockEntityType<BlockEntitySpellCircle>> SPELL_CIRCLE = REGISTER.register("be_spell_circle",
            () -> of(BlockEntitySpellCircle::new, ArcaneBlocks.SPELL_CIRCLE.get()));

    // Generators
    public static final RegistryObject<BlockEntityType<BlockEntityIgnisCollector>> IGNIS_COLLECTOR = REGISTER.register("be_ignis_collector",
            () -> of(BlockEntityIgnisCollector::new, ArcaneBlocks.IGNIS_COLLECTOR.get()));
    public static final RegistryObject<BlockEntityType<BlockEntityAquaCollector>> AQUA_COLLECTOR = REGISTER.register("be_aqua_collector",
            () -> of(BlockEntityAquaCollector::new, ArcaneBlocks.AQUA_COLLECTOR.get()));

    // Helpers
    private static <T extends BlockEntity> BlockEntityType<T> of(BlockEntityType.BlockEntitySupplier<T> sup, Block... blocks) {
        return BlockEntityType.Builder.of(sup, blocks).build(null);
    }
}
