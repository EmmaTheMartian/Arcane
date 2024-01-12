package martian.arcane.block;

import martian.arcane.block.entity.BlockEntityAuraNodi;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAuraNodi extends Block implements EntityBlock {
    public BlockAuraNodi() {
        super(BlockBehaviour.Properties.copy(Blocks.GLASS).noCollission());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new BlockEntityAuraNodi(pos, state);
    }
}