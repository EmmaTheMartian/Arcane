package martian.arcane.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.block.*;
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

    public static final RegistryObject<Block> AURA_NODI = registerBlock("aura_nodi", BlockAuraNodi::new);
    public static final RegistryObject<Block> AURAGLASS = registerBlock("auraglass", () -> new Block(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion()));
    public static final RegistryObject<Block> AURA_EXTRACTOR = registerBlock("aura_extractor", BlockAuraExtractor::new);
    public static final RegistryObject<Block> AURA_INSERTER = registerBlock("aura_inserter", BlockAuraInserter::new);
    public static final RegistryObject<Block> AURA_BASIN = registerBlock("aura_basin", BlockAuraBasin::new);
    public static final RegistryObject<Block> AURA_INFUSER = registerBlock("aura_infuser", BlockAuraInfuser::new);

    // Helpers
    private static RegistryObject<Block> registerBlock(String id, Supplier<Block> sup) {
        RegistryObject<Block> reg = BLOCKS.register(id, sup);
        ArcaneItems.registerBlockItem(id, reg, null);
        return reg;
    }
}
