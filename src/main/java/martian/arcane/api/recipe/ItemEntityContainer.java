package martian.arcane.api.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ItemEntityContainer(NonNullList<ItemEntity> entities) implements Container {
    public ItemEntity getEntity(int index) {
        return entities.get(index);
    }

    public ItemStack getEntityStack(int index) {
        return getEntity(index).getItem();
    }

    public boolean hasItem(Item item) {
        return entities.stream().anyMatch(itemEntity -> itemEntity.getItem().is(item));
    }

    public List<ItemEntity> entitiesOfType(Item item) {
        return entities.stream().filter(itemEntity -> itemEntity.getItem().is(item)).toList();
    }

    public ItemEntity getFirstOfItem(Item item) {
        return entitiesOfType(item).get(0);
    }

    public void removeEntityFromWorld(int index) {
        entities.get(index).remove(Entity.RemovalReason.DISCARDED);
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
