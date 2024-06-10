package martian.arcane.common.block.generators.heat;

import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.block.AbstractAuraMachine;
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
        return !level.isClientSide && type == ArcaneBlockEntities.HEAT_COLLECTOR.get() ? BlockEntityIgnisCollector::tick : null;
    }
}
