package martian.arcane.block.entity;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.registry.ArcaneBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAuraExtractor extends AbstractAuraBlockEntity {
    public BlockEntityAuraExtractor(BlockPos pos, BlockState state) {
        super(ArcaneStaticConfig.AuraMaximums.AURA_EXTRACTOR, true, ArcaneBlockEntities.AURA_EXTRACTOR_BE.get(), pos, state);
    }
}
