package martian.arcane.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.block.entity.BlockEntityAuraExtractor;
import martian.arcane.block.entity.BlockEntityAuraNodi;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ArcaneBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ArcaneMod.MODID);

    public static final RegistryObject<BlockEntityType<BlockEntityAuraNodi>> AURA_NODI_BE = BLOCK_ENTITIES.register("be_aura_nodi", () ->
        BlockEntityType.Builder.of(BlockEntityAuraNodi::new, ArcaneBlocks.AURA_NODI.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityAuraExtractor>> AURA_EXTRACTOR_BE = BLOCK_ENTITIES.register("be_aura_extractor", () ->
        BlockEntityType.Builder.of(BlockEntityAuraExtractor::new, ArcaneBlocks.AURA_EXTRACTOR.get()).build(null));
}
