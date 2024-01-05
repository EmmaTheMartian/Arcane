package martian.arcane.api.datagen;

import martian.arcane.ArcaneMod;
import martian.arcane.registry.ArcaneBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ArcaneBlockModelProvider extends BlockModelProvider {
    public ArcaneBlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ArcaneMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ArcaneBlocks.BLOCKS.getEntries().forEach(block -> {
            basicBlock(block.getId());
        });
    }

    private void basicBlock(ResourceLocation id) {
        String path = "block/" + id.getPath();
        cubeAll(path, new ResourceLocation(id.getNamespace(), path));
    }
}
