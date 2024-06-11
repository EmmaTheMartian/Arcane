package martian.arcane.api.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IAuraWrench {
    default boolean getIsWrench(Level level, Player player, BlockState state, BlockPos pos, ItemStack stack) {
        return true;
    }
}
