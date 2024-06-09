package martian.arcane.common.block.aura.infuser;

import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.common.registry.ArcaneBlockEntities;
import martian.arcane.common.registry.ArcaneItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
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

public class BlockAuraInfuser extends AbstractAuraMachine {
    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 8, 14);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlockAuraInfuser(int maxAura, int auraLoss) {
        super(PropertyHelpers.basicAuraMachine().noOcclusion(), (pos, state) -> new BlockEntityAuraInfuser(maxAura, auraLoss, pos, state));
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    @ParametersAreNonnullByDefault
    @NotNull
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide && type == ArcaneBlockEntities.AURA_INFUSER.get() ? BlockEntityAuraInfuser::tick : null;
    }

    @Override
    @ParametersAreNonnullByDefault
    @NotNull
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }

        if (level.getBlockEntity(pos) instanceof BlockEntityAuraInfuser infuser) {
            /*if (stack.is(ArcaneItems.AURA_WRENCH.get())) {
                infuser.nextMode();
            } else */if (!infuser.getItem().isEmpty()) {
                player.getInventory().placeItemBackInInventory(infuser.getItem());
                infuser.setItem(ItemStack.EMPTY);
            } else if (!stack.isEmpty() && infuser.getItem().isEmpty()) {
                infuser.setItem(stack.copyWithCount(1));
                stack.shrink(1);
            }

            level.sendBlockUpdated(pos, state, state, 2);
        }

        return ItemInteractionResult.CONSUME;
    }

    @Override
    @ParametersAreNonnullByDefault
    @NotNull
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(level, pos, state, player);
        if (level.getBlockEntity(pos) instanceof BlockEntityAuraInfuser infuser)
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), infuser.getItem()));
        return state;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof BlockEntityAuraInfuser infuser)
            return infuser.getItem().getCount() > 0 ? 15 : 0;
        return 0;
    }
}
