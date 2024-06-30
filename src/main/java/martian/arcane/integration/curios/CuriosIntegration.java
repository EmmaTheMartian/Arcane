package martian.arcane.integration.curios;

import martian.arcane.api.integration.AbstractIntegration;
import martian.arcane.common.ArcaneContent;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosIntegration extends AbstractIntegration {
    public static final CuriosIntegration INSTANCE = new CuriosIntegration();

    private CuriosIntegration() {
        super("curios");
    }

    @Override
    protected void onLoad() {
        CuriosApi.registerCurio(ArcaneContent.ENDERPACK.get(), new CurioEnderpack());
    }
}
