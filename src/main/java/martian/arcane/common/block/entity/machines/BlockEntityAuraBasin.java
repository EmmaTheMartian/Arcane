package martian.arcane.common.block.entity.machines;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.common.registry.ArcaneBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAuraBasin extends AbstractAuraBlockEntity {
    public BlockEntityAuraBasin(int max, int auraLoss, BlockPos pos, BlockState state) {
        super(max, auraLoss, true, true, ArcaneBlockEntities.AURA_BASIN.get(), pos, state);
    }

    public BlockEntityAuraBasin(BlockPos pos, BlockState state) {
        super(ArcaneStaticConfig.Maximums.COPPER_AURA_BASIN, ArcaneStaticConfig.AuraLoss.COPPER_TIER, true, true, ArcaneBlockEntities.AURA_BASIN.get(), pos, state);
    }
}
