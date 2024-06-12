package martian.arcane.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

public class ArcaneKeybindings {
    public static final Lazy<KeyMapping> OPEN_ENDERPACK = Lazy.of(() -> new KeyMapping(
            "key.arcane.open_enderpack",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            "key.categories.arcane.arcane"
    ));

    public static final Lazy<KeyMapping> WANDBOOK_NEXT_SPELL = Lazy.of(() -> new KeyMapping(
            "key.arcane.wandbook_next_spell",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            "key.categories.arcane.arcane"
    ));

    public static final Lazy<KeyMapping> WANDBOOK_PREV_SPELL = Lazy.of(() -> new KeyMapping(
            "key.arcane.wandbook_prev_spell",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "key.categories.arcane.arcane"
    ));
}
