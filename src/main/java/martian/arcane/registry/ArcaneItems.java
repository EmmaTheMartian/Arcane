package martian.arcane.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneStaticConfig;
import martian.arcane.item.ItemAuraWand;
import martian.arcane.item.ItemAuraglassBottle;
import martian.arcane.item.ItemAuraometer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ArcaneItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArcaneMod.MODID);

    public static final RegistryObject<Item> AURAGLASS_BOTTLE = registerItem("auraglass_bottle", ItemAuraglassBottle::new);

    private static final Supplier<Item> BASIC_WAND_SUPPLIER = () -> new ItemAuraWand(ArcaneStaticConfig.AuraMaximums.BASIC_WAND, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    private static final Supplier<Item> ADVANCED_WAND_SUPPLIER = () -> new ItemAuraWand(ArcaneStaticConfig.AuraMaximums.ADVANCED_WAND, new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
    private static final Supplier<Item> MYSTICAL_WAND_SUPPLIER = () -> new ItemAuraWand(ArcaneStaticConfig.AuraMaximums.MYSTIC_WAND, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

    public static final RegistryObject<Item>
            WAND_ACACIA_AURA = registerItem("wand_acacia", BASIC_WAND_SUPPLIER),
            WAND_BAMBOO_AURA = registerItem("wand_bamboo", BASIC_WAND_SUPPLIER),
            WAND_BIRCH = registerItem("wand_birch", BASIC_WAND_SUPPLIER),
            WAND_CHERRY = registerItem("wand_cherry", BASIC_WAND_SUPPLIER),
            WAND_DARK_OAK = registerItem("wand_dark_oak", BASIC_WAND_SUPPLIER),
            WAND_JUNGLE = registerItem("wand_jungle", BASIC_WAND_SUPPLIER),
            WAND_MANGROVE = registerItem("wand_mangrove", BASIC_WAND_SUPPLIER),
            WAND_OAK = registerItem("wand_oak", BASIC_WAND_SUPPLIER),
            WAND_SPRUCE = registerItem("wand_spruce", BASIC_WAND_SUPPLIER),
            WAND_WARPED = registerItem("wand_warped", ADVANCED_WAND_SUPPLIER),
            WAND_CRIMSON = registerItem("wand_crimson", ADVANCED_WAND_SUPPLIER);

    public static final RegistryObject<Item> AURAOMETER = registerItem("auraometer", ItemAuraometer::new);

    // Helpers
    private static RegistryObject<Item> registerItem(String id, Supplier<Item> sup) {
        return ITEMS.register(id, sup);
    }

    public static void registerBlockItem(String id, RegistryObject<Block> block, @Nullable Supplier<Item.Properties> itemProps) {
        Supplier<Item.Properties> sup = itemProps != null ? itemProps : Item.Properties::new;
        registerItem(id, () -> new BlockItem(block.get(), sup.get()));
    }
}
