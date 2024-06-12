package martian.arcane.common.item;

import martian.arcane.api.Raycasting;
import martian.arcane.api.aura.AuraRecord;
import martian.arcane.api.aura.IMutableAuraStorage;
import martian.arcane.api.item.AbstractAuraItem;
import martian.arcane.common.ArcaneContent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemAuraglassBottle extends AbstractAuraItem {
    public ItemAuraglassBottle(int maxAura, int pushRate) {
        super(maxAura, true, true, new Item.Properties()
                .stacksTo(1)
                .component(ArcaneContent.DC_ACTIVE, false)
                .component(ArcaneContent.DC_PUSH_RATE, pushRate));
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide)
            return InteractionResultHolder.success(stack);

        if (player.isCrouching()) {
            stack.set(ArcaneContent.DC_ACTIVE, Boolean.FALSE.equals(stack.get(ArcaneContent.DC_ACTIVE)));
        } else {
            BlockHitResult hit = Raycasting.blockRaycast(player, player.blockInteractionRange(), false);
            if (hit == null)
                return InteractionResultHolder.fail(stack);

            BlockEntity e = level.getBlockEntity(hit.getBlockPos());
            if (e == null)
                return InteractionResultHolder.fail(stack);

            if (e instanceof IMutableAuraStorage eAura && eAura.canInsert()) {
                mutateAuraStorage(stack, bottleAura -> {
                    bottleAura.sendAuraTo(eAura, Integer.MAX_VALUE);
                    return bottleAura;
                });
            }
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.isEnchanted() || isActive(stack);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide)
            return;

        if (entity instanceof Player player && isActive(stack)) {
            ItemStack held = player.getMainHandItem();
            if (held.isEmpty() || held.getItem() instanceof ItemAuraglassBottle)
                return;

            if (held.has(ArcaneContent.DC_AURA)) {
                AuraRecord heldAuraRecord = held.get(ArcaneContent.DC_AURA);
                if (heldAuraRecord != null && heldAuraRecord.canInsert()) {
                    mutateAuraStorage(stack, bottleAura -> {
                        mutateAuraStorage(held, heldAura -> {
                            bottleAura.sendAuraTo(heldAura, getPushRate(stack));
                            return heldAura;
                        });
                        return bottleAura;
                    });
                }
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> text, TooltipFlag flag) {
        super.appendHoverText(stack, context, text, flag);

        text.add(Component
                .translatable("messages.arcane.push_rate")
                .append(Integer.toString(getPushRate(stack)))
                .withStyle(ChatFormatting.AQUA));
    }

    public int getPushRate(ItemStack stack) {
        if (stack.has(ArcaneContent.DC_PUSH_RATE)) {
            //noinspection DataFlowIssue
            return stack.get(ArcaneContent.DC_PUSH_RATE);
        }
        return 0;
    }

    public boolean isActive(ItemStack stack) {
        if (stack.has(ArcaneContent.DC_ACTIVE)) {
            //noinspection DataFlowIssue
            return stack.get(ArcaneContent.DC_ACTIVE);
        }
        return false;
    }
}
