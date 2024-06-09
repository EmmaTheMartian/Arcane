package martian.arcane.common.item;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class ItemGemSaw extends Item {
    private static final RandomSource random = RandomSource.create();

    public ItemGemSaw() {
        super(new Item.Properties().stacksTo(1).durability(128));
    }

    @Override
    public boolean hasCraftingRemainingItem(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    @NotNull
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack s = stack.copy();
        s.hurtAndBreak(1, random, null, () -> {});
        return s;
    }
}
