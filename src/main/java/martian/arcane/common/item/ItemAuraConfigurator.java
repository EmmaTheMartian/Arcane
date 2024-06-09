package martian.arcane.common.item;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.Raycasting;
import martian.arcane.common.block.aura.extractor.BlockEntityAuraExtractor;
import martian.arcane.common.block.aura.inserter.BlockEntityAuraInserter;
import martian.arcane.common.registry.ArcaneDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
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
import java.util.Objects;

public class ItemAuraConfigurator extends Item {
    public ItemAuraConfigurator() {
        //noinspection DataFlowIssue
        super(new Item.Properties().stacksTo(1).component(ArcaneDataComponents.TARGET_POS.get(), null));
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide)
            return InteractionResultHolder.success(stack);

        BlockHitResult hit = Raycasting.blockRaycast(player, player.blockInteractionRange(), false);

        if (player.isCrouching()) {
            if (hit == null) {
                stack.remove(ArcaneDataComponents.TARGET_POS);
                return InteractionResultHolder.success(stack);
            }

            BlockEntity l = level.getBlockEntity(hit.getBlockPos());
            if (l instanceof BlockEntityAuraExtractor extractor)
                BlockEntityAuraExtractor.removeTarget(extractor);
        }

        if (hit == null)
            return InteractionResultHolder.fail(stack);

        if (stack.has(ArcaneDataComponents.TARGET_POS)) {
            BlockEntity l = level.getBlockEntity(hit.getBlockPos());
            if (l instanceof BlockEntityAuraInserter inserter) {
//                BlockPos pos1 = NBTHelpers.getBlockPos(nbt, NBTHelpers.KEY_CONFIGURATOR_P1);
                BlockPos pos1 = Objects.requireNonNull(stack.get(ArcaneDataComponents.TARGET_POS));
                BlockEntity e = level.getBlockEntity(pos1);

                if (e instanceof BlockEntityAuraExtractor extractor) {
                    if (!pos1.closerToCenterThan(l.getBlockPos().getCenter(), ArcaneStaticConfig.AURA_EXTRACTOR_MAX_DISTANCE)) {
                        player.sendSystemMessage(Component
                                .translatable("messages.arcane.distance_too_far")
                                .withStyle(ChatFormatting.RED));

                        return InteractionResultHolder.fail(stack);
                    }

                    BlockEntityAuraExtractor.setTarget(extractor, inserter);
                    player.sendSystemMessage(Component.translatable("messages.arcane.linked"));
                    stack.remove(ArcaneDataComponents.TARGET_POS);
                    return InteractionResultHolder.success(stack);
                }
            }
        } else {
            BlockEntity e = level.getBlockEntity(hit.getBlockPos());
            if (e instanceof BlockEntityAuraExtractor extractor) {
                if (player.isCrouching()) {
                    BlockEntityAuraExtractor.removeTarget(extractor);
                    player.sendSystemMessage(Component.translatable("messages.arcane.unlinked"));
                    return InteractionResultHolder.success(stack);
                }

                stack.set(ArcaneDataComponents.TARGET_POS, hit.getBlockPos());
                player.sendSystemMessage(Component.translatable("messages.arcane.selected"));
                return InteractionResultHolder.success(stack);
            }
        }

        return InteractionResultHolder.fail(stack);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> text, TooltipFlag flag) {
        super.appendHoverText(stack, context, text, flag);
        text.add(Component.translatable("item.arcane.aura_configurator.tooltip.1"));
        text.add(Component.translatable("item.arcane.aura_configurator.tooltip.2"));
        if (stack.has(ArcaneDataComponents.TARGET_POS)) {
            text.add(Component
                    .translatable("messages.arcane.linking_from")
                    .append(Objects.requireNonNull(stack.get(ArcaneDataComponents.TARGET_POS)).toShortString())
                    .withStyle(ChatFormatting.AQUA));
        }
    }
}