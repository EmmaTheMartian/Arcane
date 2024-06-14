package martian.arcane.api.spell;

import martian.arcane.ArcaneConfig;
import martian.arcane.api.ArcaneRegistries;
import martian.arcane.common.item.ItemAuraWand;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

public abstract class AbstractSpell {
    public Component getSpellName() {
        return Component.translatable("spell." + Objects.requireNonNull(ArcaneRegistries.SPELLS.getKey(this)).toShortLanguageKey() + ".name");
    }

    public Component getItemName() {
        return Component.translatable("spell." + Objects.requireNonNull(ArcaneRegistries.SPELLS.getKey(this)).toShortLanguageKey() + ".name.item");
    }

    public boolean isValidWand(ItemAuraWand wand) {
        return wand.level >= getMinLevel();
    }

    public int getCooldownTicks(CastContext context) {
        return getConfig().get("cooldown");
    }

    public int getMinLevel() {
        return getConfig().get("minLevel");
    }

    public int getAuraCost(CastContext context) {
        return getConfig().get("auraCost");
    }

    protected abstract SpellConfig getConfig();

    /**
     * Use CastContext.cast(AbstractSpell) instead so that disabled spells are automatically handled.
     */
    @ApiStatus.Internal
    public abstract CastResult cast(CastContext context);

    public static boolean isDisabled(AbstractSpell spell) {
        return ArcaneConfig.disabledSpells.contains(ArcaneRegistries.SPELLS.getKey(spell));
    }

    public static boolean isDisabled(ResourceLocation spellId) {
        return ArcaneConfig.disabledSpells.contains(spellId);
    }
}
