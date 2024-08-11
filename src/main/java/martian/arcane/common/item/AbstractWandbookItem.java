package martian.arcane.common.item;

import martian.arcane.ArcaneTags;
import martian.arcane.api.ArcaneRegistries;
import martian.arcane.api.aura.AuraRecord;
import martian.arcane.api.block.entity.IAuraometerOutput;
import martian.arcane.api.item.AbstractAuraItem;
import martian.arcane.api.item.IAuraWand;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.wand.WandbookData;
import martian.arcane.client.ArcaneClient;
import martian.arcane.client.ArcaneKeybindings;
import martian.arcane.common.ArcaneContent;
import martian.arcane.common.networking.c2s.C2SSetSelectionComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public abstract class AbstractWandbookItem extends AbstractAuraItem implements IAuraWand, IAuraometerOutput {
    public final boolean supportsWandSelection;
    public final Supplier<Integer> maxWandsSupplier, maxAuraSupplier;

    public AbstractWandbookItem(Supplier<Integer> maxWandsSupplier, Supplier<Integer> maxAuraSupplier, boolean supportsWandSelection, Properties properties) {
        //noinspection DataFlowIssue
        super(null, properties.component(ArcaneContent.DC_WANDBOOK_DATA, null));
        this.maxWandsSupplier = maxWandsSupplier;
        this.maxAuraSupplier = maxAuraSupplier;
        this.supportsWandSelection = supportsWandSelection;
    }

    @Override
    protected AuraRecord getDefaultAuraRecord() {
        return new AuraRecord(maxAuraSupplier.get(), 0, false, true);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!this.supportsWandSelection || !level.isClientSide() || !isSelected || !ArcaneClient.isGameActive())
            return;

        if (level.isClientSide) {
            if (ArcaneKeybindings.WANDBOOK_NEXT_SPELL.get().consumeClick()) {
                WandbookData data = getData(stack);
                int selection = data.selection();
                if (selection < getUsedSlotCount(stack) - 1)
                    PacketDistributor.sendToServer(new C2SSetSelectionComponent(slotId, selection + 1));
            } else if (ArcaneKeybindings.WANDBOOK_PREV_SPELL.get().consumeClick()) {
                WandbookData data = getData(stack);
                int selection = data.selection();
                if (selection > 0)
                    PacketDistributor.sendToServer(new C2SSetSelectionComponent(slotId, selection - 1));
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> text, TooltipFlag flag) {
        super.appendHoverText(stack, context, text, flag);
        text.addAll(getInfoText(stack, true));
    }

    @Override
    public List<Component> getText(List<Component> text, IAuraometerOutput.Context context) {
        text.add(mapAuraStorage(context.heldStack(), aura -> Component
                .translatable("messages.arcane.aura")
                .append(Integer.toString(aura.getAura()))
                .append("/")
                .append(Integer.toString(aura.getMaxAura()))
                .withStyle(ChatFormatting.LIGHT_PURPLE)));
        text.addAll(getInfoText(context.heldStack(), false));
        return text;
    }

    public List<Component> getInfoText(ItemStack stack, boolean includeSelectionInfo) {
        WandbookData data = getData(stack);
        List<Component> text = new ArrayList<>();
        if (data == null)
            return text;

        var sel = data.selection();

        if (includeSelectionInfo) {
            if (supportsWandSelection)
                text.add(Component.translatable("messages.arcane.selection").append(Integer.toString(sel + 1)).withStyle(ChatFormatting.BLUE));
            text.add(Component.translatable("messages.arcane.holding_n_of_n_wands", getUsedSlotCount(stack), data.maxWands()).withStyle(ChatFormatting.BLUE));
        }

        for (int i = 0; i < data.wands().size(); i++) {
            var wand = data.wands().get(i);
            if (wand.isEmpty())
                continue;

            text.add(Component.literal(i + 1 + ". ").append(wand.getDisplayName().copy()
                    .withStyle(supportsWandSelection && i == sel ? ChatFormatting.GOLD : ChatFormatting.WHITE)));
        }
        return text;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (stack.getCount() == 1 && action == ClickAction.SECONDARY) {
            if (slot.getItem().isEmpty()) {
                playRemoveOneSound(player);
                ItemStack s1 = removeLastWand(stack);
                ItemStack s2 = slot.safeInsert(s1);
                return tryAddWand(stack, s2);
            } else {
                if (tryAddWand(stack, slot.getItem())) {
                    slot.set(ItemStack.EMPTY);
                    playInsertSound(player);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (stack.getCount() == 1 && action == ClickAction.SECONDARY && slot.allowModification(player)) {
            if (other.isEmpty()) {
                ItemStack wand = removeLastWand(stack);
                if (!wand.isEmpty()) {
                    playRemoveOneSound(player);
                    access.set(wand);
                    return true;
                }
            } else {
                if (tryAddWand(stack, other)) {
                    playInsertSound(player);
                    access.set(ItemStack.EMPTY);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean shouldCauseReequipAnimation(ItemStack old, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public int getCastLevel(CastContext context) {
        assert context instanceof CastContext.WandContext;
        var data = getData(((CastContext.WandContext) context).castingStack);
        if (!data.wands().get(data.selection()).isEmpty()) {
            var item = data.wands().get(data.selection());
            if (item.getItem() instanceof IAuraWand wand)
                return wand.getCastLevel(context);
        }
        return 0;
    }

    public WandbookData getData(ItemStack stack) {
        return WandbookData.getOrCreate(stack, () -> new WandbookData(maxWandsSupplier.get()));
    }

    public <T> T mapData(ItemStack stack, Function<WandbookData, T> function) {
        return function.apply(getData(stack));
    }

    public void mutateData(ItemStack stack, UnaryOperator<WandbookData> operator) {
        stack.set(ArcaneContent.DC_WANDBOOK_DATA, operator.apply(getData(stack)));
    }

    public void setWand(ItemStack wandbookStack, ItemStack wandStack, int slot) {
        mutateData(wandbookStack, it -> {
            it.wands().set(slot, wandStack);
            return it;
        });
    }

    public @Nullable AbstractSpell getWandSpell(ItemStack stack, int slot) {
        return ArcaneRegistries.SPELLS.get(getWandSpellId(stack, slot));
    }

    public boolean wandHasSpell(ItemStack stack, int slot) {
        return !getWand(stack, slot).isEmpty() && getWandSpellId(stack, slot) != null;
    }

    public @Nullable ResourceLocation getWandSpellId(ItemStack stack, int slot) {
        return getWand(stack, slot).get(ArcaneContent.DC_SPELL);
    }

    public List<ItemStack> getWands(ItemStack stack) {
        return mapData(stack, WandbookData::wands);
    }

    public ItemStack getWand(ItemStack stack, int slot) {
        return getWands(stack).get(slot);
    }

    public int getUsedSlotCount(ItemStack stack) {
        return mapData(stack, data -> {
            int count = 0;
            for (ItemStack wand : data.wands())
                if (!wand.isEmpty())
                    count++;
            return count;
        });
    }

    public int findFirstEmptySlot(ItemStack wandbookStack) {
        final var wands = getWands(wandbookStack);
        for (int i = 0; i < wands.size(); i++)
            if (wands.get(i).isEmpty())
                return i;
        return -1;
    }

    public int findLastUsedSlot(ItemStack wandbookStack) {
        final var wands = getWands(wandbookStack);
        for (int i = wands.size() - 1; i >= 0; i--)
            if (!wands.get(i).isEmpty())
                return i;
        return -1;
    }

    public boolean tryAddWand(ItemStack wandbookStack, ItemStack wandStack) {
        if (wandStack.isEmpty() || !wandStack.is(ArcaneTags.WANDS) || ItemWand.getSpellId(wandStack) == null)
            return false;
        int slot = findFirstEmptySlot(wandbookStack);
        if (slot == -1)
            return false;
        setWand(wandbookStack, wandStack, slot);
        return true;
    }

    public ItemStack removeLastWand(ItemStack wandbookStack) {
        int slot = findLastUsedSlot(wandbookStack);
        if (slot == -1)
            return ItemStack.EMPTY;
        ItemStack wand = getWand(wandbookStack, slot).copy();
        setWand(wandbookStack, ItemStack.EMPTY, slot);
        // Select the new last wand in the book unless we removed the topmost (ie, the only remaining) wand
        if (supportsWandSelection && getData(wandbookStack).selection() == getData(wandbookStack).maxWands() - 1 && getUsedSlotCount(wandbookStack) > 0)
            mutateData(wandbookStack, data -> data.withSelection(findLastUsedSlot(wandbookStack)));
        return wand;
    }

    protected void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    protected void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }
}
