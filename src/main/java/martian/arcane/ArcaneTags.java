package martian.arcane;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static martian.arcane.ArcaneMod.id;

public class ArcaneTags {
    public static final TagKey<Item> WANDS = TagKey.create(Registries.ITEM, id("wands"));
    public static final TagKey<Item> WAND_CAPS = TagKey.create(Registries.ITEM, id("wand_caps"));
    public static final TagKey<Item> CRUSHED_DUSTS = TagKey.create(Registries.ITEM, id("crushed_dusts"));
    public static final TagKey<Item> PURIFIED_DUSTS = TagKey.create(Registries.ITEM, id("purified_dusts"));

    public static final TagKey<Block> AURA_WRENCH_BREAKABLE = TagKey.create(Registries.BLOCK, id("aura_wrench_breakable"));
    public static final TagKey<Block> BLOCKS_AURA_FLOW = TagKey.create(Registries.BLOCK, id("blocks_aura_flow"));
    public static final TagKey<Block> MIXING_CAULDRONS = TagKey.create(Registries.BLOCK, id("mixing_cauldrons"));
}
