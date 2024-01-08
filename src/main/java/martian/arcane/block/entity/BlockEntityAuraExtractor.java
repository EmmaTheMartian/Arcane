package martian.arcane.block.entity;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.NBTHelpers;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.api.capability.IAuraStorage;
import martian.arcane.registry.ArcaneBlockEntities;
import martian.arcane.registry.ArcaneCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class BlockEntityAuraExtractor extends AbstractAuraBlockEntity {
    private LazyOptional<IAuraStorage> cachedTarget;
    public BlockPos targetPos;

    public BlockEntityAuraExtractor(BlockPos pos, BlockState state) {
        super(ArcaneStaticConfig.AuraMaximums.AURA_EXTRACTOR, false, false, ArcaneBlockEntities.AURA_EXTRACTOR_BE.get(), pos, state);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (targetPos != null)
            NBTHelpers.putBlockPos(nbt, NBTHelpers.KEY_EXTRACTOR_TARGET_POS, targetPos);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        targetPos = NBTHelpers.getBlockPos(nbt, NBTHelpers.KEY_EXTRACTOR_TARGET_POS);
    }

    public static void setTarget(BlockEntityAuraExtractor extractor, BlockEntityAuraInserter target) {
        extractor.targetPos = target.getBlockPos();
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T blockEntity) {
        if (blockEntity instanceof BlockEntityAuraExtractor extractor) {
            if (extractor.targetPos == null)
                return;

            LazyOptional<IAuraStorage> target = Objects.requireNonNullElseGet(extractor.cachedTarget, () ->
                    Objects.requireNonNull(level.getBlockEntity(extractor.targetPos))
                            .getCapability(ArcaneCapabilities.AURA_STORAGE));

            if (extractor.cachedTarget == null)
                extractor.cachedTarget = target;

            if (!target.isPresent())
                return;

            extractor.mapAuraStorage(storage -> {
                { // Extract from the block below the extractor
                    BlockPos extractFrom = blockPos.below();
                    if (!level.getBlockState(extractFrom).hasBlockEntity())
                        return null;

                    BlockEntity e = level.getBlockEntity(extractFrom);
                    if (e == null)
                        return null;

                    LazyOptional<IAuraStorage> cap = e.getCapability(ArcaneCapabilities.AURA_STORAGE);
                    if (cap.isPresent() && cap.resolve().isPresent()) {
                        storage.extractAuraFrom(cap.resolve().get(), ArcaneStaticConfig.Rates.AURA_EXTRACTOR_RATE);
                    }
                }

                // Send to the target inserter
                storage.sendAuraTo(target.resolve().orElseThrow(), -1);

                return null;
            });
        }
    }
}
