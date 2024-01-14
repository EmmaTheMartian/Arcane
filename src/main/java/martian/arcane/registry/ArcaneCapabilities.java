package martian.arcane.registry;

import martian.arcane.api.ArcaneRegistry;
import martian.arcane.api.capability.IAuraStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

@SuppressWarnings("unused")
public final class ArcaneCapabilities extends ArcaneRegistry {
    public static final Capability<IAuraStorage> AURA_STORAGE = CapabilityManager.get(new CapabilityToken<>(){});
}
