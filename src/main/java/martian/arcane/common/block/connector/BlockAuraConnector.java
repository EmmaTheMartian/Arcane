package martian.arcane.common.block.connector;

import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.api.block.BlockHelpers;
import martian.arcane.api.item.IAuraConfigurator;
import martian.arcane.common.ArcaneContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

public class BlockAuraConnector extends AbstractAuraMachine {
    public static Map<Direction, VoxelShape> SHAPES = Map.of(
            Direction.UP, Block.box(0, 14, 0, 16, 16, 16),
            Direction.DOWN, Block.box(0, 0, 0, 16, 2, 16),
            Direction.NORTH, Block.box(0, 0, 0, 16, 16, 2),
            Direction.SOUTH, Block.box(0, 0, 14, 16, 16, 16),
            Direction.EAST, Block.box(14, 0, 0, 16, 16, 16),
            Direction.WEST, Block.box(0, 0, 0, 2, 16, 16)
    );

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BlockAuraConnector() {
        super(PropertyHelpers.basicAuraMachine().noOcclusion(), BlockEntityAuraConnector::new);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.DOWN));
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (
                stack.getItem() instanceof IAuraConfigurator auraConfigurator &&
                auraConfigurator.getIsConfigurator(level, player, state, pos, stack) &&
                level.getBlockEntity(pos) instanceof BlockEntityAuraConnector connector
        ) {
            if (level.isClientSide)
                return ItemInteractionResult.SUCCESS;
            connector.cycleMode();
            BlockHelpers.sync(connector);
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    @ParametersAreNonnullByDefault
    @NotNull
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == ArcaneContent.AURA_CONNECTOR.tile().get() ? BlockEntityAuraConnector::tick : null;
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
