package martian.arcane.block.entity;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.registry.ArcaneBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAuraBasin extends AbstractAuraBlockEntity {
    public BlockEntityAuraBasin(BlockPos pos, BlockState state) {
        super(ArcaneStaticConfig.Maximums.AURA_BASIN, true, true, ArcaneBlockEntities.AURA_BASIN.get(), pos, state);
    }
}
