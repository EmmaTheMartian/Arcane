package martian.arcane.api.spell;

import martian.arcane.common.item.ItemAuraWand;
import martian.arcane.common.registry.ArcaneSpells;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractSpell {
    public final int minLevel;

    public AbstractSpell(int minLevel) {
        this.minLevel = minLevel;
    }

    public Component getSpellName() {
        return Component.translatable("spell." + ArcaneSpells.getId(this).toShortLanguageKey() + ".name");
    }

    public Component getItemName(ItemAuraWand item, ItemStack stack) {
        return Component.translatable("spell." + ArcaneSpells.getId(this).toShortLanguageKey() + ".name.item");
    }

    public boolean isValidWand(ItemAuraWand wand) {
        return wand.level >= minLevel;
    }

    public int getCooldownTicks(CastContext context) {
        return 20;
    }

    public abstract CastResult cast(CastContext context);
}
