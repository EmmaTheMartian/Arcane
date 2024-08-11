package martian.arcane.common.block.basin;

import martian.arcane.ArcaneConfig;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.common.ArcaneContent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAuraBasin extends AbstractAuraBlockEntity {
    public BlockEntityAuraBasin(BlockPos pos, BlockState state) {
        super(ArcaneConfig.auraBasinAuraCapacity, true, true, ArcaneContent.BE_AURA_BASIN.tile().get(), pos, state);
    }

    @Override
    public boolean isUpgradable() {
        return true;
    }
}
