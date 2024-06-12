package martian.arcane.common.item;

import martian.arcane.ArcaneTags;
import martian.arcane.api.item.AbstractAuraItem;
import martian.arcane.api.item.IAuraWand;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.spell.MutableWandbookData;
import martian.arcane.api.spell.WandbookDataRecord;
import martian.arcane.client.ArcaneKeybindings;
import martian.arcane.common.networking.c2s.C2SSetSelectionComponent;
import martian.arcane.common.registry.ArcaneDataComponents;
import martian.arcane.common.registry.ArcaneRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ItemWandbook extends AbstractAuraItem implements IAuraWand {
    public final int defaultMaxWands;

    public ItemWandbook(int maxWands, int maxAura) {
        super(maxAura, false, true, new Properties()
                .stacksTo(1)
                .component(ArcaneDataComponents.WANDBOOK_DATA, null));
        this.defaultMaxWands = maxWands;
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        int slot = getData(stack).selection();

        if (player.getCooldowns().isOnCooldown(this) || !wandHasSpell(stack, slot))
            return InteractionResultHolder.fail(stack);

        AbstractSpell spell = getWandSpell(stack, slot);
        if (spell == null)
            return InteractionResultHolder.fail(stack);

        mutateAuraStorage(stack, aura -> {
            CastContext.WandContext ctx = new CastContext.WandContext(level, aura, player, hand, stack, this);

            int cost = spell.getAuraCost(ctx);
            if (aura.getAura() < cost) {
                return aura;
            }

            player.getCooldowns().addCooldown(this, spell.getCooldownTicks(ctx));

            spell.cast(ctx);
            aura.removeAura(cost);
            return aura;
        });

        return InteractionResultHolder.success(stack);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!isSelected)
            return;

        if (level.isClientSide) {
            WandbookDataRecord data = getData(stack);
            if (data != null) {
                var selection = data.selection();
                if (ArcaneKeybindings.WANDBOOK_NEXT_SPELL.get().consumeClick() && selection < data.maxWands()) {
                    PacketDistributor.sendToServer(new C2SSetSelectionComponent(slotId, selection + 1));
                } else if (ArcaneKeybindings.WANDBOOK_PREV_SPELL.get().consumeClick() && selection > 0) {
                    PacketDistributor.sendToServer(new C2SSetSelectionComponent(slotId, selection - 1));
                }
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> text, TooltipFlag flag) {
        super.appendHoverText(stack, context, text, flag);

        WandbookDataRecord data = getData(stack);

        if (data != null) {
            var sel = data.selection();

            for (int i = 0; i < data.wands().size(); i++) {
                var wand = data.wands().get(i);
                if (wand.isEmpty())
                    continue;

                if (i == sel)
                    text.add(Component.literal(" - ").append(wand.getDisplayName().copy().withStyle(ChatFormatting.GOLD)));
                else
                    text.add(Component.literal(" - ").append(wand.getDisplayName().copy().withStyle(ChatFormatting.WHITE)));
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (stack.getCount() == 1 && action == ClickAction.SECONDARY) {
            if (slot.getItem().isEmpty()) {
                playRemoveOneSound(player);
                ItemStack s1 = removeFirstWand(stack);
                ItemStack s2 = slot.safeInsert(s1);
                tryAddWand(stack, s2);
            } else {
                if (tryAddWand(stack, slot.getItem())) {
                    slot.set(ItemStack.EMPTY);
                    playInsertSound(player);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (stack.getCount() != 1) {
            return false;
        } else if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
            if (other.isEmpty()) {
                ItemStack wand = removeFirstWand(stack);
                if (!wand.isEmpty()) {
                    playRemoveOneSound(player);
                    access.set(wand);
                }
            } else {
                if (tryAddWand(stack, other)) {
                    playInsertSound(player);
                    access.set(ItemStack.EMPTY);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int getCastLevel(ItemStack stack) {
        var data = getData(stack);
        if (!data.wands().get(data.selection()).isEmpty()) {
            var item = data.wands().get(data.selection());
            if (item.getItem() instanceof IAuraWand wand)
                return wand.getCastLevel(item);
        }
        return 0;
    }

    public WandbookDataRecord getData(ItemStack stack) {
        return WandbookDataRecord.getOrCreate(stack, () -> new WandbookDataRecord(defaultMaxWands));
    }

    public <T> T mapData(ItemStack stack, Function<WandbookDataRecord, T> function) {
        return function.apply(getData(stack));
    }

    public void mutateData(ItemStack stack, UnaryOperator<MutableWandbookData> operator) {
        stack.set(ArcaneDataComponents.WANDBOOK_DATA, operator.apply(getData(stack).unfreeze()).freeze());
    }

    public void setWand(ItemStack wandbookStack, ItemStack wandStack, int slot) {
        mutateData(wandbookStack, it -> {
            it.wands.set(slot, wandStack);
            return it;
        });
    }

    public @Nullable AbstractSpell getWandSpell(ItemStack stack, int slot) {
        return ArcaneRegistries.SPELLS.get(getWandSpellId(stack, slot));
    }

    public boolean wandHasSpell(ItemStack stack, int slot) {
        return getWandSpellId(stack, slot) != null;
    }

    public @Nullable ResourceLocation getWandSpellId(ItemStack stack, int slot) {
        return getWand(stack, slot).get(ArcaneDataComponents.SPELL);
    }

    public List<ItemStack> getWands(ItemStack stack) {
        return mapData(stack, WandbookDataRecord::wands);
    }

    public ItemStack getWand(ItemStack stack, int slot) {
        return getWands(stack).get(slot);
    }

    public int findFirstEmptySlot(ItemStack wandbookStack) {
        final var wands = getWands(wandbookStack);
        for (int i = 0; i < wands.size(); i++) {
            if (wands.get(i).isEmpty())
                return i;
        }
        return -1;
    }

    public int findFirstUsedSlot(ItemStack wandbookStack) {
        final var wands = getWands(wandbookStack);
        for (int i = 0; i < wands.size(); i++) {
            if (!wands.get(i).isEmpty())
                return i;
        }
        return -1;
    }

    public boolean tryAddWand(ItemStack wandbookStack, ItemStack wandStack) {
        if (wandStack.isEmpty() || !wandStack.is(ArcaneTags.WANDS))
            return false;
        int slot = findFirstEmptySlot(wandbookStack);
        if (slot == -1)
            return false;
        setWand(wandbookStack, wandStack, slot);
        return true;
    }

    public ItemStack removeFirstWand(ItemStack wandbookStack) {
        int slot = findFirstUsedSlot(wandbookStack);
        if (slot == -1)
            return ItemStack.EMPTY;
        ItemStack wand = getWand(wandbookStack, slot).copy();
        setWand(wandbookStack, ItemStack.EMPTY, slot);
        return wand;
    }

    protected void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    protected void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }
}
