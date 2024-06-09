package martian.arcane.common.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.api.block.BasicLarimarBlock;
import martian.arcane.common.block.aura.basin.BlockAuraBasin;
import martian.arcane.common.block.aura.extractor.BlockAuraExtractor;
import martian.arcane.common.block.aura.infuser.BlockAuraInfuser;
import martian.arcane.common.block.aura.inserter.BlockAuraInserter;
import martian.arcane.common.block.aura.generators.heat.BlockIgnisCollector;
import martian.arcane.common.block.etc.auranodi.BlockAuraNodi;
import martian.arcane.common.block.etc.BlockConjuredCraftingTable;
import martian.arcane.common.block.pedestal.BlockPedestal;
import martian.arcane.common.block.etc.BlockSoulMagma;
import martian.arcane.common.block.aura.generators.water.BlockAquaCollector;
import martian.arcane.common.block.spellcircle.BlockSpellCircle;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ArcaneBlocks extends ArcaneRegistry {
    public ArcaneBlocks() { super(BLOCKS); }

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ArcaneMod.MODID);

    public static final DeferredBlock<?>
            AURA_NODI = register("aura_nodi", BlockAuraNodi::new),

            // General Blocks
            AURAGLASS = register("auraglass", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion())),
            SOUL_MAGMA = register("soul_magma", BlockSoulMagma::new),

            // Aura Machines
            COPPER_AURA_EXTRACTOR = register("aura_extractor_copper", () -> new BlockAuraExtractor(ArcaneStaticConfig.AuraMaximums.AURA_EXTRACTORS, ArcaneStaticConfig.AuraLoss.COPPER_TIER, ArcaneStaticConfig.Rates.COPPER_AURA_EXTRACTOR_RATE)),
            LARIMAR_AURA_EXTRACTOR = register("aura_extractor_larimar", () -> new BlockAuraExtractor(ArcaneStaticConfig.AuraMaximums.AURA_EXTRACTORS, ArcaneStaticConfig.AuraLoss.LARIMAR_TIER, ArcaneStaticConfig.Rates.LARIMAR_AURA_EXTRACTOR_RATE)),
            AURACHALCUM_AURA_EXTRACTOR = register("aura_extractor_aurachalcum", () -> new BlockAuraExtractor(ArcaneStaticConfig.AuraMaximums.AURA_EXTRACTORS, ArcaneStaticConfig.AuraLoss.AURACHALCUM_TIER, ArcaneStaticConfig.Rates.AURACHALCUM_AURA_EXTRACTOR_RATE)),

            COPPER_AURA_INSERTER = register("aura_inserter_copper", () -> new BlockAuraInserter(ArcaneStaticConfig.AuraMaximums.AURA_INSERTERS, ArcaneStaticConfig.AuraLoss.COPPER_TIER, ArcaneStaticConfig.Rates.COPPER_AURA_INSERTER_RATE)),
            LARIMAR_AURA_INSERTER = register("aura_inserter_larimar", () -> new BlockAuraInserter(ArcaneStaticConfig.AuraMaximums.AURA_INSERTERS, ArcaneStaticConfig.AuraLoss.LARIMAR_TIER, ArcaneStaticConfig.Rates.LARIMAR_AURA_INSERTER_RATE)),
            AURACHALCUM_AURA_INSERTER = register("aura_inserter_aurachalcum", () -> new BlockAuraInserter(ArcaneStaticConfig.AuraMaximums.AURA_INSERTERS, ArcaneStaticConfig.AuraLoss.AURACHALCUM_TIER, ArcaneStaticConfig.Rates.AURACHALCUM_AURA_INSERTER_RATE)),

            COPPER_AURA_BASIN = register("aura_basin_copper", () -> new BlockAuraBasin(ArcaneStaticConfig.AuraMaximums.COPPER_AURA_BASIN, ArcaneStaticConfig.AuraLoss.COPPER_TIER)),
            LARIMAR_AURA_BASIN = register("aura_basin_larimar", () -> new BlockAuraBasin(ArcaneStaticConfig.AuraMaximums.LARIMAR_AURA_BASIN, ArcaneStaticConfig.AuraLoss.LARIMAR_TIER)),
            AURACHALCUM_AURA_BASIN = register("aura_basin_aurachalcum", () -> new BlockAuraBasin(ArcaneStaticConfig.AuraMaximums.AURACHALCUM_AURA_BASIN, ArcaneStaticConfig.AuraLoss.AURACHALCUM_TIER)),
            CREATIVE_AURA_BASIN = register("aura_basin_creative", () -> new BlockAuraBasin(Integer.MAX_VALUE, 0)),

            PEDESTAL = register("pedestal", () -> new BlockPedestal(ArcaneStaticConfig.AuraMaximums.PEDESTAL, ArcaneStaticConfig.AuraLoss.COPPER_TIER)),
            AURA_INFUSER = register("aura_infuser", () -> new BlockAuraInfuser(ArcaneStaticConfig.AuraMaximums.AURA_INFUSER, ArcaneStaticConfig.AuraLoss.COPPER_TIER)),
            SPELL_CIRCLE = register("spell_circle", () -> new BlockSpellCircle(ArcaneStaticConfig.AuraMaximums.SPELL_CIRCLE_BASIC, ArcaneStaticConfig.Speed.SPELL_CIRCLE_BASIC, 1)),

            // Elemental Energy Machines
            HEAT_COLLECTOR = register("heat_collector", () -> new BlockIgnisCollector(ArcaneStaticConfig.AuraMaximums.COLLECTOR, ArcaneStaticConfig.AuraLoss.COPPER_TIER)),
            AQUA_COLLECTOR = register("aqua_collector", () -> new BlockAquaCollector(ArcaneStaticConfig.AuraMaximums.COLLECTOR, ArcaneStaticConfig.AuraLoss.COPPER_TIER)),

            // Conjured Blocks
            CONJURED_BLOCK = register("conjured_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).instabreak())),
            CONJURED_CRAFTING_TABLE = register("conjured_crafting_table", BlockConjuredCraftingTable::new),

            // Ores
            FADED_LARIMAR_ORE = register("faded_larimar_ore", () ->
                    new BasicLarimarBlock(null, BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE))),
            FADING_LARIMAR_ORE = register("fading_larimar_ore", () ->
                    new BasicLarimarBlock(() -> FADED_LARIMAR_ORE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE))),
            LARIMAR_ORE = register("larimar_ore", () ->
                    new BasicLarimarBlock(() -> FADING_LARIMAR_ORE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE))),

            FADED_DEEPSLATE_LARIMAR_ORE = register("faded_deepslate_larimar_ore", () ->
                    new BasicLarimarBlock(null, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_COPPER_ORE))),
            FADING_DEEPSLATE_LARIMAR_ORE = register("fading_deepslate_larimar_ore", () ->
                    new BasicLarimarBlock(() -> FADED_DEEPSLATE_LARIMAR_ORE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_COPPER_ORE))),
            DEEPSLATE_LARIMAR_ORE = register("deepslate_larimar_ore", () ->
                    new BasicLarimarBlock(() -> FADING_DEEPSLATE_LARIMAR_ORE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_COPPER_ORE))),

            IDOCRASE_ORE = register("idocrase_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_ORE))),
            DEEPSLATE_IDOCRASE_ORE = register("deepslate_idocrase_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_GOLD_ORE))),
            NETHER_IDOCRASE_ORE = register("nether_idocrase_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_GOLD_ORE))),
            BLACKSTONE_IDOCRASE_ORE = register("blackstone_idocrase_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GILDED_BLACKSTONE))),

            // Storage blocks
            FADED_LARIMAR_BLOCK = register("faded_larimar_block", () ->
                    new BasicLarimarBlock(null, BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK))),
            FADING_LARIMAR_BLOCK = register("fading_larimar_block", () ->
                    new BasicLarimarBlock(() -> FADED_LARIMAR_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK))),
            LARIMAR_BLOCK = register("larimar_block", () ->
                    new BasicLarimarBlock(() -> FADING_LARIMAR_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK))),

            AURACHALCUM_BLOCK = register("aurachalcum_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK)))
    ;

    private static DeferredBlock<?> register(String id, Supplier<Block> sup) {
        DeferredBlock<?> reg = BLOCKS.register(id, sup);

        // The spell circle has its own, custom, fancy-shmancy BlockItem
        if (!id.equals("spell_circle"))
            ArcaneItems.blockItem(id, reg);

        return reg;
    }
}
