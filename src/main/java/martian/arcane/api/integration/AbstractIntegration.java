package martian.arcane.api.integration;

import net.neoforged.fml.ModList;

public abstract class AbstractIntegration {
    private boolean isLoaded = false;
    public final String modid;

    protected AbstractIntegration(String modid) {
        this.modid = modid;
    }

    protected abstract void onLoad();

    public boolean load() {
        if (ModList.get().isLoaded(modid)) {
            isLoaded = true;
            onLoad();
            return true;
        }
        return false;
    }

    public boolean isLoaded() {
        return isLoaded;
    }
}
