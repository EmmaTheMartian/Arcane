package martian.arcane.block.entity;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.api.capability.IAuraStorage;
import martian.arcane.registry.ArcaneBlockEntities;
import martian.arcane.registry.ArcaneCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;

public class BlockEntityAuraInserter extends AbstractAuraBlockEntity {
    public BlockEntityAuraInserter(BlockPos pos, BlockState state) {
        super(ArcaneStaticConfig.AuraMaximums.AURA_INSERTER, false, true, ArcaneBlockEntities.AURA_INSERTER_BE.get(), pos, state);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T blockEntity) {
        if (blockEntity instanceof BlockEntityAuraInserter inserter) {
            BlockPos insertTarget = blockPos.below();
            if (!level.getBlockState(insertTarget).hasBlockEntity())
                return;

            BlockEntity e = level.getBlockEntity(insertTarget);
            if (e == null)
                return;

            LazyOptional<IAuraStorage> storage = e.getCapability(ArcaneCapabilities.AURA_STORAGE);
            if (!storage.isPresent())
                return;

            inserter.mapAuraStorage(aura -> {
                aura.sendAuraTo(storage.resolve().orElseThrow(), -1);
                return null;
            });
        }
    }
}
