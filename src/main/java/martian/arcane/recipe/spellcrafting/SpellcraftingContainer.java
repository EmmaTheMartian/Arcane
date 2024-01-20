package martian.arcane.recipe.spellcrafting;

import martian.arcane.block.entity.BlockEntitySpellcraftingCore;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class SpellcraftingContainer extends RecipeWrapper {
    public SpellcraftingContainer(IItemHandlerModifiable items) {
        super(items);
    }

    public SpellcraftingContainer(BlockEntitySpellcraftingCore core) {
        super(core.getInventory());
    }

    public ItemStack getCenterItem() {
        return this.getItem(0);
    }

    public NonNullList<ItemStack> getPylonItems() {
        NonNullList<ItemStack> stacks = NonNullList.create();
        for (int i = 1; i < this.getContainerSize(); i++) {
            stacks.add(this.getItem(i));
        }
        return stacks;
    }
}
