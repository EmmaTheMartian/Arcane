package martian.arcane.common.item;

import martian.arcane.api.IMachineTierable;
import martian.arcane.api.MachineTier;
import martian.arcane.api.Raycasting;
import martian.arcane.api.block.BlockHelpers;
import martian.arcane.common.ArcaneContent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class ItemUpgradeKit extends Item {
    public ItemUpgradeKit(MachineTier tier) {
        super(new Properties().stacksTo(1).component(ArcaneContent.DC_MACHINE_TIER, tier));
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        BlockHitResult hit = Raycasting.blockRaycast(player, player.blockInteractionRange(), false);
        if (hit == null)
            return InteractionResultHolder.pass(stack);

        BlockState state = level.getBlockState(hit.getBlockPos());
        BlockEntity be = level.getBlockEntity(hit.getBlockPos());

        if (!level.isClientSide && be instanceof IMachineTierable tierable && tierable.isUpgradable()) {
            if (tierable.upgradeTo(stack.get(ArcaneContent.DC_MACHINE_TIER))) {
                stack.consume(1, player);
                BlockHelpers.sync(be);
            } else {
                return InteractionResultHolder.fail(stack);
            }
        }

        return InteractionResultHolder.success(stack);
    }
}
