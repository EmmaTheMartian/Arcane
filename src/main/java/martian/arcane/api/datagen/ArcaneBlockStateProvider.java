package martian.arcane.api.datagen;

import martian.arcane.ArcaneMod;
import martian.arcane.registry.ArcaneBlocks;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ArcaneBlockStateProvider extends BlockStateProvider {
    public ArcaneBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ArcaneMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ArcaneBlocks.BLOCKS.getEntries().forEach(block -> {
            simpleBlock(block.get());
        });
    }
}
