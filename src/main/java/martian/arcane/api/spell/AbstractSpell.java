package martian.arcane.api.spell;

import martian.arcane.api.ArcaneRegistries;
import martian.arcane.common.item.ItemAuraWand;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public abstract class AbstractSpell {
    public final int minLevel;

    public AbstractSpell(int minLevel) {
        this.minLevel = minLevel;
    }

    public Component getSpellName() {
        return Component.translatable("spell." + Objects.requireNonNull(ArcaneRegistries.SPELLS.getKey(this)).toShortLanguageKey() + ".name");
    }

    public Component getItemName(ItemAuraWand item, ItemStack stack) {
        return Component.translatable("spell." + Objects.requireNonNull(ArcaneRegistries.SPELLS.getKey(this)).toShortLanguageKey() + ".name.item");
    }

    public boolean isValidWand(ItemAuraWand wand) {
        return wand.level >= minLevel;
    }

    public int getCooldownTicks(CastContext context) {
        return 20;
    }

    public abstract int getAuraCost(CastContext context);

    public abstract CastResult cast(CastContext context);
}
