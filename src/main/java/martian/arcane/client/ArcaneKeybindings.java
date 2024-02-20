package martian.arcane.client;

import com.mojang.blaze3d.platform.InputConstants;
import martian.arcane.api.ArcaneRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

public class ArcaneKeybindings extends ArcaneRegistry {
    public static final Lazy<KeyMapping> OPEN_ENDERPACK = Lazy.of(() -> new KeyMapping(
            "key.arcane.open_enderpack",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            "key.categories.arcane.arcane"
    ));
}
