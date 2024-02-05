package martian.arcane.api.spell;

import martian.arcane.item.ItemAuraWand;
import martian.arcane.registry.ArcaneSpells;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public abstract class AbstractSpell {
    public final int minLevel;

    public AbstractSpell(int minLevel) {
        this.minLevel = minLevel;
    }

    public Component getSpellName(ItemStack stack) {
        return Component.translatable("spell." + ArcaneSpells.getId(this).toShortLanguageKey() + ".name");
    }

    public Component getItemName(ItemAuraWand item, ItemStack stack) {
        return Component.translatable("spell." + ArcaneSpells.getId(this).toShortLanguageKey() + ".name.item");
    }

    public boolean isValidWand(ItemAuraWand wand) {
        return wand.level >= minLevel;
    }

    public abstract int getAuraCost(ItemAuraWand wand, ItemStack stack);

    public abstract void cast(ItemAuraWand wand, ItemStack stack, Level level, Player caster, InteractionHand castHand, HitResult hit);
}
