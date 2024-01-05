package martian.arcane.api.datagen;

import martian.arcane.ArcaneMod;
import martian.arcane.registry.ArcaneItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ArcaneItemModelProvider extends ItemModelProvider {
    public ArcaneItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ArcaneMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ArcaneItems.ITEMS.getEntries().forEach(item -> {
            if (item.get() instanceof BlockItem)
                blockItem(item);
            else
                basicItem(item.get());
        });
    }

    private void blockItem(RegistryObject<Item> item) {
        String path = item.getId().getPath();
        withExistingParent("item/" + path, new ResourceLocation(item.getId().getNamespace(), "block/" + path));
    }
}
