package martian.arcane.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.block.*;
import martian.arcane.block.machines.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ArcaneBlocks extends ArcaneRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ArcaneMod.MODID);

    public static final RegistryObject<Block> AURA_NODI = register("aura_nodi", BlockAuraNodi::new);

    public static final RegistryObject<Block>
            // General Blocks
            AURAGLASS = register("auraglass", () -> new Block(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion())),
            SOUL_MAGMA = register("soul_magma", BlockSoulMagma::new),
            // Logistics
            AURA_EXTRACTOR = register("aura_extractor", () -> new BlockAuraExtractor(ArcaneStaticConfig.Maximums.AURA_EXTRACTOR, ArcaneStaticConfig.Rates.AURA_EXTRACTOR_RATE)),
            AURA_INSERTER = register("aura_inserter", () -> new BlockAuraInserter(ArcaneStaticConfig.Maximums.AURA_INSERTER, ArcaneStaticConfig.Rates.AURA_INSERTER_RATE)),
            IMPROVED_AURA_EXTRACTOR = register("improved_aura_extractor", () -> new BlockAuraExtractor(ArcaneStaticConfig.Maximums.IMPROVED_AURA_EXTRACTOR, ArcaneStaticConfig.Rates.IMPROVED_AURA_EXTRACTOR_RATE)),
            IMPROVED_AURA_INSERTER = register("improved_aura_inserter", () -> new BlockAuraInserter(ArcaneStaticConfig.Maximums.IMPROVED_AURA_INSERTER, ArcaneStaticConfig.Rates.IMPROVED_AURA_INSERTER_RATE)),
            // Storage
            AURA_BASIN = register("aura_basin", BlockAuraBasin::new),
            PEDESTAL = register("pedestal", BlockPedestal::new),
            // Machines
            AURA_INFUSER = register("aura_infuser", BlockAuraInfuser::new),
            // Generators
            IGNIS_COLLECTOR = register("ignis_collector", BlockIgnisCollector::new),
            AQUA_COLLECTOR = register("aqua_collector", BlockAquaCollector::new)
    ;

    private static RegistryObject<Block> register(String id, Supplier<Block> sup) {
        RegistryObject<Block> reg = BLOCKS.register(id, sup);
        ArcaneItems.blockItem(id, reg);
        return reg;
    }
}
