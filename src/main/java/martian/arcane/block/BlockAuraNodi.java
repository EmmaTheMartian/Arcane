package martian.arcane.block;

import martian.arcane.ArcaneConfig;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.api.capability.IAuraStorage;
import martian.arcane.block.entity.BlockEntityAuraNodi;
import martian.arcane.registry.ArcaneBlockEntities;
import martian.arcane.registry.ArcaneItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BlockAuraNodi extends Block implements EntityBlock {
    public BlockAuraNodi() {
        super(BlockBehaviour.Properties.copy(Blocks.GLASS).noCollission());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new BlockEntityAuraNodi(pos, state);
    }
}
