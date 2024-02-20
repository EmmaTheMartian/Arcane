package martian.arcane.client;

import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import martian.arcane.ArcaneMod;

public class ArcaneFx {
    public static final FX ON_CAST = FXHelper.getFX(ArcaneMod.id("on_cast"));
    public static final FX ON_CAST_CONSTANT = FXHelper.getFX(ArcaneMod.id("on_cast_constant"));
    public static final FX ON_CAST_GRAVITY = FXHelper.getFX(ArcaneMod.id("on_cast_gravity"));
    public static final FX SPELL_CIRCLE_PLACE = FXHelper.getFX(ArcaneMod.id("spell_circle_place"));
    public static final FX SPELL_CIRCLE_INIT = FXHelper.getFX(ArcaneMod.id("spell_circle_init"));
}
