package martian.arcane.datagen;

import martian.arcane.ArcaneMod;
import martian.arcane.registry.ArcaneItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ArcaneItemModelProvider extends ItemModelProvider {
    public ArcaneItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ArcaneMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ArcaneItems.ITEMS.getEntries().forEach(item -> {
            if (item.get() instanceof BlockItem)
                blockItem(item.get());
            else
                basicItem(item.get());
        });
    }

    private void blockItem(Item item) {
        String path = DataGenUtils.name(item);
        withExistingParent("item/" + path, new ResourceLocation(DataGenUtils.namespace(item), "block/" + path));
    }
}
