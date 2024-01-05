package martian.arcane.registry;

import martian.arcane.api.capability.IAuraStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public final class ArcaneCapabilities {
    public static final Capability<IAuraStorage> AURA_STORAGE = CapabilityManager.get(new CapabilityToken<>(){});
}
