package martian.arcane.common.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.common.item.*;
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
            AURAGLASS_BOTTLE = register("auraglass_bottle", () -> new ItemAuraglassBottle(ArcaneStaticConfig.Maximums.SMALL_AURAGLASS_BOTTLE, ArcaneStaticConfig.Rates.SMALL_AURAGLASS_BOTTLE)),
            MEDIUM_AURAGLASS_BOTTLE = register("medium_auraglass_bottle", () -> new ItemAuraglassBottle(ArcaneStaticConfig.Maximums.MEDIUM_AURAGLASS_BOTTLE, ArcaneStaticConfig.Rates.MEDIUM_AURAGLASS_BOTTLE)),
            LARGE_AURAGLASS_BOTTLE = register("large_auraglass_bottle", () -> new ItemAuraglassBottle(ArcaneStaticConfig.Maximums.LARGE_AURAGLASS_BOTTLE, ArcaneStaticConfig.Rates.LARGE_AURAGLASS_BOTTLE)),
            EXTREME_AURAGLASS_BOTTLE = register("extreme_auraglass_bottle", () -> new ItemAuraglassBottle(ArcaneStaticConfig.Maximums.EXTREME_AURAGLASS_BOTTLE, ArcaneStaticConfig.Rates.EXTREME_AURAGLASS_BOTTLE)),
            CREATIVE_AURAGLASS_BOTTLE = register("creative_auraglass_bottle", () -> new ItemAuraglassBottle(Integer.MAX_VALUE, Integer.MAX_VALUE)),
            // Wands
            WAND_ACACIA = register("wand_acacia", BASIC_WAND_SUPPLIER),
            WAND_BAMBOO = register("wand_bamboo", BASIC_WAND_SUPPLIER),
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
            ARCANE_BLEACH = basicItem("arcane_bleach"),
            SPELL_CHALK = register("spell_chalk", ItemSpellChalk::new),
            ENDERPACK = register("enderpack", ItemEnderpack::new),
            // Materials
            BLUE_GOLD = basicItem("blue_gold_ingot"),
            RAW_AURACHALCUM = basicItem("raw_aurachalcum"),
            AURACHALCUM = basicItem("aurachalcum_ingot"),
            ELDRITCH_ALLOY = basicItem("eldritch_alloy"),
            BLUE_GOLD_CORE = basicItem("blue_gold_core"),
            AURACHALCUM_CORE = basicItem("aurachalcum_core"),
            ELDRITCH_CORE = basicItem("eldritch_core"),
            SPELL_CIRCLE_CORE = basicItem("spell_circle_core"),
            AURAGLASS_SHARD = basicItem("auraglass_shard"),
            AURAGLASS_DUST = basicItem("auraglass_dust"),
            // Ore Processing
            CRUSHED_RAW_COPPER = basicItem("crushed_raw_copper"),
            CRUSHED_RAW_IRON = basicItem("crushed_raw_iron"),
            CRUSHED_RAW_GOLD = basicItem("crushed_raw_gold"),
            PURIFIED_RAW_COPPER = basicItem("purified_raw_copper"),
            PURIFIED_RAW_IRON = basicItem("purified_raw_iron"),
            PURIFIED_RAW_GOLD = basicItem("purified_raw_gold")
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

    public static RegistryObject<Item> blockItem(String id, RegistryObject<Block> block) {
        return register(id, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
