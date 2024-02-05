package martian.arcane.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.spells.SpellBreaking;
import martian.arcane.spells.SpellHammering;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ArcaneSpells extends ArcaneRegistry {
    public static final DeferredRegister<AbstractSpell> REGISTER = DeferredRegister.create(ArcaneMod.id("spells"), ArcaneMod.MODID);
    public static final Supplier<IForgeRegistry<AbstractSpell>> REGISTRY = REGISTER.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<SpellBreaking> BREAKING = REGISTER.register("breaking", SpellBreaking::new);
    public static final RegistryObject<SpellHammering> HAMMERING = REGISTER.register("hammering", SpellHammering::new);

    public static AbstractSpell getSpellById(ResourceLocation id) {
        //noinspection OptionalGetWithoutIsPresent
        return REGISTER
                .getEntries()
                .stream()
                .filter(spell -> getId(spell.get()).equals(id))
                .findFirst()
                .get()
                .get();
    }

    public static ResourceLocation getId(AbstractSpell spell) {
        return REGISTRY.get().getKey(spell);
    }
}
