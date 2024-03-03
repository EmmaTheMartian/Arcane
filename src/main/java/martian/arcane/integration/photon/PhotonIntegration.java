package martian.arcane.integration.photon;

import martian.arcane.api.integration.AbstractIntegration;

public class PhotonIntegration extends AbstractIntegration {
    public static final PhotonIntegration INSTANCE = new PhotonIntegration();

    public PhotonIntegration() {
        super("photon");
    }

    @Override
    protected void onLoad() {
    }
}
