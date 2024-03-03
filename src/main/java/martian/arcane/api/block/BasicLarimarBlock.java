package martian.arcane.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.function.Supplier;

public class BasicLarimarBlock extends PreservableWeatherableBlock {
    public BasicLarimarBlock(@Nullable Supplier<BlockState> nextBlock, Properties properties) {
        super(nextBlock, properties);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(0, weatherChanceEachRandomTick) == 0 && BlockHelpers.exposedToSunlight(level, pos))
            level.setBlockAndUpdate(pos, Objects.requireNonNull(nextBlock).get());
    }
}
