package martian.arcane.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.function.Supplier;

public class WeatherableBlock extends Block {
    public static final Property<Boolean> WAXED = BooleanProperty.create("waxed");

    public final @Nullable Supplier<BlockState> nextBlock;
    protected final int weatherChanceEachRandomTick = 10;

    public WeatherableBlock(@Nullable Supplier<BlockState> nextBlock, Properties properties) {
        super(properties);
        this.nextBlock = nextBlock;
        registerDefaultState(defaultBlockState().setValue(WAXED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WAXED);
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return nextBlock != null && !state.getValue(WAXED);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(0, weatherChanceEachRandomTick) == 0)
            level.setBlockAndUpdate(pos, Objects.requireNonNull(nextBlock).get());
    }
}
