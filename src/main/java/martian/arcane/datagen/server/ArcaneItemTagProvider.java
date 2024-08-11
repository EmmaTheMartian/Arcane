package martian.arcane.datagen.server;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static martian.arcane.common.ArcaneContent.*;

public class ArcaneItemTagProvider extends ItemTagsProvider {
    public ArcaneItemTagProvider(PackOutput arg, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, lookupProvider, blockLookupProvider, ArcaneMod.MODID, existingFileHelper);
    }

    @Override
    public void addTags(@NotNull HolderLookup.Provider provider) {
        tag(ArcaneTags.WANDS)
                .add(ITEM_WAND.get());

        tag(ArcaneTags.CRUSHED_DUSTS)
                .add(ITEM_CRUSHED_RAW_COPPER.get())
                .add(ITEM_CRUSHED_RAW_IRON.get())
                .add(ITEM_CRUSHED_RAW_GOLD.get());

        tag(ArcaneTags.PURIFIED_DUSTS)
                .add(ITEM_PURIFIED_RAW_COPPER.get())
                .add(ITEM_PURIFIED_RAW_IRON.get())
                .add(ITEM_PURIFIED_RAW_GOLD.get());

        tag(commonTag("ingots"))
                .add(ITEM_ELDRITCH_ALLOY.get());

        tag(commonTag("gems"))
                .add(ITEMS_LARIMAR.rough().get())
                .add(ITEMS_LARIMAR.smooth().get())
                .add(ITEMS_LARIMAR.sandyPolished().get())
                .add(ITEMS_LARIMAR.polished().get())
                .add(ITEMS_LARIMAR.exquisite().get())
                .add(ITEMS_FADED_LARIMAR.rough().get())
                .add(ITEMS_FADED_LARIMAR.smooth().get())
                .add(ITEMS_FADED_LARIMAR.sandyPolished().get())
                .add(ITEMS_FADED_LARIMAR.polished().get())
                .add(ITEMS_FADED_LARIMAR.exquisite().get())
                .add(ITEMS_IDOCRASE.rough().get())
                .add(ITEMS_IDOCRASE.smooth().get())
                .add(ITEMS_IDOCRASE.sandyPolished().get())
                .add(ITEMS_IDOCRASE.polished().get())
                .add(ITEMS_IDOCRASE.exquisite().get())
                .add(ITEM_RAW_AURACHALCUM.get())
                .add(ITEM_AURACHALCUM.get());

        tag(commonTag("dusts"))
                .addTag(ArcaneTags.CRUSHED_DUSTS)
                .addTag(ArcaneTags.PURIFIED_DUSTS);

        tag(commonTag("dusts/copper"))
                .add(ITEM_CRUSHED_RAW_COPPER.get())
                .add(ITEM_PURIFIED_RAW_COPPER.get());

        tag(commonTag("dusts/iron"))
                .add(ITEM_CRUSHED_RAW_IRON.get())
                .add(ITEM_PURIFIED_RAW_IRON.get());

        tag(commonTag("dusts/gold"))
                .add(ITEM_CRUSHED_RAW_GOLD.get())
                .add(ITEM_PURIFIED_RAW_GOLD.get());
    }

    private static TagKey<Item> commonTag(String location) {
        return TagKey.create(Registries.ITEM, new ResourceLocation("c", location));
    }
}
