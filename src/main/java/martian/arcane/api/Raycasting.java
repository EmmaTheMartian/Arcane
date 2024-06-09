package martian.arcane.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Raycasting {
    public static @Nullable BlockHitResult blockRaycast(Entity entity, double reach, boolean hitFluids) {
        HitResult hit = entity.pick(reach, 0, hitFluids);
        if (hit.getType() != HitResult.Type.BLOCK) {
            return null;
        }
        return (BlockHitResult)hit;
    }

    public static HitResult raycast(Entity entity, double reach, boolean hitFluids) {
        return entity.pick(reach, 0, hitFluids);
    }

    // Based on Create: Solar Powered, thanks for letting me do this Ginger :P
    public static List<BlockPos> raycastAndGetBlockPositions(Level level, BlockPos from, BlockPos to) {
        List<BlockPos> traversedBlocks = new ArrayList<>();
        Vec3 origin = from.getCenter();
        Vec3 target = to.getCenter();

//        ClipContext context = new ClipContext(origin, target, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, null);
        ClipContext context = new ClipContext(origin, target, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, CollisionContext.empty());

        BlockGetter.traverseBlocks(origin, target, context,
                (ctx, pos) -> {
                    if (from == pos)
                        return null;

                    traversedBlocks.add(pos.immutable());
                    BlockState state = level.getBlockState(pos);
                    return level.clipWithInteractionOverride(
                            ctx.getFrom(),
                            ctx.getTo(),
                            pos,
                            ctx.getBlockShape(state, level, pos),
                            state
                    );
                },
                ctx -> {
                    Vec3 direction = ctx.getFrom().subtract(ctx.getTo());
                    return BlockHitResult.miss(
                            ctx.getTo(),
                            Direction.getNearest(direction.x, direction.y, direction.z),
                            BlockPos.containing(ctx.getTo())
                    );
                }
        );

        return traversedBlocks;
    }

    public static void traverseBetweenPoints(Vec3 from, Vec3 to, double step, double endRange, Function<Vec3, Boolean> tester) {
        if (from.equals(to))
            return;

        double x = from.x, y = from.y, z = from.z;
        Vec3 vel = from.vectorTo(to).normalize();
        double remainingX = Math.abs(to.x - from.x);
        double remainingY = Math.abs(to.y - from.y);
        double remainingZ = Math.abs(to.z - from.z);

        while (tester.apply(new Vec3(x, y, z))) {
            if (remainingX > 0) {
                x += vel.x * step;
                remainingX -= step;
            } else if (remainingY > 0) {
                y += vel.y * step;
                remainingY -= step;
            } else if (remainingZ > 0) {
                z += vel.z * step;
                remainingZ -= step;
            }

            if (remainingX <= endRange && remainingY <= endRange && remainingZ <= endRange) {
                return;
            }
        }
    }
}
