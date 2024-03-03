package martian.arcane.integration.photon;

import com.lowdragmc.photon.client.fx.FXHelper;
import martian.arcane.ArcaneMod;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Objects;

public class ArcaneFx {
    public static final FallbackFX ON_CAST = get("on_cast");
    public static final FallbackFX ON_CAST_CONSTANT = get("on_cast_constant");
    public static final FallbackFX ON_CAST_GRAVITY = get("on_cast_gravity");
    public static final FallbackFX SPELL_CIRCLE_PLACE = get("spell_circle_place");
    public static final FallbackFX SPELL_CIRCLE_INIT = get("spell_circle_init");

    private static FallbackFX get(String id) {
        return new FallbackFX(PhotonIntegration.INSTANCE.isLoaded() ? FXHelper.getFX(ArcaneMod.id(id)) : null);
    }
}
