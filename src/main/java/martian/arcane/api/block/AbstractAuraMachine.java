package martian.arcane.api.block;

import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiFunction;

public abstract class AbstractAuraMachine extends BlockWithEntity {
    public AbstractAuraMachine(BlockBehaviour.Properties properties, BiFunction<BlockPos, BlockState, BlockEntity> beSupplier) {
        super(properties, beSupplier);
    }

    @Override
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide ? AbstractAuraBlockEntity::tick : null;
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof AbstractAuraBlockEntity machine)
            return Math.round(machine.mapAuraStorage(aura -> (float)aura.getAura() / aura.getMaxAura() * 15F));
        return 0;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block fromBlock, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, fromBlock, fromPos, isMoving);
        if (
            !level.isClientSide &&
            level.getBlockEntity(pos) instanceof AbstractAuraBlockEntity machine &&
            machine.hasSignal != level.hasNeighborSignal(pos)
        ) {
            machine.hasSignal = !machine.hasSignal;
            level.updateNeighbourForOutputSignal(pos, this);
            BlockHelpers.sync(machine);
        }
    }
}
