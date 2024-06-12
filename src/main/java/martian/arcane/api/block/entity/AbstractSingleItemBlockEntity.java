package martian.arcane.api.block.entity;

import martian.arcane.api.block.BlockHelpers;
import martian.arcane.api.NBTHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

//public abstract class AbstractSingleItemBlockEntity extends BlockEntity implements ISingleItemContainer {
//    protected ItemStack stack = ItemStack.EMPTY;
//    public boolean hasSignal = false;
//
//    public AbstractSingleItemBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
//        super(type, pos, blockState);
//    }
//
//    public ItemStack getItem() {
//        return stack;
//    }
//
//    public void setItem(ItemStack stack) {
//        this.stack = stack;
//        BlockHelpers.sync(this);
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return stack.isEmpty();
//    }
//
//    @Override
//    @NotNull
//    @Deprecated
//    public ItemStack getItem(int slot) {
//        return stack;
//    }
//
//    @Override
//    @NotNull
//    @Deprecated
//    public ItemStack removeItem(int slot, int amount) {
//        ItemStack stack = getItem().copy().split(amount);
//        getItem().shrink(amount);
//        BlockHelpers.sync(this);
//        return stack;
//    }
//
//    @Override
//    @NotNull
//    public ItemStack removeItemNoUpdate(int slot) {
//        ItemStack old = getItem(slot);
//        this.stack = ItemStack.EMPTY;
//        return old;
//    }
//
//    @Override
//    @Deprecated
//    public void setItem(int slot, @NotNull ItemStack stack) {
//        this.stack = stack;
//        BlockHelpers.sync(this);
//    }
//
//    @Override
//    public boolean stillValid(@NotNull Player player) {
//        return false;
//    }
//
//    @Override
//    public void clearContent() {
//        setItem(ItemStack.EMPTY);
//    }
//
//    @Override
//    public void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
//        NBTHelpers.putItemStack(provider, nbt, NBTHelpers.KEY_STACK, stack);
//        nbt.putBoolean(NBTHelpers.KEY_HAS_SIGNAL, hasSignal);
//        super.saveAdditional(nbt, provider);
//    }
//
//    @Override
//    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
//        super.loadAdditional(nbt, provider);
//        stack = NBTHelpers.getItemStack(provider, nbt, NBTHelpers.KEY_STACK);
//        hasSignal = nbt.getBoolean(NBTHelpers.KEY_HAS_SIGNAL);
//    }
//
//    @Override
//    @NotNull
//    public CompoundTag getUpdateTag(HolderLookup.@NotNull Provider provider) {
//        CompoundTag nbt = super.getUpdateTag(provider);
//        NBTHelpers.putItemStack(provider, nbt, NBTHelpers.KEY_STACK, stack);
//        nbt.putBoolean(NBTHelpers.KEY_HAS_SIGNAL, hasSignal);
//        return nbt;
//    }
//
//    @Override
//    public Packet<ClientGamePacketListener> getUpdatePacket() {
//        assert level != null;
//        CompoundTag nbt = getUpdateTag(level.registryAccess());
//        stack = NBTHelpers.getItemStack(level.registryAccess(), nbt, NBTHelpers.KEY_STACK);
//        hasSignal = nbt.getBoolean(NBTHelpers.KEY_HAS_SIGNAL);
//        return ClientboundBlockEntityDataPacket.create(this);
//    }
//}
