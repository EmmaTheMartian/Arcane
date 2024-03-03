package martian.arcane.api.block;

import martian.arcane.api.spell.CastContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class PreservableWeatherableBlock extends WeatherableBlock implements IPreservable {
    public PreservableWeatherableBlock(@Nullable Supplier<BlockState> nextBlock, Properties properties) {
        super(nextBlock, properties);
    }

    @Override
    public void onPreserve(Level level, BlockPos pos, BlockState state, CastContext context) {
        level.setBlockAndUpdate(pos, state.setValue(WeatherableBlock.WAXED, true));
    }
}
