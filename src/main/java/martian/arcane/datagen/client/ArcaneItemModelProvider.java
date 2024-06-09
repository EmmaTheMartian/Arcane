package martian.arcane.datagen.client;

import martian.arcane.ArcaneMod;
import martian.arcane.common.registry.ArcaneItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

public class ArcaneItemModelProvider extends ItemModelProvider {
    public ArcaneItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ArcaneMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Tools
        {
            // Wands
            for (DeferredItem<?> wand : ArcaneItems.WANDS) {
                handheld(wand, "wands/");
            }

            String path = "tools/";
            item(ArcaneItems.AURAGLASS_BOTTLE, path);
            item(ArcaneItems.MEDIUM_AURAGLASS_BOTTLE, path);
            item(ArcaneItems.LARGE_AURAGLASS_BOTTLE, path);
            item(ArcaneItems.EXTREME_AURAGLASS_BOTTLE, path);
            item(ArcaneItems.CREATIVE_AURAGLASS_BOTTLE.get(), "tools/extreme_auraglass_bottle");

            item(ArcaneItems.AURAOMETER, path);
            item(ArcaneItems.AURA_WRENCH, path);
            item(ArcaneItems.AURA_CONFIGURATOR, path);
            item(ArcaneItems.SPELL_TABLET, path);
            item(ArcaneItems.ARCANE_BLEACH, path);
            item(ArcaneItems.SPELL_CHALK, path);
            item(ArcaneItems.ENDERPACK, path);
            item(ArcaneItems.AXOBOTTLE, path);
            item(ArcaneItems.GUIDEBOOK, path);
            item(ArcaneItems.GEM_SAW, path);
        }

        // Resources
        {
            String path = "resources/";
            item(ArcaneItems.RAW_LARIMAR, path);
            item(ArcaneItems.CUT_LARIMAR, path);
            item(ArcaneItems.POLISHED_LARIMAR, path);
            item(ArcaneItems.FADED_RAW_LARIMAR, path);
            item(ArcaneItems.FADED_CUT_LARIMAR, path);
            item(ArcaneItems.FADED_POLISHED_LARIMAR, path);
            item(ArcaneItems.RAW_IDOCRASE, path);
            item(ArcaneItems.CUT_IDOCRASE, path);
            item(ArcaneItems.POLISHED_IDOCRASE, path);
            item(ArcaneItems.RAW_AURACHALCUM, path);
            item(ArcaneItems.AURACHALCUM, path);
            item(ArcaneItems.ELDRITCH_ALLOY, path);

            item(ArcaneItems.COPPER_CORE, path);
            item(ArcaneItems.LARIMAR_CORE, path);
            item(ArcaneItems.AURACHALCUM_CORE, path);
            item(ArcaneItems.ELDRITCH_CORE, path);
            item(ArcaneItems.SPELL_CIRCLE_CORE, path);

            item(ArcaneItems.AURAGLASS_SHARD, path);
            item(ArcaneItems.AURAGLASS_DUST, path);
        }

        // Ore Processing
        {
            String path = "dusts/";
            item(ArcaneItems.CRUSHED_RAW_COPPER, path);
            item(ArcaneItems.CRUSHED_RAW_IRON, path);
            item(ArcaneItems.CRUSHED_RAW_GOLD, path);
            item(ArcaneItems.PURIFIED_RAW_COPPER, path);
            item(ArcaneItems.PURIFIED_RAW_IRON, path);
            item(ArcaneItems.PURIFIED_RAW_GOLD, path);
        }

        // Block items
        ArcaneItems.ITEMS.getEntries()
                .stream()
                .filter(item -> item.get() instanceof BlockItem && item != ArcaneItems.SPELL_CHALK)
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
