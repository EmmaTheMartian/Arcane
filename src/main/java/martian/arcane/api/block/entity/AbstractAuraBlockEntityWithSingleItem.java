package martian.arcane.api.block.entity;

import martian.arcane.api.block.BlockHelpers;
import martian.arcane.api.NBTHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public class AbstractAuraBlockEntityWithSingleItem extends AbstractAuraBlockEntity implements Container, IItemHandlerModifiable {
    protected ItemStack stack = ItemStack.EMPTY;
    public boolean hasSignal = false;

    public AbstractAuraBlockEntityWithSingleItem(int maxAura, boolean extractable, boolean receivable, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(maxAura, extractable, receivable, type, pos, state);
    }

    public ItemStack getItem() {
        return stack;
    }

    public void setItem(ItemStack stack) {
        this.stack = stack;
        setChanged();
        BlockHelpers.sync(this);
    }

    // Container implementation
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
    public ItemStack getItem(int ignoredSlot) {
        return stack;
    }

    @Override
    @NotNull
    @Deprecated
    public ItemStack removeItem(int ignoredSlot, int amount) {
        ItemStack s = stack.copy().split(amount);
        stack.shrink(amount);
        setChanged();
        BlockHelpers.sync(this);
        return s;
    }

    @Override
    @NotNull
    public ItemStack removeItemNoUpdate(int ignoredSlot) {
        ItemStack old = stack.copy();
        setItem(ItemStack.EMPTY);
        return old;
    }

    @Override
    @Deprecated
    public void setItem(int ignoredSlot, @NotNull ItemStack stack) {
        this.stack = stack;
        setChanged();
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
    public boolean canPlaceItem(int ignoredSlot, @NotNull ItemStack stack) {
        return getItem().getCount() == 0 && stack.getCount() <= 1;
    }

    // Serialization
    @Override
    public void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        NBTHelpers.putItemStack(provider, nbt, NBTHelpers.KEY_STACK, stack);
        nbt.putBoolean(NBTHelpers.KEY_HAS_SIGNAL, hasSignal);
        super.saveAdditional(nbt, provider);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);
        stack = NBTHelpers.getItemStack(provider, nbt, NBTHelpers.KEY_STACK);
        hasSignal = nbt.getBoolean(NBTHelpers.KEY_HAS_SIGNAL);
    }

    @Override
    @NotNull
    public CompoundTag getUpdateTag(HolderLookup.@NotNull Provider provider) {
        CompoundTag nbt = super.getUpdateTag(provider);
        NBTHelpers.putItemStack(provider, nbt, NBTHelpers.KEY_STACK, stack);
        nbt.putBoolean(NBTHelpers.KEY_HAS_SIGNAL, hasSignal);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        assert level != null;
        CompoundTag nbt = getUpdateTag(level.registryAccess());
        stack = NBTHelpers.getItemStack(level.registryAccess(), nbt, NBTHelpers.KEY_STACK);
        hasSignal = nbt.getBoolean(NBTHelpers.KEY_HAS_SIGNAL);
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // IItemHandlerModifiable
    @Override
    public void setStackInSlot(int i, @NotNull ItemStack arg) {
        setItem(arg);
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    @NotNull
    public ItemStack getStackInSlot(int i) {
        return getItem();
    }

    @Override
    @NotNull
    public ItemStack insertItem(int i, @NotNull ItemStack arg, boolean simulate) {
        if (!getItem().isEmpty())
            return stack;

        if (stack.getCount() > 1) {
            if (!simulate)
                setItem(stack.copyWithCount(1));
            return stack.copyWithCount(stack.getCount() - 1);
        }

        return ItemStack.EMPTY;
    }

    @Override
    @NotNull
    public ItemStack extractItem(int i, int amount, boolean simulate) {
        if (simulate) {
            return stack.copy().split(amount);
        } else {
            return removeItem(i, amount);
        }
    }

    @Override
    public int getSlotLimit(int i) {
        return 1;
    }

    @Override
    public boolean isItemValid(int i, @NotNull ItemStack arg) {
        return true;
    }
}
