package martian.arcane.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class MathHelpers {
    public static BlockPos vec3toBlockPos(Vec3 v) {
        return new BlockPos((int)v.x, (int)v.y, (int)v.z);
    }
}
