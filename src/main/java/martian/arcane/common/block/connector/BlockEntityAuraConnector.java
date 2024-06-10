package martian.arcane.common.block.connector;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.ArcaneTags;
import martian.arcane.api.NBTHelpers;
import martian.arcane.api.Raycasting;
import martian.arcane.api.aura.IMutableAuraStorage;
import martian.arcane.api.block.BlockHelpers;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
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

public class BlockEntityAuraConnector extends AbstractAuraBlockEntity {
    public enum Mode {
        RELAY,
        INSERT,
        EXTRACT
    }

    public @Nullable IMutableAuraStorage target = null;
    public List<BlockPos> blocksToWatch = new ArrayList<>();
    public @Nullable BlockPos targetPos;
    public Mode mode = Mode.RELAY;

    public BlockEntityAuraConnector(BlockPos pos, BlockState state) {
        super(ArcaneStaticConfig.AuraMaximums.AURA_EXTRACTORS, false, true, ArcaneBlockEntities.AURA_CONNECTOR.get(), pos, state);
    }

    @Override
    public boolean isUpgradable() {
        return true;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(nbt, provider);
        nbt.putString(NBTHelpers.KEY_MODE, mode.toString());
        if (targetPos != null)
            NBTHelpers.putBlockPos(nbt, NBTHelpers.KEY_EXTRACTOR_TARGET_POS, targetPos);
        else if (nbt.contains(NBTHelpers.KEY_EXTRACTOR_TARGET_POS))
            nbt.remove(NBTHelpers.KEY_EXTRACTOR_TARGET_POS);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);
        mode = Mode.valueOf(nbt.getString(NBTHelpers.KEY_MODE));
        if (nbt.contains(NBTHelpers.KEY_EXTRACTOR_TARGET_POS))
            targetPos = NBTHelpers.getBlockPos(nbt, NBTHelpers.KEY_EXTRACTOR_TARGET_POS);
        else
            targetPos = null;
    }

    @Override
    public List<Component> getText(List<Component> text, boolean detailed) {
        if (mode != Mode.INSERT) {
            if (targetPos != null)
                text.add(Component.translatable("messages.arcane.linked_to")
                        .append(targetPos.toShortString()));
            else
                text.add(Component.translatable("messages.arcane.not_linked")
                        .withStyle(ChatFormatting.DARK_RED));
        }

        text.add(Component.translatable("messages.arcane.mode").append(mode.toString()));

        return super.getText(text, detailed);
    }

    public boolean validateTarget(Level level) {
        if (targetPos == null)
            return false;
        BlockState target = level.getBlockState(targetPos);
        return target.getBlock() instanceof BlockAuraConnector &&
                level.getBlockEntity(targetPos) instanceof BlockEntityAuraConnector connector &&
                connector.mode != Mode.EXTRACT;
    }

    public boolean hasObstructions(Level level) {
        return blocksToWatch.stream()
                .map(level::getBlockState)
                .anyMatch(state -> state.is(ArcaneTags.BLOCKS_AURA_FLOW));
    }

    public void cycleMode() {
        mode = switch (mode) {
            case RELAY -> Mode.INSERT;
            case INSERT -> Mode.EXTRACT;
            case EXTRACT -> Mode.RELAY;
        };
    }

    public void setTarget(@NotNull BlockEntityAuraConnector target) {
        targetPos = target.getBlockPos();
        BlockHelpers.sync(this);
        blocksToWatch = Raycasting.raycastAndGetBlockPositions(level, getBlockPos(), target.getBlockPos());
    }

    public void removeTarget() {
        targetPos = null;
        target = null;
        blocksToWatch.clear();
        BlockHelpers.sync(this);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        AbstractAuraBlockEntity.tick(level, pos, state, blockEntity);

        if (!level.isClientSide && blockEntity instanceof BlockEntityAuraConnector connector) {
            // If there is a redstone signal coming into this block then we stop now
            if (level.hasNeighborSignal(pos))
                return;

            if (connector.mode != Mode.INSERT) {
                // If targetPos is null we need to clear the cachedTarget and sync the extractor
                if (connector.targetPos == null && connector.target != null)
                    connector.removeTarget();
                // Refresh the cached target if cachedTarget is not present but targetPos is
                else if (connector.target == null && connector.targetPos != null) {
                    BlockEntity e = level.getBlockEntity(connector.targetPos);
                    if (e instanceof IMutableAuraStorage be)
                        connector.target = be;
                    else
                        connector.removeTarget();
                }

                if (connector.targetPos != null && !connector.validateTarget(level))
                    connector.removeTarget();

                // Check for blocks that obstruct aura flow
                if (connector.hasObstructions(level))
                    return;
            }

            connector.voidMapAuraStorage(storage -> {
                switch (connector.mode) {
                    case RELAY -> {
                        // Send aura to the target inserter if able
                        if (connector.target != null)
                            storage.sendAuraTo(connector.target, connector.getTier().ioRate());
                    }
                    case INSERT -> {
                        Direction facing = state.getValue(BlockAuraConnector.FACING);
                        BlockPos insertTarget = pos.offset(facing.getStepX(), facing.getStepY(), facing.getStepZ());
                        if (!level.getBlockState(insertTarget).hasBlockEntity())
                            break;

                        BlockEntity e = level.getBlockEntity(insertTarget);
                        if (e == null)
                            break;

                        if (e instanceof IMutableAuraStorage eAura)
                            connector.voidMapAuraStorage(aura -> aura.sendAuraTo(eAura, connector.getTier().ioRate()));
                    }
                    case EXTRACT -> {
                        // Extract aura from the target block
                        Direction facing = state.getValue(BlockAuraConnector.FACING);
                        BlockPos extractFrom = pos.offset(facing.getStepX(), facing.getStepY(), facing.getStepZ());
                        BlockEntity e = level.getBlockEntity(extractFrom);
                        if (e == null)
                            break;

                        if (e instanceof IMutableAuraStorage eAura)
                            storage.extractAuraFrom(eAura, connector.getTier().ioRate());

                        // Send aura to the target inserter if able
                        if (connector.target != null)
                            storage.sendAuraTo(connector.target, connector.getTier().ioRate());
                    }
                }
            });
        }
    }
}
