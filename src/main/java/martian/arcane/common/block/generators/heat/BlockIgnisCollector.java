package martian.arcane.common.block.generators.heat;

import martian.arcane.api.block.BlockHelpers;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.common.ArcaneContent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockIgnisCollector extends AbstractAuraMachine {
    public BlockIgnisCollector() {
        super(BlockHelpers.basicAuraMachine(), BlockEntityIgnisCollector::new);
    }

    @Override
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide && type == ArcaneContent.BE_HEAT_COLLECTOR.tile().get() ? BlockEntityIgnisCollector::tick : null;
    }
}
