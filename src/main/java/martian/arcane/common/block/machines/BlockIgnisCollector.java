package martian.arcane.common.block.machines;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.common.block.entity.machines.BlockEntityIgnisCollector;
import martian.arcane.common.registry.ArcaneBlockEntities;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockIgnisCollector extends AbstractAuraMachine {
    public BlockIgnisCollector(int maxAura, int auraLoss) {
        super(PropertyHelpers.basicAuraMachine(), (pos, state) -> new BlockEntityIgnisCollector(maxAura, auraLoss, pos, state));
    }

    @Override
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide && type == ArcaneBlockEntities.IGNIS_COLLECTOR.get() ? BlockEntityIgnisCollector::tick : null;
    }
}
