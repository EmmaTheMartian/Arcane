package martian.arcane.block;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.block.entity.BlockEntityIgnisCollector;
import martian.arcane.registry.ArcaneBlockEntities;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockIgnisCollector extends AbstractAuraMachine {
    public BlockIgnisCollector() {
        super(PropertyHelpers.basicAuraMachine(), (pos, state) -> new BlockEntityIgnisCollector(ArcaneStaticConfig.Maximums.COLLECTOR_MAX_AURA, pos, state));
    }

    @Override
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide() && type == ArcaneBlockEntities.IGNIS_COLLECTOR.get() ? BlockEntityIgnisCollector::tick : null;
    }
}
