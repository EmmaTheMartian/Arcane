package martian.arcane.datagen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class DataGenUtils {
    public static ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    public static ResourceLocation key(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }

    public static String name(Block block) {
        return key(block).getPath();
    }

    public static String name(Item item) {
        return key(item).getPath();
    }

    public static String namespace(Block block) {
        return key(block).getNamespace();
    }

    public static String namespace(Item item) {
        return key(item).getNamespace();
    }
}
