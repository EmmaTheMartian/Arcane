package martian.arcane.common.item;

import martian.arcane.api.block.BlockHelpers;
import martian.arcane.api.Raycasting;
import martian.arcane.api.block.entity.IAuraometerOutput;
import martian.arcane.client.ArcaneClient;
import martian.arcane.common.block.aura.extractor.ExtractorLinkingRenderer;
import martian.arcane.common.block.aura.extractor.BlockEntityAuraExtractor;
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

public class ItemAuraometer extends Item {
    private static final int RAYCAST_FREQUENCY = 10; // How often (in ticks) the Auraometer should perform a raycast

    public ItemAuraometer() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void inventoryTick(ItemStack stack, Level level, Entity holder, int slot, boolean isHeld) {
        if (ArcaneClient.clientTicks % RAYCAST_FREQUENCY == 0 && holder instanceof Player player) {
            BlockHitResult hit = Raycasting.blockRaycast(holder, player.getBlockReach(), false);
            if (hit == null)
                return;

            BlockEntity entity = level.getBlockEntity(hit.getBlockPos());
            if (entity instanceof IAuraometerOutput)
                BlockHelpers.sync(entity);
        }
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hit = Raycasting.blockRaycast(player, player.getBlockReach(), false);
        if (hit == null)
            return InteractionResultHolder.fail(stack);

        if (!level.isClientSide)
            return InteractionResultHolder.success(stack);

        if (player.isCrouching()) {
            ExtractorLinkingRenderer.selectedExtractors.clear();
        } else if (level.getBlockEntity(hit.getBlockPos()) instanceof BlockEntityAuraExtractor extractor) {
            ExtractorLinkingRenderer.selectedExtractors.add(extractor.getBlockPos());
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> text, @NotNull TooltipFlag flags) {
        text.add(Component.translatable("item.arcane.auraometer.tooltip"));
    }
}
