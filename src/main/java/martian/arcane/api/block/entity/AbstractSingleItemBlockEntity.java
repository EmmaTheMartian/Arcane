package martian.arcane.api.block.entity;

import martian.arcane.api.block.BlockHelpers;
import martian.arcane.api.NBTHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractSingleItemBlockEntity extends BlockEntity implements Container {
    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new InvWrapper(this));
    protected ItemStack stack = ItemStack.EMPTY;
    public boolean hasSignal = false;

    public AbstractSingleItemBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public ItemStack getItem() {
        return stack;
    }

    public void setItem(ItemStack stack) {
        this.stack = stack;
        BlockHelpers.sync(this);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    @NotNull
    @Deprecated
    public ItemStack getItem(int slot) {
        return stack;
    }

    @Override
    @NotNull
    @Deprecated
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = getItem().copy().split(amount);
        getItem().shrink(amount);
        BlockHelpers.sync(this);
        return stack;
    }

    @Override
    @NotNull
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack old = getItem(slot);
        this.stack = ItemStack.EMPTY;
        return old;
    }

    @Override
    @Deprecated
    public void setItem(int slot, @NotNull ItemStack stack) {
        this.stack = stack;
        BlockHelpers.sync(this);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return false;
    }

    @Override
    public void clearContent() {
        setItem(ItemStack.EMPTY);
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        itemHandler.invalidate();
        super.invalidateCaps();
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        NBTHelpers.putItemStack(nbt, NBTHelpers.KEY_STACK, stack);
        nbt.putBoolean(NBTHelpers.KEY_HAS_SIGNAL, hasSignal);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        stack = NBTHelpers.getItemStack(nbt, NBTHelpers.KEY_STACK);
        hasSignal = nbt.getBoolean(NBTHelpers.KEY_HAS_SIGNAL);
    }

    @Override
    @NotNull
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        NBTHelpers.putItemStack(nbt, NBTHelpers.KEY_STACK, stack);
        nbt.putBoolean(NBTHelpers.KEY_HAS_SIGNAL, hasSignal);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        CompoundTag nbt = getUpdateTag();
        stack = NBTHelpers.getItemStack(nbt, NBTHelpers.KEY_STACK);
        hasSignal = nbt.getBoolean(NBTHelpers.KEY_HAS_SIGNAL);
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
