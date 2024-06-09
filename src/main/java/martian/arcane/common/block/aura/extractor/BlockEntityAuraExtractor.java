package martian.arcane.common.block.aura.extractor;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.ArcaneTags;
import martian.arcane.api.NBTHelpers;
import martian.arcane.api.Raycasting;
import martian.arcane.api.block.BlockHelpers;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.api.aura.IMutableAuraStorage;
import martian.arcane.common.block.aura.inserter.BlockEntityAuraInserter;
import martian.arcane.common.registry.ArcaneBlockEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityAuraExtractor extends AbstractAuraBlockEntity {
    public @Nullable IMutableAuraStorage target = null;
    public List<BlockPos> blocksToWatch = new ArrayList<>();
    public @Nullable BlockPos targetPos;
    public int extractRate;

    public BlockEntityAuraExtractor(int maxAura, int auraLoss, int extractRate, BlockPos pos, BlockState state) {
        super(maxAura, auraLoss, false, false, ArcaneBlockEntities.AURA_EXTRACTOR.get(), pos, state);
        this.extractRate = extractRate;
    }

    public BlockEntityAuraExtractor(BlockPos pos, BlockState state) {
        super(ArcaneStaticConfig.AuraMaximums.AURA_EXTRACTORS, ArcaneStaticConfig.AuraLoss.COPPER_TIER, false, false, ArcaneBlockEntities.AURA_EXTRACTOR.get(), pos, state);
        this.extractRate = ArcaneStaticConfig.Rates.COPPER_AURA_EXTRACTOR_RATE;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(nbt, provider);
        if (targetPos != null)
            NBTHelpers.putBlockPos(nbt, NBTHelpers.KEY_EXTRACTOR_TARGET_POS, targetPos);
        else if (nbt.contains(NBTHelpers.KEY_EXTRACTOR_TARGET_POS))
            nbt.remove(NBTHelpers.KEY_EXTRACTOR_TARGET_POS);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);
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

    public boolean hasObstructions(Level level) {
        return blocksToWatch.stream()
                .map(level::getBlockState)
                .anyMatch(state -> state.is(ArcaneTags.BLOCKS_AURA_FLOW));
    }

    public static void setTarget(@NotNull BlockEntityAuraExtractor extractor, @NotNull BlockEntityAuraInserter target) {
        extractor.targetPos = target.getBlockPos();
        BlockHelpers.sync(extractor);
        extractor.blocksToWatch = Raycasting.raycastAndGetBlockPositions(extractor.level, extractor.getBlockPos(), target.getBlockPos());
    }

    public static void removeTarget(@NotNull BlockEntityAuraExtractor extractor) {
        extractor.targetPos = null;
        extractor.target = null;
        extractor.blocksToWatch.clear();
        BlockHelpers.sync(extractor);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        AbstractAuraBlockEntity.tick(level, pos, state, blockEntity);

        if (!level.isClientSide && blockEntity instanceof BlockEntityAuraExtractor extractor) {
            // If there is a redstone signal coming into this block then we stop now
            if (level.hasNeighborSignal(pos))
                return;

            // If targetPos is null we need to clear the cachedTarget and sync the extractor
            if (extractor.targetPos == null && extractor.target != null)
                removeTarget(extractor);
            // Refresh the cached target if cachedTarget is not present but targetPos is
            else if (extractor.target == null && extractor.targetPos != null) {
                BlockEntity e = level.getBlockEntity(extractor.targetPos);
                if (e instanceof IMutableAuraStorage be)
                    extractor.target = be;
                else
                    removeTarget(extractor);
            }

            if (extractor.targetPos != null && !extractor.validateTarget(level))
                removeTarget(extractor);

            // Check for blocks that obstruct aura flow
            if (extractor.hasObstructions(level))
                return;

            // Extract aura from the target block
            extractor.mapAuraStorage(storage -> {
                Direction facing = state.getValue(BlockAuraExtractor.FACING);
                BlockPos extractFrom = pos.offset(facing.getStepX(), facing.getStepY(), facing.getStepZ());
                BlockEntity e = level.getBlockEntity(extractFrom);
                if (e == null)
                    return null;

                if (e instanceof IMutableAuraStorage eAura)
                    storage.extractAuraFrom(eAura, extractor.extractRate);

                // Send aura to the target inserter if able
                if (extractor.target != null)
                    storage.sendAuraTo(extractor.target, extractor.extractRate);

                return null;
            });
        }
    }
}
