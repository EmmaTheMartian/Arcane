package martian.arcane.integration;

public class ArcaneIntegrations {
    public static final AbstractIntegration KUBEJS = simple("kubejs");
    public static final AbstractIntegration PEHKUI = simple("pehkui");

    private static AbstractIntegration simple(String id) {
        return new AbstractIntegration(id) {
            @Override
            protected void onLoad() {}
        };
    }
}
