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
        tag(ArcaneTags.BASIC_WANDS)
                .add(WAND_ACACIA.get())
                .add(WAND_BAMBOO.get())
                .add(WAND_BIRCH.get())
                .add(WAND_CHERRY.get())
                .add(WAND_DARK_OAK.get())
                .add(WAND_JUNGLE.get())
                .add(WAND_MANGROVE.get())
                .add(WAND_OAK.get())
                .add(WAND_SPRUCE.get())
                .add(WAND_WARPED.get())
                .add(WAND_CRIMSON.get())
                .add(WAND_COPPER.get());

        tag(ArcaneTags.ADVANCED_WANDS)
                .add(WAND_LARIMAR.get());

        tag(ArcaneTags.MYSTICAL_WANDS)
                .add(WAND_AURACHALCUM.get())
                .add(WAND_ELDRITCH.get());

        tag(ArcaneTags.WANDS)
                .addTag(ArcaneTags.BASIC_WANDS)
                .addTag(ArcaneTags.ADVANCED_WANDS)
                .addTag(ArcaneTags.MYSTICAL_WANDS);

        tag(ArcaneTags.CRUSHED_DUSTS)
                .add(CRUSHED_RAW_COPPER.get())
                .add(CRUSHED_RAW_IRON.get())
                .add(CRUSHED_RAW_GOLD.get());

        tag(ArcaneTags.PURIFIED_DUSTS)
                .add(PURIFIED_RAW_COPPER.get())
                .add(PURIFIED_RAW_IRON.get())
                .add(PURIFIED_RAW_GOLD.get());

        tag(commonTag("ingots"))
                .add(ELDRITCH_ALLOY.get());

        tag(commonTag("gems"))
                .add(LARIMAR.rough().get())
                .add(LARIMAR.smooth().get())
                .add(LARIMAR.sandyPolished().get())
                .add(LARIMAR.polished().get())
                .add(LARIMAR.exquisite().get())
                .add(FADED_LARIMAR.rough().get())
                .add(FADED_LARIMAR.smooth().get())
                .add(FADED_LARIMAR.sandyPolished().get())
                .add(FADED_LARIMAR.polished().get())
                .add(FADED_LARIMAR.exquisite().get())
                .add(IDOCRASE.rough().get())
                .add(IDOCRASE.smooth().get())
                .add(IDOCRASE.sandyPolished().get())
                .add(IDOCRASE.polished().get())
                .add(IDOCRASE.exquisite().get())
                .add(RAW_AURACHALCUM.get())
                .add(AURACHALCUM.get());

        tag(commonTag("dusts"))
                .addTag(ArcaneTags.CRUSHED_DUSTS)
                .addTag(ArcaneTags.PURIFIED_DUSTS);

        tag(commonTag("dusts/copper"))
                .add(CRUSHED_RAW_COPPER.get())
                .add(PURIFIED_RAW_COPPER.get());

        tag(commonTag("dusts/iron"))
                .add(CRUSHED_RAW_IRON.get())
                .add(PURIFIED_RAW_IRON.get());

        tag(commonTag("dusts/gold"))
                .add(CRUSHED_RAW_GOLD.get())
                .add(PURIFIED_RAW_GOLD.get());
    }

    private static TagKey<Item> commonTag(String location) {
        return TagKey.create(Registries.ITEM, new ResourceLocation("c", location));
    }
}
