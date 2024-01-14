package martian.arcane.block;

import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.block.entity.BlockEntityAuraExtractor;
import martian.arcane.registry.ArcaneBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockAuraExtractor extends AbstractAuraMachine {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BlockAuraExtractor() {
        super(PropertyHelpers.basicAuraMachine().noOcclusion().noCollission(), BlockEntityAuraExtractor::new);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.DOWN));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {

        super.animateTick(state, level, pos, rand);
        if (level.getBlockEntity(pos) instanceof BlockEntityAuraExtractor extractor) {
            BlockPos tp = extractor.targetPos;

            if (tp == null)
                return;

            Vec3 a = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            Vec3 b = new Vec3(tp.getX() + 0.5, tp.getY() + 0.5, tp.getZ() + 0.5);
            Vec3 vel = b.subtract(a).normalize().scale(0.2F);

            level.addParticle(
                    ParticleTypes.ENCHANT,
                    true,
                    a.x, a.y, a.z,
                    vel.x, vel.y, vel.z
            );
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide() && type == ArcaneBlockEntities.AURA_EXTRACTOR_BE.get() ? BlockEntityAuraExtractor::tick : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }
}
