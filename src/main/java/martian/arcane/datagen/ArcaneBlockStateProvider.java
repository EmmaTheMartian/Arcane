package martian.arcane.datagen;

import martian.arcane.ArcaneMod;
import martian.arcane.registry.ArcaneBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ArcaneBlockStateProvider extends BlockStateProvider {
    private static final ResourceLocation TRANSLUCENT = new ResourceLocation("translucent");

    public ArcaneBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ArcaneMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ArcaneBlocks.BLOCKS.getEntries().forEach(this::makeBlock);

        makeBlockState(ArcaneBlocks.AURA_NODI);
        makeBlockState(ArcaneBlocks.AURA_BASIN);
        makeBlockState(ArcaneBlocks.AURA_INFUSER);
        makeBlockState(ArcaneBlocks.ITEM_PYLON);
        makeBlockState(ArcaneBlocks.SPELLCRAFTING_CORE);
    }

    private void makeBlockState(RegistryObject<Block> block) {
        getMultipartBuilder(block.get())
                .part()
                .modelFile(models().getExistingFile(modLoc(block.getId().getPath())))
                .addModel();
    }

    @SuppressWarnings("UnnecessaryReturnStatement")
    private void makeBlock(RegistryObject<Block> block) {
        // Blocks to skip
        if (
                block == ArcaneBlocks.AURA_EXTRACTOR ||
                block == ArcaneBlocks.AURA_INSERTER ||
                block == ArcaneBlocks.AURA_NODI ||
                block == ArcaneBlocks.AURA_BASIN ||
                block == ArcaneBlocks.AURA_INFUSER ||
                block == ArcaneBlocks.SPELLCRAFTING_CORE ||
                block == ArcaneBlocks.ITEM_PYLON
        )
            return;
        // Basic translucent blocks
        else if (
                block == ArcaneBlocks.AURAGLASS
        )
            simpleTranslucent(block.get());
        // Everything else
        else
            simpleBlock(block.get());
    }

    private void simpleTranslucent(Block block) {
        simpleBlockWithRenderType(block, TRANSLUCENT);
    }

    @SuppressWarnings("SameParameterValue")
    private void simpleBlockWithRenderType(Block block, ResourceLocation renderType) {
        getVariantBuilder(block)
                .partialState()
                .setModels(new ConfiguredModel(models()
                        .cubeAll(DataGenUtils.name(block), blockTexture(block))
                        .renderType(renderType)
                ));
    }
}
