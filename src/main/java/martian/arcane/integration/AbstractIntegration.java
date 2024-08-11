package martian.arcane.integration;

import net.neoforged.fml.ModList;

public abstract class AbstractIntegration {
    public final String modid;

    protected AbstractIntegration(String modid) {
        this.modid = modid;
    }

    protected abstract void onLoad();

    public void load() {
        if (isLoaded())
            onLoad();
    }

    public boolean isLoaded() {
        return ModList.get().isLoaded(modid);
    }
}
