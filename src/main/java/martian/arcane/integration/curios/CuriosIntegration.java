package martian.arcane.integration.curios;

import martian.arcane.common.registry.ArcaneItems;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosIntegration {
    public static boolean curiosLoaded = false;

    public static void init() {
        curiosLoaded = true;
        CuriosApi.registerCurio(ArcaneItems.ENDERPACK.get(), new CurioEnderpack());
    }
}
