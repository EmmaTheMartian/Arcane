package martian.arcane.api.block.entity;

import martian.arcane.ArcaneConfig;
import martian.arcane.api.IMachineTierable;
import martian.arcane.api.MachineTier;
import martian.arcane.api.NBTHelpers;
import martian.arcane.api.aura.AuraStorage;
import martian.arcane.api.aura.IAuraStorage;
import martian.arcane.api.aura.IMutableAuraStorage;
import martian.arcane.common.ArcaneContent;
import martian.arcane.common.networking.s2c.S2CSyncAuraAttachment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * If you use AbstractAuraBlockEntity.getData(ArcaneDataAttachments.AURA) to modify aura, make sure to invoke
 * AbstractAuraBlockEntity.markChanged() afterward.
 */
public abstract class AbstractAuraBlockEntity extends BlockEntity implements IAuraometerOutput, IMutableAuraStorage, IMachineTierable {
    protected final Lazy<AuraStorage> auraStorageCache = Lazy.of(() -> getData(ArcaneContent.DA_AURA));
    public final int defaultMaxAura;
    public int ticksUntilIdle = ArcaneConfig.ticksUntilConsideredIdle;
    public int ticksUntilNextAuraLoss = ArcaneConfig.auraLossTicks;
    public boolean hasSignal = false;

    public AbstractAuraBlockEntity(int maxAura, boolean extractable, boolean receivable, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        defaultMaxAura = maxAura;
        setData(ArcaneContent.DA_MACHINE_TIER, MachineTier.COPPER.get());
        setData(ArcaneContent.DA_AURA, new AuraStorage(maxAura, extractable, receivable));
    }

    @Override
    @NotNull
    public CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        CompoundTag tag = saveWithFullMetadata(registries);
        tag.putBoolean(NBTHelpers.KEY_HAS_SIGNAL, hasSignal);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        assert this.level != null;
        CompoundTag tag = getUpdateTag(this.level.registryAccess());
        hasSignal = tag.getBoolean(NBTHelpers.KEY_HAS_SIGNAL);
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public List<Component> getText(List<Component> text, IAuraometerOutput.Context context) {
        text.add(Component
                .translatable("messages.arcane.aura")
                .append(Integer.toString(getAuraStorage().getAura()))
                .append("/")
                .append(Integer.toString(getAuraStorage().getMaxAura()))
                .withStyle(ChatFormatting.LIGHT_PURPLE)
        );
        if (isUpgradable())
            text.add(Component
                    .translatable("messages.arcane.tier")
                    .append(getTier().getName()));
        return text;
    }

    // Default implementations for IMachineTierable
    public MachineTier getTier() {
        return getData(ArcaneContent.DA_MACHINE_TIER);
    }

    public boolean upgradeTo(MachineTier newTier) {
        setData(ArcaneContent.DA_MACHINE_TIER, newTier);
        setMaxAura(newTier.getMaxAuraForMachine(defaultMaxAura));
        return true;
    }

    public boolean isUpgradable() {
        return false;
    }

    // Aura management
    /**
     * Use the block entity's wrapper methods for IAuraStorage instead! They will automatically sync and update idleness
     * for the block entity.
     */
    @Deprecated
    @ApiStatus.Internal
    public AuraStorage getAuraStorage() {
        return auraStorageCache.get();
    }

    @ApiStatus.Internal
    public void setAuraStorage(IAuraStorage it) {
        var storage = getAuraStorage();
        storage.setAura(it.getAura());
        storage.setMaxAura(it.getMaxAura());
        storage.setInsertable(it.canInsert());
        storage.setExtractable(it.canExtract());
    }

    public <U> U mapAuraStorage(Function<? super IMutableAuraStorage, ? extends U> func) {
        return func.apply(this);
    }

    public void voidMapAuraStorage(Consumer<? super IMutableAuraStorage> func) {
        func.accept(this);
    }

