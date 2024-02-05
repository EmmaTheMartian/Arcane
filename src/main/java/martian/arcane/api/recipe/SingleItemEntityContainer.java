package martian.arcane.api.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record SingleItemEntityContainer(ItemEntity entity) implements Container {
    public ItemStack getEntityItemStack() {
        return entity().getItem();
    }

    public boolean is(Item item) {
        return getEntityItemStack().is(item);
    }

    public void removeEntityFromWorld() {
        entity.remove(Entity.RemovalReason.DISCARDED);
    }

    // Everything here down is just the Container methods that need to be there to let this be a Container object
    @Override
    @Deprecated
    public int getContainerSize() {
        throw new RuntimeException("Cannot invoke EntityContainer#getContainerSize");
    }

    @Override
    @Deprecated
    public boolean isEmpty() {
        throw new RuntimeException("Cannot invoke EntityContainer#isEmpty");
    }

    @Override
    @Deprecated
    @NotNull
    public ItemStack getItem(int slot) {
        throw new RuntimeException("Cannot invoke EntityContainer#getItem");
    }

    @Override
    @Deprecated
    @NotNull
    public ItemStack removeItem(int slot, int amount) {
        throw new RuntimeException("Cannot invoke EntityContainer#removeItem");
    }

    @Override
    @Deprecated
    @NotNull
    public ItemStack removeItemNoUpdate(int slot) {
        throw new RuntimeException("Cannot invoke EntityContainer#removeItemNoUpdate");
    }

    @Override
    @Deprecated
    public void setItem(int slot, @NotNull ItemStack stack) {
        throw new RuntimeException("Cannot invoke EntityContainer#setItem");
    }

    @Override
    @Deprecated
    public void setChanged() {
        throw new RuntimeException("Cannot invoke EntityContainer#setChanged");
    }

    @Override
    @Deprecated
    public boolean stillValid(@NotNull Player player) {
        throw new RuntimeException("Cannot invoke EntityContainer#stillValid");
    }

    @Override
    @Deprecated
    public void clearContent() {
        throw new RuntimeException("Cannot invoke EntityContainer#clearContent");
    }
}
