package martian.arcane.common.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.common.block.*;
import martian.arcane.common.block.machines.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ArcaneBlocks extends ArcaneRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ArcaneMod.MODID);

    public static final RegistryObject<Block>
            AURA_NODI = register("aura_nodi", BlockAuraNodi::new),
            // General Blocks
            AURAGLASS = register("auraglass", () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion())),
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
            SPELL_CIRCLE = register("spell_circle", () -> new BlockSpellCircle(ArcaneStaticConfig.Maximums.SPELL_CIRCLE_BASIC, ArcaneStaticConfig.Speed.SPELL_CIRCLE_BASIC, 1)),
            // Generators
            IGNIS_COLLECTOR = register("ignis_collector", BlockIgnisCollector::new),
            AQUA_COLLECTOR = register("aqua_collector", BlockAquaCollector::new),
            // Conjured Blocks
            CONJURED_BLOCK = register("conjured_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.GLASS).instabreak().noParticlesOnBreak())),
            CONJURED_CRAFTING_TABLE = register("conjured_crafting_table", BlockConjuredCraftingTable::new)
    ;

    private static RegistryObject<Block> register(String id, Supplier<Block> sup) {
        RegistryObject<Block> reg = BLOCKS.register(id, sup);
        // The spell circle has its own item to place it
        if (!id.equals("spell_circle"))
            ArcaneItems.blockItem(id, reg);
        return reg;
    }
}
