package martian.arcane.common.item;

import martian.arcane.api.NBTHelpers;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.common.registry.ArcaneSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemSpellTablet extends Item {
    public ItemSpellTablet() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> text, @NotNull TooltipFlag flags) {
        text.add(Component.translatable("item.arcane.spell_tablet.tooltip"));
        if (hasSpell(stack)) {
            AbstractSpell spell = ArcaneSpells.getSpellById(getSpell(stack));
            text.add(Component
                    .translatable("messages.arcane.spell")
                    .append(spell.getSpellName())
                    .withStyle(ChatFormatting.AQUA));
            text.add(Component
                    .translatable("messages.arcane.spell_min_level")
                    .append(Integer.toString(spell.minLevel))
                    .withStyle(ChatFormatting.AQUA));
        }
    }

    public static void setSpell(ItemStack stack, ResourceLocation id) {
        getNBT(stack).putString(NBTHelpers.KEY_SPELL, id.toString());
    }

    public static boolean hasSpell(ItemStack stack) {
        return !getNBT(stack).getString(NBTHelpers.KEY_SPELL).equals("null");
    }

    public static CompoundTag getNBT(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        initNBT(tag);
        return tag;
    }

    public static @Nullable ResourceLocation getSpell(ItemStack stack) {
        String id = getNBT(stack).getString(NBTHelpers.KEY_SPELL);
        return id.equals("null") ? null : ResourceLocation.of(id, ':');
    }

    public static void initNBT(CompoundTag nbt) {
        NBTHelpers.init(nbt, NBTHelpers.KEY_SPELL, (nbt_, key) -> nbt.putString(key, "null"));
    }
}
