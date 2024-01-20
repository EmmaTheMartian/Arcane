package martian.arcane.block.entity;

import martian.arcane.api.NBTHelpers;
import martian.arcane.api.block.entity.IAuraometerOutput;
import martian.arcane.registry.ArcaneBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlockEntityItemPylon extends BlockEntity implements IAuraometerOutput {
    public ItemStackHandler inv;

    public BlockEntityItemPylon(BlockPos pos, BlockState state) {
        super(ArcaneBlockEntities.AURA_INFUSER_BE.get(), pos, state);
        inv = new ItemStackHandler(1);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        nbt.put(NBTHelpers.KEY_STACK, inv.serializeNBT());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        inv = new ItemStackHandler(1);
        inv.deserializeNBT(nbt.getCompound(NBTHelpers.KEY_STACK));
    }

    @Override
    public List<Component> getText(List<Component> text, boolean detailed) {
        if (!getItem().isEmpty())
            text.add(Component.literal("Holding: ").append(getItem().getDisplayName()));
        else
            text.add(Component.literal("No held item."));
        return text;
    }

    public ItemStack getItem() {
        return inv.getStackInSlot(0);
    }

    public void setItem(ItemStack stack) {
        inv.setStackInSlot(0, stack);
    }
}
