package martian.arcane.api.block.entity;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ISingleItemContainer extends Container {
    ItemStack getItem();

    void setItem(ItemStack stack);

    default int getContainerSize() {
        return 1;
    }

    default int getMaxStackSize() {
        return 1;
    }

    boolean isEmpty();

    @NotNull
    ItemStack getItem(int slot);

    @NotNull
    ItemStack removeItem(int slot, int amount);

    @NotNull
    ItemStack removeItemNoUpdate(int slot);

    void setItem(int slot, @NotNull ItemStack stack);

    boolean stillValid(@NotNull Player player);

    void clearContent();
}
