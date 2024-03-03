package martian.arcane.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockHelpers {
    public static void sync(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        level.sendBlockUpdated(pos, state, state, 2);
    }

    public static void sync(BlockEntity be) {
        if (be.getLevel() != null) {
            sync(be.getLevel(), be.getBlockPos());
            be.setChanged();
        }
    }

    public static boolean exposedToSunlight(Level level, BlockPos pos) {
        do {
            pos = pos.above();
            if (!level.getBlockState(pos).propagatesSkylightDown(level, pos))
                return false;
        } while (pos.getY() < level.getHeight());
        return true;
    }
}
