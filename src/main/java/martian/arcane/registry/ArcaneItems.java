package martian.arcane.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.item.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ArcaneItems extends ArcaneRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArcaneMod.MODID);

    // Some shorthands
    private static final Supplier<Item> BASIC_WAND_SUPPLIER = () -> new ItemAuraWand(ArcaneStaticConfig.Maximums.BASIC_WAND, 1, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    private static final Supplier<Item> ADVANCED_WAND_SUPPLIER = () -> new ItemAuraWand(ArcaneStaticConfig.Maximums.ADVANCED_WAND, 2, new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
    private static final Supplier<Item> MYSTICAL_WAND_SUPPLIER = () -> new ItemAuraWand(ArcaneStaticConfig.Maximums.MYSTIC_WAND, 3, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

    // Actual Items
    public static final RegistryObject<Item>
            // Auraglass Bottles
            AURAGLASS_BOTTLE = register("auraglass_bottle", () -> new ItemAuraglassBottle(ArcaneStaticConfig.Maximums.SMALL_AURAGLASS_BOTTLE)),
            MEDIUM_AURAGLASS_BOTTLE = register("medium_auraglass_bottle", () -> new ItemAuraglassBottle(ArcaneStaticConfig.Maximums.MEDIUM_AURAGLASS_BOTTLE)),
            LARGE_AURAGLASS_BOTTLE = register("large_auraglass_bottle", () -> new ItemAuraglassBottle(ArcaneStaticConfig.Maximums.LARGE_AURAGLASS_BOTTLE)),
            EXTREME_AURAGLASS_BOTTLE = register("extreme_auraglass_bottle", () -> new ItemAuraglassBottle(ArcaneStaticConfig.Maximums.EXTREME_AURAGLASS_BOTTLE)),
            // Wands
            WAND_ACACIA_AURA = register("wand_acacia", BASIC_WAND_SUPPLIER),
            WAND_BAMBOO_AURA = register("wand_bamboo", BASIC_WAND_SUPPLIER),
            WAND_BIRCH = register("wand_birch", BASIC_WAND_SUPPLIER),
            WAND_CHERRY = register("wand_cherry", BASIC_WAND_SUPPLIER),
            WAND_DARK_OAK = register("wand_dark_oak", BASIC_WAND_SUPPLIER),
            WAND_JUNGLE = register("wand_jungle", BASIC_WAND_SUPPLIER),
            WAND_MANGROVE = register("wand_mangrove", BASIC_WAND_SUPPLIER),
            WAND_OAK = register("wand_oak", BASIC_WAND_SUPPLIER),
            WAND_SPRUCE = register("wand_spruce", BASIC_WAND_SUPPLIER),
            WAND_WARPED = register("wand_warped", BASIC_WAND_SUPPLIER),
            WAND_CRIMSON = register("wand_crimson", BASIC_WAND_SUPPLIER),
            WAND_BLUE_GOLD = register("wand_blue_gold", ADVANCED_WAND_SUPPLIER),
            WAND_AURACHALCUM = register("wand_aurachalcum", MYSTICAL_WAND_SUPPLIER),
            WAND_ELDRITCH = register("wand_eldritch", MYSTICAL_WAND_SUPPLIER),
            // Tools
            AURAOMETER = register("auraometer", ItemAuraometer::new),
            AURA_WRENCH = register("aura_wrench", ItemAuraWrench::new),
            AURA_CONFIGURATOR = register("aura_configurator", ItemAuraConfigurator::new),
            SPELL_TABLET = register("spell_tablet", ItemSpellTablet::new),
            // Materials
            BLUE_GOLD = basicItem("blue_gold_ingot"),
            RAW_AURACHALCUM = basicItem("raw_aurachalcum"),
            AURACHALCUM = basicItem("aurachalcum_ingot"),
            BLUE_GOLD_CORE = basicItem("blue_gold_core"),
            AURACHALCUM_CORE = basicItem("aurachalcum_core"),
            ELDRITCH_ALLOY = basicItem("eldritch_alloy"),
            ELDRITCH_CORE = basicItem("eldritch_core")
    ;

    // Helpers
    private static RegistryObject<Item> register(String id, Supplier<Item> sup) {
        return ITEMS.register(id, sup);
    }

    private static RegistryObject<Item> register(String id, Item.Properties properties) {
        return ITEMS.register(id, () -> new Item(properties));
    }

    private static RegistryObject<Item> basicItem(String id) {
        return register(id, new Item.Properties());
    }

    public static void blockItem(String id, RegistryObject<Block> block) {
        register(id, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
