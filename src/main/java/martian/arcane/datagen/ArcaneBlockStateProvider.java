package martian.arcane.datagen;

import martian.arcane.ArcaneMod;
import martian.arcane.registry.ArcaneBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ArcaneBlockStateProvider extends BlockStateProvider {
    private static final ResourceLocation CUTOUT = new ResourceLocation("cutout");
    private static final ResourceLocation TRANSLUCENT = new ResourceLocation("translucent");

    public ArcaneBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ArcaneMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ArcaneBlocks.BLOCKS.getEntries().forEach(this::makeBlock);

        makeBlockState(ArcaneBlocks.AURA_NODI);
        makeBlockState(ArcaneBlocks.AURA_EXTRACTOR);
        makeBlockState(ArcaneBlocks.AURA_INSERTER);
    }

    private void makeBlockState(RegistryObject<Block> block) {
        getMultipartBuilder(block.get())
                .part()
                .modelFile(models().getExistingFile(modLoc(block.getId().getPath())))
                .addModel();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private void makeBlock(RegistryObject<Block> block) {
        if (
            block == ArcaneBlocks.AURAGLASS ||
            block == ArcaneBlocks.AURA_BASIN
        )
            simpleTranslucent(block.get());
        else if (
            block == ArcaneBlocks.AURA_EXTRACTOR ||
            block == ArcaneBlocks.AURA_INSERTER ||
            block == ArcaneBlocks.AURA_NODI
        ) { }
        else
            simpleBlock(block.get());
    }

    private void simpleCutout(Block block) {
        simpleBlockWithRenderType(block, CUTOUT);
    }

    private void simpleTranslucent(Block block) {
        simpleBlockWithRenderType(block, TRANSLUCENT);
    }

    private void simpleBlockWithRenderType(Block block, ResourceLocation renderType) {
        simpleBlock(block, models()
                .getBuilder(DataGenUtils.name(block))
                .parent(cubeAll(block))
                .renderType(renderType));
    }
}
