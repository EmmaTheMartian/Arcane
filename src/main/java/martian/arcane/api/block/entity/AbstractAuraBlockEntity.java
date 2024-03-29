package martian.arcane.api.block.entity;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.NBTHelpers;
import martian.arcane.api.capability.AuraStorageBlockEntityProvider;
import martian.arcane.api.capability.BlockEntityAuraStorage;
import martian.arcane.api.capability.IAuraStorage;
import martian.arcane.common.registry.ArcaneCapabilities;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractAuraBlockEntity extends BlockEntity implements IAuraometerOutput {
    protected final AuraStorageBlockEntityProvider provider;
    public final int auraLossWhenIdle;
    private int ticksUntilIdle = ArcaneStaticConfig.TICKS_UNTIL_CONSIDERED_IDLE;
    public int ticksUntilNextAuraLoss = ArcaneStaticConfig.Rates.AURA_LOSS_TICKS;
    public boolean hasSignal = false;

    public AbstractAuraBlockEntity(int maxAura, int auraLossWhenIdle, boolean extractable, boolean receivable, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.auraLossWhenIdle = auraLossWhenIdle;
        provider = new AuraStorageBlockEntityProvider(this, maxAura, extractable, receivable);
    }

    @Override
    @NotNull
    public CompoundTag getUpdateTag() {
        CompoundTag tag = saveWithFullMetadata();
        tag.putBoolean(NBTHelpers.KEY_HAS_SIGNAL, hasSignal);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        CompoundTag tag = getUpdateTag();
        hasSignal = tag.getBoolean(NBTHelpers.KEY_HAS_SIGNAL);
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        LazyOptional<T> providerCapability = provider.getCapability(capability);

        if (providerCapability.isPresent())
            return providerCapability;

        return super.getCapability(capability, side);
    }

    @Override
    public void invalidateCaps() {
        provider.invalidate();
        super.invalidateCaps();
    }

    public Optional<IAuraStorage> getAuraStorage() {
        return getCapability(ArcaneCapabilities.AURA_STORAGE).resolve();
    }

    public <U> Optional<U> mapAuraStorage(Function<? super IAuraStorage, ? extends U> func) {
        return getAuraStorage().map(func);
    }

    public List<Component> getText(List<Component> text, boolean detailed) {
        IAuraStorage aura = getAuraStorage().orElseThrow();
        text.add(Component
                .translatable("messages.arcane.aura")
                .append(Integer.toString(aura.getAura()))
                .append("/")
                .append(Integer.toString(aura.getMaxAura()))
                .withStyle(ChatFormatting.LIGHT_PURPLE)
        );
        return text;
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T entity) {
        if (!level.isClientSide && entity instanceof AbstractAuraBlockEntity machine && --machine.ticksUntilIdle <= 0 && --machine.ticksUntilNextAuraLoss <= 0) {
            machine.mapAuraStorage(storage -> ((BlockEntityAuraStorage) storage).removeAuraNoIdleUpdate(machine.auraLossWhenIdle));
            machine.ticksUntilNextAuraLoss = ArcaneStaticConfig.Rates.AURA_LOSS_TICKS;
        }
    }

    public static void setNotIdle(BlockEntity entity) {
        if (entity instanceof AbstractAuraBlockEntity machine)
            machine.ticksUntilIdle = ArcaneStaticConfig.TICKS_UNTIL_CONSIDERED_IDLE;
    }
}
