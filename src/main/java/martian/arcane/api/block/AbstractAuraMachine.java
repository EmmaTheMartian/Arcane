package martian.arcane.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public abstract class AbstractAuraMachine extends Block implements EntityBlock {
    private final BiFunction<BlockPos, BlockState, BlockEntity> beSupplier;

    public AbstractAuraMachine(BlockBehaviour.Properties properties, BiFunction<BlockPos, BlockState, BlockEntity> beSupplier) {
        super(properties);
        this.beSupplier = beSupplier;
    }

    @Override
    @NotNull
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return beSupplier.apply(pos, state);
    }
}