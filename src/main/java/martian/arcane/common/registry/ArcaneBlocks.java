package martian.arcane.common.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.api.block.BasicLarimarBlock;
import martian.arcane.client.ParticleHelper;
import martian.arcane.common.block.BlockAuraTorch;
import martian.arcane.common.block.basin.BlockAuraBasin;
import martian.arcane.common.block.connector.BlockAuraConnector;
import martian.arcane.common.block.infuser.BlockAuraInfuser;
import martian.arcane.common.block.generators.heat.BlockIgnisCollector;
import martian.arcane.common.block.BlockConjuredCraftingTable;
import martian.arcane.common.block.pedestal.BlockPedestal;
import martian.arcane.common.block.BlockSoulMagma;
import martian.arcane.common.block.generators.water.BlockAquaCollector;
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
            // General Blocks
            AURAGLASS = register("auraglass", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion())),
            SOUL_MAGMA = register("soul_magma", BlockSoulMagma::new),
            AURA_TORCH = register("aura_torch", () -> new BlockAuraTorch(ParticleHelper.MAGIC_PARTICLE_OPTIONS, BlockBehaviour.Properties.ofFullCopy(Blocks.TORCH).noLootTable())),

            // Aura machines
            AURA_CONNECTOR = register("aura_connector", BlockAuraConnector::new),
            AURA_BASIN = register("aura_basin", BlockAuraBasin::new),
            PEDESTAL = register("pedestal", () -> new BlockPedestal(ArcaneStaticConfig.AuraMaximums.PEDESTAL)),
            AURA_INFUSER = register("aura_infuser", BlockAuraInfuser::new),
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
