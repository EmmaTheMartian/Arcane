package martian.arcane.api.block.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public interface IAuraometerOutput {
    record Context(@Nullable ItemStack heldStack, boolean detailed) {}

    List<Component> getText(List<Component> text, Context context);
}
