package martian.arcane.common.item;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemGemSaw extends Item {
    private static final RandomSource random = RandomSource.create();

    public ItemGemSaw() {
        super(new Item.Properties().stacksTo(1).durability(128));
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack s = stack.copy();
        if (s.hurt(1, random, null))
            return ItemStack.EMPTY;
        return s;
    }
}
