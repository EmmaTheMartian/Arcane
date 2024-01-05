package martian.arcane.api;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class PropertyHelpers {
    public static BlockBehaviour.Properties basicAuraMachine() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).destroyTime(1.5f);
    }
}
