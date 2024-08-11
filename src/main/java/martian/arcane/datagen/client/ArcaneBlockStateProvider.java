package martian.arcane.datagen.client;

import martian.arcane.ArcaneMod;
import martian.arcane.common.block.connector.BlockAuraConnector;
import martian.arcane.common.block.pedestal.BlockPedestal;
import martian.arcane.common.block.basin.BlockAuraBasin;
import martian.arcane.common.block.infuser.BlockAuraInfuser;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import static martian.arcane.common.ArcaneContent.*;

public class ArcaneBlockStateProvider extends BlockStateProvider {
    private static final ResourceLocation TRANSLUCENT = new ResourceLocation("translucent");

    public ArcaneBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ArcaneMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Models
        // Generators
        topBottom(BE_HEAT_COLLECTOR.block().get(), texture("machines/ignis_collector"), new ResourceLocation("minecraft", "block/polished_deepslate"));
        topBottom(BE_AQUA_COLLECTOR.block().get(), texture("machines/aqua_collector"), new ResourceLocation("minecraft", "block/polished_deepslate"));

        // General Blocks
        simpleTranslucent(BLOCK_AURAGLASS.get());
        cubeAll(BLOCK_SOUL_MAGMA.get());
        withExistingParent(BLOCK_AURA_TORCH.get(), new ResourceLocation("minecraft", "block/template_torch"))
                .renderType("minecraft:cutout")
                .texture("torch", "arcane:block/aura_torch");

        // Conjured Blocks
        simpleTranslucent(BLOCK_CONJURED_BLOCK.get());

        // Ores
        cubeAll(BLOCK_LARIMAR_ORE.get());
        cubeAll(BLOCK_FADING_LARIMAR_ORE.get());
        cubeAll(BLOCK_FADED_LARIMAR_ORE.get());
        cubeAll(BLOCK_DEEPSLATE_LARIMAR_ORE.get());
        cubeAll(BLOCK_FADING_DEEPSLATE_LARIMAR_ORE.get());
        cubeAll(BLOCK_FADED_DEEPSLATE_LARIMAR_ORE.get());
        cubeAll(BLOCK_IDOCRASE_ORE.get());
        cubeAll(BLOCK_DEEPSLATE_IDOCRASE_ORE.get());
        cubeAll(BLOCK_NETHER_IDOCRASE_ORE.get());
        cubeAll(BLOCK_BLACKSTONE_IDOCRASE_ORE.get());

        // Storage Blocks
        cubeAll(BLOCK_FADED_LARIMAR.get());
        cubeAll(BLOCK_FADING_LARIMAR.get());
        cubeAll(BLOCK_LARIMAR.get());
        cubeAll(BLOCK_AURACHALCUM.get());

        cubeAll(BLOCK_FROZEN_OBSIDIAN.get());

        // Block States
        makeBlockState(BLOCK_SOUL_MAGMA);
        makeBlockState(BLOCK_AURA_TORCH);

        // Machines
        makeRotatableModel(BE_AURA_CONNECTOR.block(), BlockAuraConnector.FACING, false);
        makeRotatableModel(BE_AURA_BASIN.block(), BlockAuraBasin.FACING, true);
        makeRotatableModel(BE_AURA_INFUSER.block(), BlockAuraInfuser.FACING, true);
        makeRotatableModel(BE_PEDESTAL.block(), BlockPedestal.FACING, true);
        makeBlockState(BE_SPELL_CIRCLE.block());
        makeBlockState(BE_HEAT_COLLECTOR.block());
        makeBlockState(BE_AQUA_COLLECTOR.block());

        // Conjured Blocks
        makeBlockState(BLOCK_CONJURED_CRAFTING_TABLE);

        // Ores
        makeBlockState(BLOCK_LARIMAR_ORE);
        makeBlockState(BLOCK_FADING_LARIMAR_ORE);
        makeBlockState(BLOCK_FADED_LARIMAR_ORE);
        makeBlockState(BLOCK_DEEPSLATE_LARIMAR_ORE);
        makeBlockState(BLOCK_FADING_DEEPSLATE_LARIMAR_ORE);
        makeBlockState(BLOCK_FADED_DEEPSLATE_LARIMAR_ORE);
        makeBlockState(BLOCK_IDOCRASE_ORE);
        makeBlockState(BLOCK_DEEPSLATE_IDOCRASE_ORE);
        makeBlockState(BLOCK_NETHER_IDOCRASE_ORE);
        makeBlockState(BLOCK_BLACKSTONE_IDOCRASE_ORE);

        // Storage Blocks
        makeBlockState(BLOCK_FADED_LARIMAR);
        makeBlockState(BLOCK_FADING_LARIMAR);
        makeBlockState(BLOCK_LARIMAR);
        makeBlockState(BLOCK_AURACHALCUM);

        makeBlockState(BLOCK_FROZEN_OBSIDIAN);
    }

    private void makeBlockState(DeferredBlock<?> block) {
        getMultipartBuilder(block.get())
                .part()
                .modelFile(models().getExistingFile(modLoc(block.getId().getPath())))
                .addModel();
    }

    private void makeRotatableModel(DeferredBlock<?> block, Property<Direction> property, boolean horizontal) {
        ModelFile model = models().getExistingFile(modLoc(block.getId().getPath()));
        getVariantBuilder(block.get()).forAllStates(state -> {
            Direction facing = state.getValue(property);
            if (horizontal && (facing == Direction.DOWN || facing == Direction.UP)) {
                return ConfiguredModel.builder().modelFile(model).build();
            }

            int x = 0, y = 0;
            switch (facing) {
                case NORTH -> { x = 90; y = 180; }
                case EAST -> { x = 90; y = 270; }
                case SOUTH -> x = 90;
                case WEST -> x = y = 90;
                case UP -> x = 180;
            }

            if (horizontal) x = 0;

            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationX(x)
                    .rotationY(y)
                    .build();
        });
    }

    private void simpleTranslucent(Block block) {
        simpleBlockWithRenderType(block, TRANSLUCENT);
    }

    private void topBottom(Block block, ResourceLocation sides, ResourceLocation topAndBottom) {
        models().cubeBottomTop(name(block), sides, topAndBottom, topAndBottom);
    }

    private BlockModelBuilder withExistingParent(Block block, ResourceLocation parent) {
        return models().withExistingParent(name(block), parent);
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
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    private String name(Block block) {
        return key(block).getPath();
    }
}
