package martian.arcane.item;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.item.AbstractAuraItem;
import net.minecraft.world.item.Item;

public class ItemAuraglassBottle extends AbstractAuraItem {
    public ItemAuraglassBottle(int maxAura) {
        super(maxAura, true, true, new Item.Properties().stacksTo(1));
    }
}
