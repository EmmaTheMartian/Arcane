package martian.arcane.common.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CraftingSpell;
import martian.arcane.common.spell.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unused")
public class ArcaneSpells extends ArcaneRegistry {
    public ArcaneSpells() { super(REGISTER); }

    private static final DeferredRegister<AbstractSpell> REGISTER = DeferredRegister.create(ArcaneRegistries.SPELLS, ArcaneMod.MODID);

    public static final DeferredHolder<AbstractSpell, ?>
            BREAKING = REGISTER.register("breaking", SpellBreaking::new),
            HAMMERING = REGISTER.register("hammering", () -> new CraftingSpell(ArcaneRecipeTypes.HAMMERING, ArcaneStaticConfig.SpellMinLevels.HAMMERING, ArcaneStaticConfig.SpellCosts.HAMMERING)),
            PURIFYING = REGISTER.register("purifying", () -> new CraftingSpell(ArcaneRecipeTypes.PURIFYING, ArcaneStaticConfig.SpellMinLevels.PURIFYING, ArcaneStaticConfig.SpellCosts.PURIFYING)),
            CLEANSING = REGISTER.register("cleansing", () -> new CraftingSpell(ArcaneRecipeTypes.CLEANSING, ArcaneStaticConfig.SpellMinLevels.CLEANSING, ArcaneStaticConfig.SpellCosts.CLEANSING)),
            BUILDING = REGISTER.register("building", SpellBuilding::new),
            DASHING = REGISTER.register("dashing", SpellDashing::new),
            CRAFTING = REGISTER.register("crafting", SpellCrafting::new),
            ACTIVATOR = REGISTER.register("activator", SpellSpellCircleActivator::new),
            PRESERVATION = REGISTER.register("preservation", SpellPreservation::new),
            LIGHTING = REGISTER.register("lighting", SpellLighting::new)
    ;
}
