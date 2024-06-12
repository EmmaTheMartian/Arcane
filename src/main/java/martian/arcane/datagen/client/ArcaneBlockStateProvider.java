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
        topBottom(HEAT_COLLECTOR.block().get(), texture("machines/ignis_collector"), new ResourceLocation("minecraft", "block/polished_deepslate"));
        topBottom(AQUA_COLLECTOR.block().get(), texture("machines/aqua_collector"), new ResourceLocation("minecraft", "block/polished_deepslate"));

        // General Blocks
        simpleTranslucent(AURAGLASS.get());
        cubeAll(SOUL_MAGMA.get());
        withExistingParent(AURA_TORCH.get(), new ResourceLocation("minecraft", "block/template_torch"))
                .renderType("minecraft:cutout")
                .texture("torch", "arcane:block/aura_torch");

        // Conjured Blocks
        simpleTranslucent(CONJURED_BLOCK.get());

        // Ores
        cubeAll(LARIMAR_ORE.get());
        cubeAll(FADING_LARIMAR_ORE.get());
        cubeAll(FADED_LARIMAR_ORE.get());
        cubeAll(DEEPSLATE_LARIMAR_ORE.get());
        cubeAll(FADING_DEEPSLATE_LARIMAR_ORE.get());
        cubeAll(FADED_DEEPSLATE_LARIMAR_ORE.get());
        cubeAll(IDOCRASE_ORE.get());
        cubeAll(DEEPSLATE_IDOCRASE_ORE.get());
        cubeAll(NETHER_IDOCRASE_ORE.get());
        cubeAll(BLACKSTONE_IDOCRASE_ORE.get());

        // Storage Blocks
        cubeAll(FADED_LARIMAR_BLOCK.get());
        cubeAll(FADING_LARIMAR_BLOCK.get());
        cubeAll(LARIMAR_BLOCK.get());
        cubeAll(AURACHALCUM_BLOCK.get());

        // Block States
        makeBlockState(SOUL_MAGMA);
        makeBlockState(AURA_TORCH);

        // Machines
        makeRotatableModel(AURA_CONNECTOR.block(), BlockAuraConnector.FACING, false);
        makeRotatableModel(AURA_BASIN.block(), BlockAuraBasin.FACING, true);
        makeRotatableModel(AURA_INFUSER.block(), BlockAuraInfuser.FACING, true);
        makeRotatableModel(PEDESTAL.block(), BlockPedestal.FACING, true);
        makeBlockState(SPELL_CIRCLE.block());
        makeBlockState(HEAT_COLLECTOR.block());
        makeBlockState(AQUA_COLLECTOR.block());

        // Conjured Blocks
        makeBlockState(CONJURED_CRAFTING_TABLE);

        // Ores
        makeBlockState(LARIMAR_ORE);
        makeBlockState(FADING_LARIMAR_ORE);
        makeBlockState(FADED_LARIMAR_ORE);
        makeBlockState(DEEPSLATE_LARIMAR_ORE);
        makeBlockState(FADING_DEEPSLATE_LARIMAR_ORE);
        makeBlockState(FADED_DEEPSLATE_LARIMAR_ORE);
        makeBlockState(IDOCRASE_ORE);
        makeBlockState(DEEPSLATE_IDOCRASE_ORE);
        makeBlockState(NETHER_IDOCRASE_ORE);
        makeBlockState(BLACKSTONE_IDOCRASE_ORE);

        // Storage Blocks
        makeBlockState(FADED_LARIMAR_BLOCK);
        makeBlockState(FADING_LARIMAR_BLOCK);
        makeBlockState(LARIMAR_BLOCK);
        makeBlockState(AURACHALCUM_BLOCK);
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
