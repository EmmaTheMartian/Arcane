package martian.arcane.integration.kubejs;

import martian.arcane.api.integration.AbstractIntegration;

public class KubeJSIntegration extends AbstractIntegration {
    public static final KubeJSIntegration INSTANCE = new KubeJSIntegration();

    protected KubeJSIntegration() {
        super("kubejs");
    }

    @Override
    protected void onLoad() {
    }
}
