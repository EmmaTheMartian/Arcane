package martian.arcane;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused")
public class ArcaneTags {
    public static final TagKey<Item> WANDS = TagKey.create(Registries.ITEM, ArcaneMod.id("wands"));
    public static final TagKey<Item> BASIC_WANDS = TagKey.create(Registries.ITEM, ArcaneMod.id("basic_wands"));
    public static final TagKey<Item> ADVANCED_WANDS = TagKey.create(Registries.ITEM, ArcaneMod.id("advanced_wands"));
    public static final TagKey<Item> MYSTICAL_WANDS = TagKey.create(Registries.ITEM, ArcaneMod.id("mystical_wands"));
    public static final TagKey<Item> CRUSHED_DUSTS = TagKey.create(Registries.ITEM, ArcaneMod.id("crushed_dusts"));
    public static final TagKey<Item> PURIFIED_DUSTS = TagKey.create(Registries.ITEM, ArcaneMod.id("purified_dusts"));

    public static final TagKey<Block> AURA_EXTRACTORS = TagKey.create(Registries.BLOCK, ArcaneMod.id("aura_extractors"));
    public static final TagKey<Block> AURA_INSERTERS = TagKey.create(Registries.BLOCK, ArcaneMod.id("aura_inserters"));
    public static final TagKey<Block> AURA_BASINS = TagKey.create(Registries.BLOCK, ArcaneMod.id("aura_basins"));
    public static final TagKey<Block> AURA_WRENCH_BREAKABLE = TagKey.create(Registries.BLOCK, ArcaneMod.id("aura_wrench_breakable"));
    public static final TagKey<Block> BLOCKS_AURA_FLOW = TagKey.create(Registries.BLOCK, ArcaneMod.id("blocks_aura_flow"));
}
