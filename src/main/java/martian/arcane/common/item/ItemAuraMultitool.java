package martian.arcane.common.item;

import martian.arcane.api.Raycasting;
import martian.arcane.api.item.IAuraConfigurator;
import martian.arcane.api.item.IAuraWrench;
import martian.arcane.api.item.IAuraometer;
import martian.arcane.common.registry.ArcaneDataComponents;
import martian.arcane.common.registry.ArcaneItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemAuraMultitool extends Item implements IAuraConfigurator, IAuraWrench, IAuraometer {
    public enum Mode {
        CONFIGURE,
        WRENCH,
        AURAOMETER,
    }

    public ItemAuraMultitool() {
        //noinspection DataFlowIssue
        super(new Properties().stacksTo(1).component(ArcaneDataComponents.TARGET_POS, null).component(ArcaneDataComponents.MODE, Mode.CONFIGURE.toString()));
    }

    public static Mode getMode(ItemStack stack) {
        return Mode.valueOf(stack.get(ArcaneDataComponents.MODE));
    }

    public static Mode getNextMode(Mode mode) {
        return switch (mode) {
            case CONFIGURE -> Mode.WRENCH;
            case WRENCH -> Mode.AURAOMETER;
            case AURAOMETER -> Mode.CONFIGURE;
        };
    }

    public static void setMode(ItemStack stack, Mode mode) {
        stack.set(ArcaneDataComponents.MODE, mode.toString());
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);

        if (
                !player.level().isClientSide &&
                player.isCrouching() &&
                Raycasting.blockRaycast(player, player.blockInteractionRange(), false) == null
        ) {
            setMode(stack, getNextMode(getMode(stack)));
            player.sendSystemMessage(Component.translatable("messages.arcane.set_mode_to").append(getMode(stack).toString()));
            return InteractionResultHolder.success(stack);
        }

        return switch (getMode(stack)) {
            case CONFIGURE -> ArcaneItems.AURA_CONFIGURATOR.get().use(level, player, hand);
            case WRENCH -> ArcaneItems.AURA_WRENCH.get().use(level, player, hand);
            case AURAOMETER -> ArcaneItems.AURAOMETER.get().use(level, player, hand);
        };
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> text, TooltipFlag flag) {
        super.appendHoverText(stack, context, text, flag);
        text.add(Component.translatable("item.arcane.aura_multitool.tooltip.1"));
        text.add(Component.translatable("item.arcane.aura_multitool.tooltip.2"));
        text.add(Component.translatable("messages.arcane.mode").append(getMode(stack).toString()));
    }

    @Override
    public boolean getIsConfigurator(Level level, Player player, BlockState state, BlockPos pos, ItemStack stack) {
        return getMode(stack) == Mode.CONFIGURE;
    }

    @Override
    public boolean getIsWrench(Level level, Player player, BlockState state, BlockPos pos, ItemStack stack) {
        return getMode(stack) == Mode.WRENCH;
    }
}