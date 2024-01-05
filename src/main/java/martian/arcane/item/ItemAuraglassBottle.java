package martian.arcane.item;

import martian.arcane.ArcaneConfig;
import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.capability.IAuraStorage;
import martian.arcane.api.item.AbstractAuraItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemAuraglassBottle extends AbstractAuraItem {
    public ItemAuraglassBottle() {
        super(ArcaneStaticConfig.AuraMaximums.AURAGLASS_BOTTLE, true, new Item.Properties().stacksTo(1));
    }
}
