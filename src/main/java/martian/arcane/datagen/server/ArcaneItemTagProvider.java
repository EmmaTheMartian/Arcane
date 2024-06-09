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
import net.neoforged.neoforge.common.data.ExistingFileHelper;
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
                .add(ArcaneItems.WAND_CRIMSON.get())
                .add(ArcaneItems.WAND_COPPER.get());

        tag(ArcaneTags.ADVANCED_WANDS)
                .add(ArcaneItems.WAND_LARIMAR.get());

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

        tag(commonTag("ingots"))
                .add(ArcaneItems.ELDRITCH_ALLOY.get());

        tag(commonTag("gems"))
                .add(ArcaneItems.RAW_LARIMAR.get())
                .add(ArcaneItems.CUT_LARIMAR.get())
                .add(ArcaneItems.POLISHED_LARIMAR.get())
                .add(ArcaneItems.FADED_RAW_LARIMAR.get())
                .add(ArcaneItems.FADED_CUT_LARIMAR.get())
                .add(ArcaneItems.FADED_POLISHED_LARIMAR.get())
                .add(ArcaneItems.RAW_IDOCRASE.get())
                .add(ArcaneItems.CUT_IDOCRASE.get())
                .add(ArcaneItems.POLISHED_IDOCRASE.get())
                .add(ArcaneItems.RAW_AURACHALCUM.get())
                .add(ArcaneItems.AURACHALCUM.get());

        tag(commonTag("dusts"))
                .addTag(ArcaneTags.CRUSHED_DUSTS)
                .addTag(ArcaneTags.PURIFIED_DUSTS);

        tag(commonTag("dusts/copper"))
                .add(ArcaneItems.CRUSHED_RAW_COPPER.get())
                .add(ArcaneItems.PURIFIED_RAW_COPPER.get());

        tag(commonTag("dusts/iron"))
                .add(ArcaneItems.CRUSHED_RAW_IRON.get())
                .add(ArcaneItems.PURIFIED_RAW_IRON.get());

        tag(commonTag("dusts/gold"))
                .add(ArcaneItems.CRUSHED_RAW_GOLD.get())
                .add(ArcaneItems.PURIFIED_RAW_GOLD.get());
    }

    private static TagKey<Item> commonTag(String location) {
        return TagKey.create(Registries.ITEM, new ResourceLocation("c", location));
    }
}
