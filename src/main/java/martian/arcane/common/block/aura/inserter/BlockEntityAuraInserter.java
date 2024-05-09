package martian.arcane.common.block.aura.inserter;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.api.block.entity.IAuraInserter;
import martian.arcane.api.capability.aura.IAuraStorage;
import martian.arcane.common.block.aura.extractor.BlockAuraExtractor;
import martian.arcane.common.registry.ArcaneBlockEntities;
import martian.arcane.common.registry.ArcaneCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;

public class BlockEntityAuraInserter extends AbstractAuraBlockEntity implements IAuraInserter {
    public int insertRate;

    public BlockEntityAuraInserter(int maxAura, int auraLoss, int insertRate, BlockPos pos, BlockState state) {
        super(maxAura, auraLoss, false, true, ArcaneBlockEntities.AURA_INSERTER.get(), pos, state);
        this.insertRate = insertRate;
    }

    public BlockEntityAuraInserter(BlockPos pos, BlockState state) {
        super(ArcaneStaticConfig.AuraMaximums.AURA_INSERTERS, ArcaneStaticConfig.AuraLoss.COPPER_TIER, false, true, ArcaneBlockEntities.AURA_INSERTER.get(), pos, state);
        this.insertRate = ArcaneStaticConfig.Rates.COPPER_AURA_INSERTER_RATE;
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        AbstractAuraBlockEntity.tick(level, pos, state, blockEntity);

        if (blockEntity instanceof BlockEntityAuraInserter inserter) {
            // If there is a redstone signal coming into this block then we stop now
            if (level.hasNeighborSignal(pos))
                return;

            Direction facing = state.getValue(BlockAuraExtractor.FACING);
            BlockPos insertTarget = pos.offset(facing.getStepX(), facing.getStepY(), facing.getStepZ());
            if (!level.getBlockState(insertTarget).hasBlockEntity())
                return;

            BlockEntity e = level.getBlockEntity(insertTarget);
            if (e == null)
                return;

            LazyOptional<IAuraStorage> storage = e.getCapability(ArcaneCapabilities.AURA_STORAGE);
            if (!storage.isPresent())
                return;

            inserter.mapAuraStorage(aura -> {
                aura.sendAuraTo(storage.resolve().orElseThrow(), inserter.insertRate);
                return null;
            });
        }
    }
}
