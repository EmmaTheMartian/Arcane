package martian.arcane.datagen.client;

import martian.arcane.ArcaneMod;
import martian.arcane.common.ArcaneContent;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import static martian.arcane.common.ArcaneContent.*;

public class ArcaneItemModelProvider extends ItemModelProvider {
    public ArcaneItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ArcaneMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Tools
        {
            String path = "tools/";
            item(ITEM_AURAGLASS_BOTTLE, path);
            item(ITEM_MEDIUM_AURAGLASS_BOTTLE, path);
            item(ITEM_LARGE_AURAGLASS_BOTTLE, path);
            item(ITEM_EXTREME_AURAGLASS_BOTTLE, path);
            item(ITEM_CREATIVE_AURAGLASS_BOTTLE.get(), "tools/extreme_auraglass_bottle");

            handheld(ITEM_CHAINWAND, "wands/");
            handheld(ITEM_AURAOMETER, path);
            handheld(ITEM_AURA_WRENCH, path);
            handheld(ITEM_AURA_CONFIGURATOR, path);
            handheld(ITEM_AURA_MULTITOOL, path);
            item(ITEM_SPELL_TABLET, path);
            item(ITEM_ARCANE_BLEACH, path);
            item(ITEM_SPELL_CHALK, path);
            item(ITEM_ENDERPACK, path);
            item(ITEM_AXOBOTTLE, path);
            item(ITEM_GUIDEBOOK, path);
            item(ITEM_GEM_SAW, path);
            item(ITEM_UPGRADE_KIT_COPPER, path);
            item(ITEM_UPGRADE_KIT_LARIMAR, path);
            item(ITEM_UPGRADE_KIT_AURACHALCUM, path);
        }

        // Resources
        {
            String path = "resources/gems/";
            item(ITEMS_LARIMAR.rough(), path);
            item(ITEMS_LARIMAR.smooth(), path);
            item(ITEMS_LARIMAR.sandyPolished(), path);
            item(ITEMS_LARIMAR.polished(), path);
            item(ITEMS_LARIMAR.exquisite(), path);
            item(ITEMS_FADED_LARIMAR.rough(), path);
            item(ITEMS_FADED_LARIMAR.smooth(), path);
            item(ITEMS_FADED_LARIMAR.sandyPolished(), path);
            item(ITEMS_FADED_LARIMAR.polished(), path);
            item(ITEMS_FADED_LARIMAR.exquisite(), path);
            item(ITEMS_IDOCRASE.rough(), path);
            item(ITEMS_IDOCRASE.smooth(), path);
            item(ITEMS_IDOCRASE.sandyPolished(), path);
            item(ITEMS_IDOCRASE.polished(), path);
            item(ITEMS_IDOCRASE.exquisite(), path);

            path = "resources/";

            item(ITEM_RAW_AURACHALCUM, path);
            item(ITEM_AURACHALCUM, path);
            item(ITEM_ELDRITCH_ALLOY, path);

            item(ITEM_COPPER_CORE, path);
            item(ITEM_LARIMAR_CORE, path);
            item(ITEM_AURACHALCUM_CORE, path);
            item(ITEM_ELDRITCH_CORE, path);
            item(ITEM_SPELL_CIRCLE_CORE, path);

            item(ITEM_AURAGLASS_SHARD, path);
            item(ITEM_AURAGLASS_DUST, path);
        }

        // Ore Processing
        {
            String path = "dusts/";
            item(ITEM_CRUSHED_RAW_COPPER, path);
            item(ITEM_CRUSHED_RAW_IRON, path);
            item(ITEM_CRUSHED_RAW_GOLD, path);
            item(ITEM_PURIFIED_RAW_COPPER, path);
            item(ITEM_PURIFIED_RAW_IRON, path);
            item(ITEM_PURIFIED_RAW_GOLD, path);
        }

        // Block items
        ArcaneContent.ITEMS.getEntries()
                .stream()
                .filter(item -> item.get() instanceof BlockItem && item != ITEM_SPELL_CHALK)
                .forEach(this::blockItem);
    }

    private void build(String name, String parent, ResourceLocation texture) {
        getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(parent))
                .texture("layer0", texture);
    }

    private void item(Item item, String path) {
        build(item.toString(), "item/generated", ArcaneMod.id(path).withPrefix("item/"));
    }

    private void item(DeferredItem<?> item, String prefix) {
        item(item.get(), item.getId().withPrefix(prefix).getPath());
    }

    private void handheld(Item item, String path) {
        build(item.toString(), "item/handheld", ArcaneMod.id(path).withPrefix("item/"));
    }

    private void handheld(DeferredItem<?> item, String prefix) {
        handheld(item.get(), item.getId().withPrefix(prefix).getPath());
    }

    private void blockItem(DeferredHolder<Item, ?> item) {
        String path = item.getId().getPath();
        withExistingParent("item/" + path, new ResourceLocation(item.getId().getNamespace(), "block/" + path));
    }
}
