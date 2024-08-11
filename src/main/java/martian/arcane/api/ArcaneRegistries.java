package martian.arcane.api;

import martian.arcane.ArcaneMod;
import martian.arcane.api.colour.ColourPalette;
import martian.arcane.api.spell.AbstractSpell;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class ArcaneRegistries {
    public static final ResourceKey<Registry<AbstractSpell>> SPELLS_KEY = ResourceKey.createRegistryKey(ArcaneMod.id("spells"));
    public static final Registry<AbstractSpell> SPELLS = new RegistryBuilder<>(SPELLS_KEY).create();

    public static final ResourceKey<Registry<ColourPalette>> PIGMENTS_KEY = ResourceKey.createRegistryKey(ArcaneMod.id("pigments"));
    public static final Registry<ColourPalette> PIGMENTS = new RegistryBuilder<>(PIGMENTS_KEY).create();
}
