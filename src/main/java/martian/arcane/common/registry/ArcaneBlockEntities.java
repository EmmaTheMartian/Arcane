package martian.arcane.common.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.common.block.aura.basin.BlockEntityAuraBasin;
import martian.arcane.common.block.aura.extractor.BlockEntityAuraExtractor;
import martian.arcane.common.block.aura.infuser.BlockEntityAuraInfuser;
import martian.arcane.common.block.aura.inserter.BlockEntityAuraInserter;
import martian.arcane.common.block.aura.generators.heat.BlockEntityIgnisCollector;
import martian.arcane.common.block.etc.auranodi.BlockEntityAuraNodi;
import martian.arcane.common.block.aura.generators.water.BlockEntityAquaCollector;
import martian.arcane.common.block.pedestal.BlockEntityPedestal;
import martian.arcane.common.block.spellcircle.BlockEntitySpellCircle;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

// DataFlowIssue gets rid of the warnings when passing `null` for BlockEntityType.Builder#build
@SuppressWarnings({"unused", "DataFlowIssue"})
public class ArcaneBlockEntities extends ArcaneRegistry {
    public ArcaneBlockEntities() { super(REGISTER); }

    private static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ArcaneMod.MODID);

    static {
        AURA_NODI = register("be_aura_nodi", () -> of(BlockEntityAuraNodi::new, ArcaneBlocks.AURA_NODI.get()));
        AURA_EXTRACTOR = register("be_aura_extractor", () -> of(BlockEntityAuraExtractor::new, ArcaneBlocks.COPPER_AURA_EXTRACTOR.get(), ArcaneBlocks.AURACHALCUM_AURA_EXTRACTOR.get()));
        AURA_INSERTER = register("be_aura_inserter", () -> of(BlockEntityAuraInserter::new, ArcaneBlocks.COPPER_AURA_INSERTER.get(), ArcaneBlocks.AURACHALCUM_AURA_INSERTER.get()));
        AURA_BASIN = register("be_aura_basin", () -> of(BlockEntityAuraBasin::new, ArcaneBlocks.COPPER_AURA_BASIN.get()));
        PEDESTAL = register("be_pedestal", () -> of(BlockEntityPedestal::new, ArcaneBlocks.PEDESTAL.get()));
        AURA_INFUSER = register("be_aura_infuser", () -> of(BlockEntityAuraInfuser::new, ArcaneBlocks.AURA_INFUSER.get()));
        SPELL_CIRCLE = register("be_spell_circle", () -> of(BlockEntitySpellCircle::new, ArcaneBlocks.SPELL_CIRCLE.get()));
        HEAT_COLLECTOR = register("be_heat_collector", () -> of(BlockEntityIgnisCollector::new, ArcaneBlocks.HEAT_COLLECTOR.get()));
        AQUA_COLLECTOR = register("be_aqua_collector", () -> of(BlockEntityAquaCollector::new, ArcaneBlocks.AQUA_COLLECTOR.get()));
    }

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityAuraNodi>> AURA_NODI;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityAuraExtractor>> AURA_EXTRACTOR;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityAuraInserter>> AURA_INSERTER;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityAuraBasin>> AURA_BASIN;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityPedestal>> PEDESTAL;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityAuraInfuser>> AURA_INFUSER;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntitySpellCircle>> SPELL_CIRCLE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityIgnisCollector>> HEAT_COLLECTOR;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityAquaCollector>> AQUA_COLLECTOR;

    // Helpers
    private static <T extends BlockEntity> BlockEntityType<T> of(BlockEntityType.BlockEntitySupplier<T> sup, Block... blocks) {
        return BlockEntityType.Builder.of(sup, blocks).build(null);
    }

    private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String name, Supplier<BlockEntityType<T>> sup) {
        return REGISTER.register(name, sup);
    }
}
