package martian.arcane.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiFunction;

public abstract class BlockWithEntity extends Block implements EntityBlock {
    protected final BiFunction<BlockPos, BlockState, BlockEntity> beSupplier;

    public BlockWithEntity(BlockBehaviour.Properties properties, BiFunction<BlockPos, BlockState, BlockEntity> beSupplier) {
        super(properties);
        this.beSupplier = beSupplier;
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return beSupplier.apply(pos, state);
    }
}
