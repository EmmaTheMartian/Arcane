package martian.arcane.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.stream.Stream;

public class AOEHelpers {
    // Based on https://github.com/CoFH/CoFHCore/blob/62908ffc3e041ed204b20f98c15334fe1d0dbbe3/src/main/java/cofh/core/util/helpers/AreaEffectHelper.java#L121
    // Yeah I know it does not look similar whatsoever. I needed to reference the math and numbers, and I am still going to credit Team CoFH for that!
    public static Stream<BlockPos> streamAOE(BlockPos pos, Direction direction, int radius) {
        final int yMin = -1;
        final int yMax = 2 * radius - 1;
        return switch (direction) {
            case UP, DOWN ->
                    BlockPos.betweenClosedStream(pos.offset(-radius, 0, -radius), pos.offset(radius, 0, radius));
            case NORTH, SOUTH ->
                    BlockPos.betweenClosedStream(pos.offset(-radius, yMin, 0), pos.offset(radius, yMax, 0));
            default ->
                    BlockPos.betweenClosedStream(pos.offset(0, yMin, -radius), pos.offset(0, yMax, radius));
        };
    }
}
