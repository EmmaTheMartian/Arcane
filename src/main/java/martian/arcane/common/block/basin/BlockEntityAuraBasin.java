package martian.arcane.common.block.basin;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.common.ArcaneContent;
import martian.arcane.common.registry.ArcaneBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAuraBasin extends AbstractAuraBlockEntity {
    public BlockEntityAuraBasin(BlockPos pos, BlockState state) {
        super(ArcaneStaticConfig.AuraMaximums.AURA_BASIN, true, true, ArcaneContent.AURA_BASIN.tile().get(), pos, state);
    }

    @Override
    public boolean isUpgradable() {
        return true;
    }
}
