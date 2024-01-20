package martian.arcane.block;

import martian.arcane.block.entity.BlockEntityAuraInfuser;
import martian.arcane.block.entity.BlockEntityItemPylon;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockItemPylon extends Block implements EntityBlock {
    public BlockItemPylon() {
        super(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS));
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityItemPylon(pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide())
            return InteractionResult.SUCCESS;

        if (level.getBlockEntity(pos) instanceof BlockEntityAuraInfuser infuser) {
            ItemStack stack = player.getItemInHand(hand);

            if (!infuser.getItem().isEmpty()) {
                player.getInventory().placeItemBackInInventory(infuser.getItem());
                infuser.setItem(ItemStack.EMPTY);
            } else if (!stack.isEmpty()) {
                infuser.setItem(stack.copyWithCount(1));
                stack.shrink(1);
            }

            level.sendBlockUpdated(pos, state, state, 2);
        }

        return InteractionResult.CONSUME;
    }
}
