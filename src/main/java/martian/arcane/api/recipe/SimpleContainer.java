package martian.arcane.api.recipe;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

public class SimpleContainer extends RecipeWrapper {
    public final int editSlot = 0;

    public SimpleContainer(IItemHandlerModifiable items) {
        super(items);
    }

    public SimpleContainer() {
        super(new ItemStackHandler());
        setItem(ItemStack.EMPTY);
    }

    public SimpleContainer(ItemStack stack) {
        super(new ItemStackHandler());
        setItem(stack);
    }

    public ItemStack getItem() {
        return getItem(editSlot);
    }

    public void setItem(ItemStack stack) {
        setItem(editSlot, stack);
    }

    public ItemStack removeItem(int count) {
        ItemStack stack = this.getItem();
        return stack.isEmpty() ? ItemStack.EMPTY : stack.split(count);
    }

    public ItemStack removeItemNoUpdate() {
        ItemStack stack = this.getItem();
        if (stack.isEmpty())
            return ItemStack.EMPTY;
        else {
            this.setItem(ItemStack.EMPTY);
            return stack;
        }
    }

    public boolean isEmpty() {
        return getItem().isEmpty();
    }

    public void clearItem() {
        this.setItem(ItemStack.EMPTY);
    }
}
