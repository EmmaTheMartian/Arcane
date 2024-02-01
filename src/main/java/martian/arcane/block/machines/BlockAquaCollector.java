package martian.arcane.block.machines;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.block.entity.BlockEntityAquaCollector;
import martian.arcane.block.entity.BlockEntityIgnisCollector;
import martian.arcane.registry.ArcaneBlockEntities;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockAquaCollector extends AbstractAuraMachine {
    public BlockAquaCollector() {
        super(PropertyHelpers.basicAuraMachine(), (pos, state) -> new BlockEntityAquaCollector(ArcaneStaticConfig.Maximums.COLLECTOR_MAX_AURA, pos, state));
    }

    @Override
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide() && type == ArcaneBlockEntities.AQUA_COLLECTOR.get() ? BlockEntityAquaCollector::tick : null;
    }
}
