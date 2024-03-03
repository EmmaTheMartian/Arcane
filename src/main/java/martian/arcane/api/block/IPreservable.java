package martian.arcane.api.block;

import martian.arcane.api.spell.CastContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IPreservable {
    void onPreserve(Level level, BlockPos pos, BlockState state, CastContext context);
}
