package martian.arcane.common.block.machines;

import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.common.block.entity.machines.BlockEntityAuraBasin;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockAuraBasin extends AbstractAuraMachine {
    public static final VoxelShape SHAPE_INSIDE = Block.box(1, 3, 1, 15, 16, 15);
    public static final VoxelShape SHAPE = Shapes.join(Shapes.block(), SHAPE_INSIDE, BooleanOp.ONLY_FIRST);

    public BlockAuraBasin() {
        super(PropertyHelpers.basicAuraMachine().noOcclusion(), BlockEntityAuraBasin::new);
    }

    @SuppressWarnings("deprecation")
    @Override
    @ParametersAreNonnullByDefault
    @NotNull
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