    // Implementations for IAuraStorage
    public int getAura() {
        return getAuraStorage().getAura();
    }
    public boolean canExtract() {
        return getAuraStorage().canExtract();
    }
    public boolean canInsert() {
        return getAuraStorage().canInsert();
    }
    public int getMaxAura() {
        return getAuraStorage().getMaxAura();
    }
    public void setAura(int value) {
        getAuraStorage().setAura(value);
        markChanged();
    }
    public void setMaxAura(int value) {
        getAuraStorage().setMaxAura(value);
        markChanged();
    }
    public void setExtractable(boolean value) {
        getAuraStorage().setExtractable(value);
        markChanged();
    }
    public void setInsertable(boolean value) {
        getAuraStorage().setInsertable(value);
        markChanged();
    }
    public void extractAuraFrom(IMutableAuraStorage other, int maxExtract) {
        getAuraStorage().extractAuraFrom(other, maxExtract);
        markChanged();
        if (other instanceof AbstractAuraBlockEntity)
            ((AbstractAuraBlockEntity) other).markChanged();
    }
    public void sendAuraTo(IMutableAuraStorage other, int maxPush) {
        getAuraStorage().sendAuraTo(other, maxPush);
        markChanged();
        if (other instanceof AbstractAuraBlockEntity)
            ((AbstractAuraBlockEntity) other).markChanged();
    }
    public int addAura(int value) {
        int overflow = getAuraStorage().addAura(value);
        markChanged();
        return overflow;
    }
    public int removeAura(int value) {
        int underflow = getAuraStorage().removeAura(value);
        markChanged();
        return underflow;
    }
    // Non-idle-update methods
    public void setAuraNoUpdateIdle(int value) {
        getAuraStorage().setAura(value);
        markChanged(false);
    }
    public int addAuraNoUpdateIdle(int value) {
        int overflow = getAuraStorage().addAura(value);
        markChanged(false);
        return overflow;
    }
    public int removeAuraNoUpdate(int value) {
        int underflow = getAuraStorage().removeAura(value);
        markChanged(false);
        return underflow;
    }
    public void sendAuraToNoUpdate(IMutableAuraStorage other, int maxPush, boolean updateIdleness) {
        getAuraStorage().sendAuraTo(other, maxPush);
        markChanged(false);
    }
    public void extractAuraFromNoUpdate(IMutableAuraStorage other, int maxExtract, boolean updateIdleness) {
        getAuraStorage().extractAuraFrom(other, maxExtract);
        markChanged(false);
    }

    // Updating
    @ApiStatus.Internal
    public void markChanged(boolean updateIdle) {
        if (updateIdle)
            setNotIdle(this);
        setChanged();
        if (level != null)
            level.getChunk(getBlockPos()).setUnsaved(true);
        syncWithClients();
    }

    @ApiStatus.Internal
    public void markChanged() {
        markChanged(true);
    }

    public static void setNotIdle(BlockEntity entity) {
        if (entity instanceof AbstractAuraBlockEntity machine)
            machine.ticksUntilIdle = ArcaneConfig.ticksUntilConsideredIdle;
    }

    @ApiStatus.Internal
    public void syncWithClients() {
        if (level != null && !level.isClientSide)
            PacketDistributor.sendToAllPlayers(new S2CSyncAuraAttachment(getAuraStorage().freeze(), getBlockPos()));
    }

    public static void tickForAuraLoss(Level level, AbstractAuraBlockEntity machine) {
        if (!level.isClientSide && !isAuraLossDisabled() && --machine.ticksUntilIdle <= 0 && --machine.ticksUntilNextAuraLoss <= 0) {
            machine.removeAuraNoUpdate(machine.getTier().auraLoss());
            machine.ticksUntilNextAuraLoss = ArcaneConfig.auraLossTicks;
        }
    }

    public static boolean isAuraLossDisabled() {
        return ArcaneConfig.ticksUntilConsideredIdle == -1;
    }
}
