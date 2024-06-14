package martian.arcane.common.block.generators.water;

import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.common.ArcaneContent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockAquaCollector extends AbstractAuraMachine {
    public BlockAquaCollector() {
        super(PropertyHelpers.basicAuraMachine(), BlockEntityAquaCollector::new);
    }

    @Override
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide && type == ArcaneContent.AQUA_COLLECTOR.tile().get() ? BlockEntityAquaCollector::tick : null;
    }
}
