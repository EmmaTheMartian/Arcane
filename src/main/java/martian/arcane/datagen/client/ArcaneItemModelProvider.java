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
            // Wands
            basicWand(WAND_OAK, "minecraft:block/oak_log");
            basicWand(WAND_ACACIA, "minecraft:block/acacia_log");
            basicWand(WAND_BAMBOO, "minecraft:block/bamboo_block");
            basicWand(WAND_BIRCH, "minecraft:block/birch_log");
            basicWand(WAND_CHERRY, "minecraft:block/cherry_log");
            basicWand(WAND_DARK_OAK, "minecraft:block/dark_oak_log");
            basicWand(WAND_JUNGLE, "minecraft:block/jungle_log");
            basicWand(WAND_MANGROVE, "minecraft:block/mangrove_log");
            basicWand(WAND_OAK, "minecraft:block/oak_log");
            basicWand(WAND_SPRUCE, "minecraft:block/spruce_log");
            basicWand(WAND_WARPED, "minecraft:block/warped_stem");
            basicWand(WAND_CRIMSON, "minecraft:block/crimson_stem");
            advancedWand(WAND_COPPER, "minecraft:block/copper_block");
            advancedWand(WAND_LARIMAR, "arcane:block/larimar_block");
            mysticalWand(WAND_AURACHALCUM, "arcane:block/aurachalcum_block");
            mysticalWand(WAND_ELDRITCH, "arcane:block/blackstone_idocrase_ore"); //todo

            String path = "tools/";
            item(AURAGLASS_BOTTLE, path);
            item(MEDIUM_AURAGLASS_BOTTLE, path);
            item(LARGE_AURAGLASS_BOTTLE, path);
            item(EXTREME_AURAGLASS_BOTTLE, path);
            item(CREATIVE_AURAGLASS_BOTTLE.get(), "tools/extreme_auraglass_bottle");

            item(AURAOMETER, path);
            item(AURA_WRENCH, path);
            item(AURA_CONFIGURATOR, path);
            item(AURA_MULTITOOL, path);
            item(SPELL_TABLET, path);
            item(ARCANE_BLEACH, path);
            item(SPELL_CHALK, path);
            item(ENDERPACK, path);
            item(AXOBOTTLE, path);
            item(GUIDEBOOK, path);
            item(GEM_SAW, path);
        }

        // Resources
        {
            String path = "resources/gems/";
            item(LARIMAR.rough(), path);
            item(LARIMAR.smooth(), path);
            item(LARIMAR.sandyPolished(), path);
            item(LARIMAR.polished(), path);
            item(LARIMAR.exquisite(), path);
//            item(FADED_LARIMAR.rough(), path);
//            item(FADED_LARIMAR.smooth(), path);
//            item(FADED_LARIMAR.sandyPolished(), path);
//            item(FADED_LARIMAR.polished(), path);
//            item(FADED_LARIMAR.exquisite(), path);
            item(IDOCRASE.rough(), path);
            item(IDOCRASE.smooth(), path);
            item(IDOCRASE.sandyPolished(), path);
            item(IDOCRASE.polished(), path);
            item(IDOCRASE.exquisite(), path);

            path = "resources/";

            item(RAW_AURACHALCUM, path);
            item(AURACHALCUM, path);
            item(ELDRITCH_ALLOY, path);

            item(COPPER_CORE, path);
            item(LARIMAR_CORE, path);
            item(AURACHALCUM_CORE, path);
            item(ELDRITCH_CORE, path);
            item(SPELL_CIRCLE_CORE, path);

            item(AURAGLASS_SHARD, path);
            item(AURAGLASS_DUST, path);
        }

        // Ore Processing
        {
            String path = "dusts/";
            item(CRUSHED_RAW_COPPER, path);
            item(CRUSHED_RAW_IRON, path);
            item(CRUSHED_RAW_GOLD, path);
            item(PURIFIED_RAW_COPPER, path);
            item(PURIFIED_RAW_IRON, path);
            item(PURIFIED_RAW_GOLD, path);
        }

        // Block items
        ArcaneContent.ITEMS.getEntries()
                .stream()
                .filter(item -> item.get() instanceof BlockItem && item != SPELL_CHALK)
                .forEach(this::blockItem);
    }

    private void build(String name, String parent, ResourceLocation texture) {
        getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(parent))
                .texture("layer0", texture);
    }

    private void basicWand(DeferredItem<?> item, String texture) {
        getBuilder(item.get().toString())
                .parent(new ModelFile.UncheckedModelFile("arcane:item/basic_wand"))
                .texture("stick", texture);
    }

    private void advancedWand(DeferredItem<?> item, String texture) {
        getBuilder(item.get().toString())
                .parent(new ModelFile.UncheckedModelFile("arcane:item/advanced_wand"))
                .texture("ring", "minecraft:block/iron_block")
                .texture("stick", texture);
    }

    private void mysticalWand(DeferredItem<?> item, String texture) {
        getBuilder(item.get().toString())
                .parent(new ModelFile.UncheckedModelFile("arcane:item/advanced_wand"))
                .texture("ring", "arcane:block/larimar_block")
                .texture("stick", texture);
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
