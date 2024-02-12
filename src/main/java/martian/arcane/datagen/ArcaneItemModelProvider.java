package martian.arcane.datagen;

import martian.arcane.ArcaneMod;
import martian.arcane.item.ItemAuraWand;
import martian.arcane.registry.ArcaneItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ArcaneItemModelProvider extends ItemModelProvider {
    public ArcaneItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ArcaneMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ArcaneItems.ITEMS.getEntries().forEach(itemHolder -> {
            Item item = itemHolder.get();
            if (item instanceof BlockItem)
                blockItem(item);
            else if (item instanceof ItemAuraWand)
                itemWithTexturePath(item, "wands");
            else if (
                    item == ArcaneItems.CRUSHED_RAW_COPPER.get() ||
                    item == ArcaneItems.CRUSHED_RAW_IRON.get() ||
                    item == ArcaneItems.CRUSHED_RAW_GOLD.get() ||
                    item == ArcaneItems.PURIFIED_RAW_COPPER.get() ||
                    item == ArcaneItems.PURIFIED_RAW_IRON.get() ||
                    item == ArcaneItems.PURIFIED_RAW_GOLD.get()
            )
                itemWithTexturePath(item, "dusts");
            else if (item == ArcaneItems.CREATIVE_AURAGLASS_BOTTLE.get())
                itemWithTexture(item, ArcaneMod.id("item/extreme_auraglass_bottle"));
            else
                basicItem(item);
        });
    }

    private void blankItem(Item item) {
        itemWithTexture(item, new ResourceLocation("minecraft", "item/amethyst_shard"));  // Why amethyst? Why not.
    }

    private void itemWithTexturePath(Item item, String path) {
        getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", namespace(item) + ":item/" + path + "/" + name(item));
    }

    private void itemWithTexture(Item item, ResourceLocation texture) {
        getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", texture);
    }

    private void blockItem(Item item) {
        String path = name(item);
        withExistingParent("item/" + path, new ResourceLocation(namespace(item), "block/" + path));
    }

    public static ResourceLocation key(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }

    public static String name(Item item) {
        return key(item).getPath();
    }

    public static String namespace(Item item) {
        return key(item).getNamespace();
    }
}
