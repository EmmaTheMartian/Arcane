package martian.arcane.integration.curios;

import martian.arcane.common.item.ItemEnderpack;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class CurioEnderpack implements ICurioItem {
    public void curioTick(SlotContext slot, ItemStack stack) {
        if (stack.getItem() instanceof ItemEnderpack pack)
            pack.tick(slot.entity());
    }
}
