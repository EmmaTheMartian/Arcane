package martian.arcane.api;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ArcaneRegistry {
    private static final List<ArcaneRegistry> instances = new ArrayList<>();

    private final DeferredRegister<?>[] registries;

    protected ArcaneRegistry(DeferredRegister<?>... registries) {
        instances.add(this);
        this.registries = registries;
    }

    protected void register(IEventBus bus) {
        Arrays.stream(registries).forEach(it -> it.register(bus));
    }

    public static void registerAll(IEventBus bus) {
        instances.forEach(it -> it.register(bus));
    }
}
