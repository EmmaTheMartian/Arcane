package martian.arcane.common.block.machines;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.common.block.entity.machines.BlockEntityAquaCollector;
import martian.arcane.common.registry.ArcaneBlockEntities;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockAquaCollector extends AbstractAuraMachine {
    public BlockAquaCollector(int maxAura, int auraLoss) {
        super(PropertyHelpers.basicAuraMachine(), (pos, state) -> new BlockEntityAquaCollector(maxAura, auraLoss, pos, state));
    }

    @Override
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide && type == ArcaneBlockEntities.AQUA_COLLECTOR.get() ? BlockEntityAquaCollector::tick : null;
    }
}
