package martian.arcane.item;

import martian.arcane.api.NBTHelpers;
import martian.arcane.api.Raycasting;
import martian.arcane.api.capability.IAuraStorage;
import martian.arcane.api.item.AbstractAuraItem;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.registry.ArcaneSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemAuraWand extends AbstractAuraItem {
    public final int level;
    public final double reach = 7.0D;
    private LazyOptional<AbstractSpell> cachedSpell = LazyOptional.empty();

    public ItemAuraWand(int maxAura, int level, Properties properties) {
        super(maxAura, false, true, properties);
        this.level = level;
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide())
            return InteractionResultHolder.success(stack);

        IAuraStorage aura = getAuraStorage(stack).orElseThrow();
        if (!hasSpell(stack))
            return InteractionResultHolder.fail(stack);

        AbstractSpell spell = getSpell(stack);
        if (spell == null)
            return InteractionResultHolder.fail(stack);

        int cost = spell.getAuraCost(this, stack);
        if (aura.getAura() < cost) {
            player.sendSystemMessage(Component.translatable("messages.arcane.not_enough_aura"));
            return InteractionResultHolder.fail(stack);
        }

        aura.setAura(aura.getAura() - cost);
        spell.cast(this, stack, level, player, hand, Raycasting.raycast(player, reach, false));
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> text, @NotNull TooltipFlag flags) {
        super.appendHoverText(stack, level, text, flags);

        tryCacheSpell(stack);
        if (hasSpell(stack))
            text.add(Component
                    .translatable("messages.arcane.spell")
                    .append(getSpellOrElse().getSpellName(stack))
                    .withStyle(ChatFormatting.AQUA));
        else
            text.add(Component.translatable("messages.arcane.no_spell"));

        text.add(Component
                .translatable("messages.arcane.wand_level")
                .append(Integer.toString(this.level)));
    }

    public void setSpell(ResourceLocation newSpell, ItemStack stack) {
        getNBT(stack).putString(NBTHelpers.KEY_SPELL, newSpell.toString());
        tryCacheSpell(stack);
    }

    public void tryCacheSpell(ItemStack stack) {
        ResourceLocation spellId = getSpellId(stack);
        if (!cachedSpell.isPresent() && spellId != null)
            cachedSpell = LazyOptional.of(() -> ArcaneSpells.getSpellById(spellId));
    }

    public @Nullable AbstractSpell getSpell(ItemStack stack) {
        tryCacheSpell(stack);
        if (cachedSpell.isPresent())
            return cachedSpell.resolve().orElseThrow();
        return null;
    }

    public AbstractSpell getSpellOrElse() {
        return cachedSpell.resolve().orElseThrow();
    }

    public static boolean hasSpell(ItemStack stack) {
        return getSpellId(stack) != null;
    }

    public static @Nullable ResourceLocation getSpellId(ItemStack stack) {
        CompoundTag nbt = getNBT(stack);
        if (!nbt.contains(NBTHelpers.KEY_SPELL))
            return null;
        String s = nbt.getString(NBTHelpers.KEY_SPELL);
        return s.equals("null") ? null : ResourceLocation.of(s, ':');
    }

    public static CompoundTag getNBT(ItemStack stack) {
        return stack.getOrCreateTag();
    }
}
