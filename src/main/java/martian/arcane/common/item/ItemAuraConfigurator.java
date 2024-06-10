package martian.arcane.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemAuraConfigurator extends Item {
    public ItemAuraConfigurator() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> text, TooltipFlag flag) {
        super.appendHoverText(stack, context, text, flag);
        text.add(Component.translatable("item.arcane.aura_configurator.tooltip.1"));
        text.add(Component.translatable("item.arcane.aura_configurator.tooltip.2"));
    }
}
