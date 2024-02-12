package martian.arcane.datagen;

import martian.arcane.ArcaneMod;
import martian.arcane.registry.ArcaneBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ArcaneBlockStateProvider extends BlockStateProvider {
    private static final ResourceLocation TRANSLUCENT = new ResourceLocation("translucent");

    public ArcaneBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ArcaneMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Block Models
        simpleTranslucent(ArcaneBlocks.AURAGLASS.get());
        simpleTranslucent(ArcaneBlocks.CONJURED_BLOCK.get());

        topBottom(ArcaneBlocks.IGNIS_COLLECTOR.get(), texture("collectors/ignis_sides"), texture("collectors/top_and_bottom"));
        topBottom(ArcaneBlocks.AQUA_COLLECTOR.get(), texture("collectors/aqua_sides"), texture("collectors/top_and_bottom"));

        cubeAll(ArcaneBlocks.SOUL_MAGMA.get());

        // Block States
        makeBlockState(ArcaneBlocks.AURA_NODI);
        makeBlockState(ArcaneBlocks.AURA_BASIN);
        makeBlockState(ArcaneBlocks.AURA_INFUSER);
        makeBlockState(ArcaneBlocks.IGNIS_COLLECTOR);
        makeBlockState(ArcaneBlocks.AQUA_COLLECTOR);
        makeBlockState(ArcaneBlocks.SOUL_MAGMA);
        makeBlockState(ArcaneBlocks.PEDESTAL);
        makeBlockState(ArcaneBlocks.CONJURED_CRAFTING_TABLE);
    }

    private void makeBlockState(RegistryObject<Block> block) {
        getMultipartBuilder(block.get())
                .part()
                .modelFile(models().getExistingFile(modLoc(block.getId().getPath())))
                .addModel();
    }

    private void simpleTranslucent(Block block) {
        simpleBlockWithRenderType(block, TRANSLUCENT);
    }

    private void topBottom(Block block, ResourceLocation sides, ResourceLocation topAndBottom) {
        models().cubeBottomTop(name(block), sides, topAndBottom, topAndBottom);
    }

    @SuppressWarnings("SameParameterValue")
    private void simpleBlockWithRenderType(Block block, ResourceLocation renderType) {
        getVariantBuilder(block)
                .partialState()
                .setModels(new ConfiguredModel(models()
                        .cubeAll(name(block), blockTexture(block))
                        .renderType(renderType)
                ));
    }

    private ResourceLocation texture(String path) {
        return new ResourceLocation(ArcaneMod.MODID, "block/" + path);
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private String name(Block block) {
        return key(block).getPath();
    }
}
