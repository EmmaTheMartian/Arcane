package martian.arcane.common.item;

import martian.arcane.api.machine.IMachineTierable;
import martian.arcane.api.machine.MachineTier;
import martian.arcane.api.Raycasting;
import martian.arcane.api.block.BlockHelpers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;

public class ItemUpgradeKit extends Item {
    private final Lazy<MachineTier> tier;

    public ItemUpgradeKit(Lazy<MachineTier> tier) {
        super(new Properties().stacksTo(1));
        this.tier = tier;
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        BlockHitResult hit = Raycasting.blockRaycast(player, player.blockInteractionRange(), false);
        if (hit == null)
            return InteractionResultHolder.pass(stack);

        BlockEntity be = level.getBlockEntity(hit.getBlockPos());

        if (!level.isClientSide && be instanceof IMachineTierable tierable && tierable.isUpgradable()) {
            if (tierable.upgradeTo(this.tier.get())) {
                stack.consume(1, player);
                BlockHelpers.sync(be);
            } else {
                return InteractionResultHolder.fail(stack);
            }
        }

        return InteractionResultHolder.success(stack);
    }
}
