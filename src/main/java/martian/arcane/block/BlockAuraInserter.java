package martian.arcane.block;

import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.block.entity.BlockEntityAuraInserter;
import martian.arcane.registry.ArcaneBlockEntities;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockAuraInserter extends AbstractAuraMachine {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BlockAuraInserter() {
        super(PropertyHelpers.basicAuraMachine().noCollission().noOcclusion(), BlockEntityAuraInserter::new);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.DOWN));
    }

    @Override
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide() && type == ArcaneBlockEntities.AURA_INSERTER_BE.get() ? BlockEntityAuraInserter::tick : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }
}
