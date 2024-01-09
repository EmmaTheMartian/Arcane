package martian.arcane.item;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.item.AbstractAuraItem;
import net.minecraft.world.item.Item;

public class ItemAuraglassBottle extends AbstractAuraItem {
    public ItemAuraglassBottle() {
        super(ArcaneStaticConfig.AuraMaximums.AURAGLASS_BOTTLE, true, true, new Item.Properties().stacksTo(1));
    }
}
