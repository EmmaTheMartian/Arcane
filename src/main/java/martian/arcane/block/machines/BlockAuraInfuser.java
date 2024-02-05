package martian.arcane.block.machines;

import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.block.entity.BlockEntityAuraInfuser;
import martian.arcane.registry.ArcaneBlockEntities;
import martian.arcane.registry.ArcaneItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockAuraInfuser extends AbstractAuraMachine {
    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 8, 14);

    public BlockAuraInfuser() {
        super(PropertyHelpers.basicAuraMachine().noOcclusion(), BlockEntityAuraInfuser::new);
    }

    @SuppressWarnings("deprecation")
    @Override
    @ParametersAreNonnullByDefault
    @NotNull
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide() && type == ArcaneBlockEntities.AURA_INFUSER.get() ? BlockEntityAuraInfuser::tick : null;
    }

    @SuppressWarnings("deprecation")
    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (level.getBlockEntity(pos) instanceof BlockEntityAuraInfuser infuser) {
            ItemStack stack = player.getItemInHand(hand);

            if (stack.is(ArcaneItems.AURA_WRENCH.get())) {
                infuser.nextMode();
            } else if (!infuser.getItem().isEmpty()) {
                player.getInventory().placeItemBackInInventory(infuser.getItem());
                infuser.setItem(ItemStack.EMPTY);
            } else if (!stack.isEmpty() && infuser.getItem().isEmpty()) {
                infuser.setItem(stack.copyWithCount(1));
                stack.shrink(1);
            }

            level.sendBlockUpdated(pos, state, state, 2);
        }

        return InteractionResult.CONSUME;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof BlockEntityAuraInfuser infuser) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), infuser.getItem());
            // Note: If comparator support is added for this then it'll need to be updated here
            // See ChestBlock#onRemove
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
