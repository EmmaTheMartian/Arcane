package martian.arcane.common.item;

import com.klikli_dev.modonomicon.item.ModonomiconItem;
import net.minecraft.world.item.Rarity;

public class ItemGuidebook extends ModonomiconItem {
    public ItemGuidebook() {
        super(new Properties().stacksTo(1).fireResistant().rarity(Rarity.RARE));
    }
}
