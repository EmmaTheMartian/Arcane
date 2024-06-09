package martian.arcane.api.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemHelpers {
    public static void addItemEntity(Level level, ItemStack stack, double x, double y, double z) {
        level.addFreshEntity(new ItemEntity(level, x, y, z, stack));
    }

    public static void addItemEntity(Level level, ItemStack stack, BlockPos pos) {
        addItemEntity(level, stack, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F);
    }
}
