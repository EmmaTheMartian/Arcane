package martian.arcane.common.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.api.spell.AbstractSpell;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class ArcaneRegistries extends ArcaneRegistry {
    public ArcaneRegistries() { super(); }

    public static final ResourceKey<Registry<AbstractSpell>> SPELLS_KEY = ResourceKey.createRegistryKey(ArcaneMod.id("spells"));
    public static final Registry<AbstractSpell> SPELLS = new RegistryBuilder<>(SPELLS_KEY).create();

    public static void registerRegisters(NewRegistryEvent event) {
        event.register(SPELLS);
    }
}
