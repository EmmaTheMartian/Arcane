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

    public static final TagKey<Block> AURA_EXTRACTORS = TagKey.create(Registries.BLOCK, ArcaneMod.id("aura_extractors"));
    public static final TagKey<Block> AURA_INSERTERS = TagKey.create(Registries.BLOCK, ArcaneMod.id("aura_inserters"));
}