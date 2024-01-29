package martian.arcane.block;

import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.block.entity.BlockEntityAuraExtractor;
import martian.arcane.registry.ArcaneBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

public class BlockAuraExtractor extends AbstractAuraMachine {
    public static Map<Direction, VoxelShape> SHAPES = Map.of(
            Direction.UP, Block.box(0, 14, 0, 16, 16, 16),
            Direction.DOWN, Block.box(0, 0, 0, 16, 2, 16),
            Direction.NORTH, Block.box(0, 0, 0, 16, 16, 2),
            Direction.SOUTH, Block.box(0, 0, 14, 16, 16, 16),
            Direction.EAST, Block.box(14, 0, 0, 16, 16, 16),
            Direction.WEST, Block.box(0, 0, 0, 2, 16, 16)
    );

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BlockAuraExtractor(int maxAura, int extractRate) {
        super(PropertyHelpers.basicAuraMachine().noOcclusion().noCollission(), (pos, state) -> new BlockEntityAuraExtractor(maxAura, extractRate, pos, state));
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.DOWN));
    }

    @SuppressWarnings("deprecation")
    @Override
    @ParametersAreNonnullByDefault
    @NotNull
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide() && type == ArcaneBlockEntities.AURA_EXTRACTOR.get() ? BlockEntityAuraExtractor::tick : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
    }
}
