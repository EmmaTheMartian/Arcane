package martian.arcane.integration;

import martian.arcane.api.integration.AbstractIntegration;

public class ArcaneIntegrations {
    public static final AbstractIntegration KUBEJS = simple("kubejs");
    public static final AbstractIntegration PHOTON = simple("photon");
    public static final AbstractIntegration PEHKUI = simple("pehkui");

    private static AbstractIntegration simple(String id) {
        return new AbstractIntegration(id) {
            @Override
            protected void onLoad() {}
        };
    }
}
