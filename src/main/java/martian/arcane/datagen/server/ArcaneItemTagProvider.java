package martian.arcane.datagen.server;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneTags;
import martian.arcane.common.registry.ArcaneItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ArcaneItemTagProvider extends ItemTagsProvider {
    public ArcaneItemTagProvider(PackOutput arg, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, lookupProvider, blockLookupProvider, ArcaneMod.MODID, existingFileHelper);
    }

    @Override
    public void addTags(@NotNull HolderLookup.Provider provider) {
        tag(ArcaneTags.BASIC_WANDS)
                .add(ArcaneItems.WAND_ACACIA.get())
                .add(ArcaneItems.WAND_BAMBOO.get())
                .add(ArcaneItems.WAND_BIRCH.get())
                .add(ArcaneItems.WAND_CHERRY.get())
                .add(ArcaneItems.WAND_DARK_OAK.get())
                .add(ArcaneItems.WAND_JUNGLE.get())
                .add(ArcaneItems.WAND_MANGROVE.get())
                .add(ArcaneItems.WAND_OAK.get())
                .add(ArcaneItems.WAND_SPRUCE.get())
                .add(ArcaneItems.WAND_WARPED.get())
                .add(ArcaneItems.WAND_CRIMSON.get());

        tag(ArcaneTags.ADVANCED_WANDS)
                .add(ArcaneItems.WAND_BLUE_GOLD.get());

        tag(ArcaneTags.MYSTICAL_WANDS)
                .add(ArcaneItems.WAND_AURACHALCUM.get())
                .add(ArcaneItems.WAND_ELDRITCH.get());

        tag(ArcaneTags.WANDS)
                .addTag(ArcaneTags.BASIC_WANDS)
                .addTag(ArcaneTags.ADVANCED_WANDS)
                .addTag(ArcaneTags.MYSTICAL_WANDS);

        tag(ArcaneTags.CRUSHED_DUSTS)
                .add(ArcaneItems.CRUSHED_RAW_COPPER.get())
                .add(ArcaneItems.CRUSHED_RAW_IRON.get())
                .add(ArcaneItems.CRUSHED_RAW_GOLD.get());

        tag(ArcaneTags.PURIFIED_DUSTS)
                .add(ArcaneItems.PURIFIED_RAW_COPPER.get())
                .add(ArcaneItems.PURIFIED_RAW_IRON.get())
                .add(ArcaneItems.PURIFIED_RAW_GOLD.get());

        tag(forgeTag("ingots"))
                .add(ArcaneItems.BLUE_GOLD.get())
                .add(ArcaneItems.AURACHALCUM.get())
                .add(ArcaneItems.AURACHALCUM.get())
                .add(ArcaneItems.ELDRITCH_ALLOY.get());

        tag(forgeTag("dusts"))
                .addTag(ArcaneTags.CRUSHED_DUSTS)
                .addTag(ArcaneTags.PURIFIED_DUSTS);

        tag(forgeTag("dusts/copper"))
                .add(ArcaneItems.CRUSHED_RAW_COPPER.get())
                .add(ArcaneItems.PURIFIED_RAW_COPPER.get());

        tag(forgeTag("dusts/iron"))
                .add(ArcaneItems.CRUSHED_RAW_IRON.get())
                .add(ArcaneItems.PURIFIED_RAW_IRON.get());

        tag(forgeTag("dusts/gold"))
                .add(ArcaneItems.CRUSHED_RAW_GOLD.get())
                .add(ArcaneItems.PURIFIED_RAW_GOLD.get());
    }

    private static TagKey<Item> forgeTag(String location) {
        return TagKey.create(Registries.ITEM, new ResourceLocation("forge", location));
    }
}
