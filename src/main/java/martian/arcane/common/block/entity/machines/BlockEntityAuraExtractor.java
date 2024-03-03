package martian.arcane.common.block.entity.machines;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.ArcaneTags;
import martian.arcane.api.block.BlockHelpers;
import martian.arcane.api.NBTHelpers;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.api.block.entity.IAuraInserter;
import martian.arcane.api.capability.IAuraStorage;
import martian.arcane.common.block.machines.BlockAuraExtractor;
import martian.arcane.common.registry.ArcaneBlockEntities;
import martian.arcane.common.registry.ArcaneCapabilities;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockEntityAuraExtractor extends AbstractAuraBlockEntity {
    private LazyOptional<IAuraStorage> cachedTarget = LazyOptional.empty();
    private @Nullable BlockPos targetPos;
    public int extractRate;

    public BlockEntityAuraExtractor(int maxAura, int auraLoss, int extractRate, BlockPos pos, BlockState state) {
        super(maxAura, auraLoss, false, false, ArcaneBlockEntities.AURA_EXTRACTOR.get(), pos, state);
        this.extractRate = extractRate;
    }

    public BlockEntityAuraExtractor(BlockPos pos, BlockState state) {
        super(ArcaneStaticConfig.Maximums.AURA_EXTRACTORS, ArcaneStaticConfig.AuraLoss.COPPER_TIER, false, false, ArcaneBlockEntities.AURA_EXTRACTOR.get(), pos, state);
        this.extractRate = ArcaneStaticConfig.Rates.COPPER_AURA_EXTRACTOR_RATE;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (targetPos != null)
            NBTHelpers.putBlockPos(nbt, NBTHelpers.KEY_EXTRACTOR_TARGET_POS, targetPos);
        else if (nbt.contains(NBTHelpers.KEY_EXTRACTOR_TARGET_POS))
            nbt.remove(NBTHelpers.KEY_EXTRACTOR_TARGET_POS);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains(NBTHelpers.KEY_EXTRACTOR_TARGET_POS))
            targetPos = NBTHelpers.getBlockPos(nbt, NBTHelpers.KEY_EXTRACTOR_TARGET_POS);
        else
            targetPos = null;
    }

    @Override
    public List<Component> getText(List<Component> text, boolean detailed) {
        if (targetPos != null)
            text.add(Component.translatable("messages.arcane.linked_to")
                    .append(targetPos.toShortString()));
        else
            text.add(Component.translatable("messages.arcane.not_linked")
                    .withStyle(ChatFormatting.DARK_RED));

        return super.getText(text, detailed);
    }

    public boolean validateTarget(Level level) {
        if (targetPos == null)
            return false;
        BlockState target = level.getBlockState(targetPos);
        return target.is(ArcaneTags.AURA_INSERTERS);
    }

    public static void setTarget(@NotNull BlockEntityAuraExtractor extractor, BlockEntityAuraInserter target) {
        extractor.targetPos = target.getBlockPos();
        BlockHelpers.sync(extractor);
    }

    public static void removeTarget(@NotNull BlockEntityAuraExtractor extractor) {
        extractor.targetPos = null;
        extractor.cachedTarget = LazyOptional.empty();
        BlockHelpers.sync(extractor);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        AbstractAuraBlockEntity.tick(level, pos, state, blockEntity);

        if (!level.isClientSide && blockEntity instanceof BlockEntityAuraExtractor extractor) {
            // If there is a redstone signal coming into this block then we stop now
            if (level.hasNeighborSignal(pos))
                return;

            // If targetPos is null we need to clear the cachedTarget and sync the extractor
            if (extractor.targetPos == null && extractor.cachedTarget.isPresent())
                removeTarget(extractor);
            // Refresh the cached target if cachedTarget is not present but targetPos is
            else if (!extractor.cachedTarget.isPresent() && extractor.targetPos != null) {
                BlockEntity e = level.getBlockEntity(extractor.targetPos);
                if (e instanceof IAuraInserter) {
                    LazyOptional<IAuraStorage> cap = e.getCapability(ArcaneCapabilities.AURA_STORAGE);
                    if (cap.isPresent())
                        extractor.cachedTarget = cap;
                } else
                    removeTarget(extractor);
            }

            if (extractor.targetPos != null && !extractor.validateTarget(level))
                removeTarget(extractor);

            // Extract aura from the target block
            extractor.mapAuraStorage(storage -> {
                Direction facing = state.getValue(BlockAuraExtractor.FACING);
                BlockPos extractFrom = pos.offset(facing.getStepX(), facing.getStepY(), facing.getStepZ());
                BlockEntity e = level.getBlockEntity(extractFrom);
                if (e == null)
                    return null;

                LazyOptional<IAuraStorage> cap = e.getCapability(ArcaneCapabilities.AURA_STORAGE);
                if (cap.isPresent() && cap.resolve().isPresent())
                    storage.extractAuraFrom(cap.resolve().get(), extractor.extractRate);

                // Send aura to the target inserter if able
                if (extractor.cachedTarget.isPresent())
                    storage.sendAuraTo(extractor.cachedTarget.resolve().orElseThrow(), extractor.extractRate);

                return null;
            });
        }
    }
}
