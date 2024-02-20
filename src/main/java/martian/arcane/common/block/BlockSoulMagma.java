package martian.arcane.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MagmaBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockSoulMagma extends MagmaBlock {
    public BlockSoulMagma() {
        super(BlockBehaviour.Properties.copy(Blocks.MAGMA_BLOCK));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity) {
            entity.hurt(level.damageSources().hotFloor(), 4.0F);
        }

        super.stepOn(level, pos, state, entity);
    }
}
