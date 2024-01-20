package martian.arcane;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public class ArcaneTags {
    public static final TagKey<Item> WANDS = TagKey.create(Registries.ITEM, new ResourceLocation(ArcaneMod.MODID, "wands"));
    public static final TagKey<Item> BASIC_WANDS = TagKey.create(Registries.ITEM, new ResourceLocation(ArcaneMod.MODID, "basic_wands"));
    public static final TagKey<Item> ADVANCED_WANDS = TagKey.create(Registries.ITEM, new ResourceLocation(ArcaneMod.MODID, "advanced_wands"));
}
