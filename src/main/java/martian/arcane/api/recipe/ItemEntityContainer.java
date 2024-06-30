package martian.arcane.api.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ItemEntityContainer(NonNullList<ItemEntity> entities) implements Container {
    public void removeEntityFromWorld(int index) {
        entities.get(index).remove(Entity.RemovalReason.DISCARDED);
    }

    // Everything here down is just the Container methods that need to be there to let this be a Container object
    @Override
    public int getContainerSize() {
        return entities.size();
    }

    @Override
    public boolean isEmpty() {
        return entities.isEmpty();
    }

    @Override
    @NotNull
    public ItemStack getItem(int slot) {
        return entities.get(slot).getItem();
    }

    @Override
    @NotNull
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = getItem(slot);
        return stack.isEmpty() ? ItemStack.EMPTY : stack.split(amount);
    }

    @Override
    @NotNull
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = getItem(slot).copy();
        if (stack.isEmpty())
            return ItemStack.EMPTY;
        else {
            setItem(slot, ItemStack.EMPTY);
            return stack;
        }
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        this.entities.get(slot).setItem(stack);
    }

    @Override
    @Deprecated
    public void setChanged() {
        throw new RuntimeException("Cannot invoke EntityContainer#setChanged");
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return false;
    }

    @Override
    public void clearContent() {
        for (ItemEntity entity : entities)
            entity.remove(Entity.RemovalReason.DISCARDED);
    }
}
