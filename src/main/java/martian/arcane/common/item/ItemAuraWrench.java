package martian.arcane.common.item;

import martian.arcane.ArcaneTags;
import martian.arcane.api.Raycasting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemAuraWrench extends Item {
    public ItemAuraWrench() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide)
            return InteractionResultHolder.success(stack);

        BlockHitResult hit = Raycasting.blockRaycast(player, player.blockInteractionRange(), false);

        if (player.isCrouching() && hit != null) {
            BlockState hitState = level.getBlockState(hit.getBlockPos());
            if (hitState.is(ArcaneTags.AURA_WRENCH_BREAKABLE)) {
                level.destroyBlock(hit.getBlockPos(), true);
            }

            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> text, TooltipFlag flag) {
        super.appendHoverText(stack, context, text, flag);
        text.add(Component.translatable("item.arcane.aura_wrench.tooltip"));
    }
}
