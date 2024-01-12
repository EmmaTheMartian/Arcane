package martian.arcane.api.block.entity;

import net.minecraft.network.chat.Component;

import java.util.List;

public interface IAuraometerOutput {
    List<Component> getText(List<Component> text);
}
