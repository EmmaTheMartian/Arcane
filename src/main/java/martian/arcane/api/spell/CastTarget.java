package martian.arcane.api.spell;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public record CastTarget<T>(Type type, T value) {
    public enum Type {
        BLOCK,
        ENTITY,
        MISS
    }

    public static CastTarget<BlockPos> targetBlock(BlockPos pos) {
        return new CastTarget<>(Type.BLOCK, pos);
    }

    public static CastTarget<Entity> targetEntity(Entity entity) {
        return new CastTarget<>(Type.ENTITY, entity);
    }

    public static CastTarget<?> fromHitResult(HitResult hit) {
        return switch (hit.getType()) {
            case MISS -> new CastTarget<>(Type.MISS, null);
            case BLOCK -> targetBlock(((BlockHitResult) hit).getBlockPos());
            case ENTITY -> targetEntity(((EntityHitResult) hit).getEntity());
        };
    }
}
