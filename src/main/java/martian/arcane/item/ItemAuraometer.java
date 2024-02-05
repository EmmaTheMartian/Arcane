package martian.arcane.item;

import martian.arcane.api.Raycasting;
import martian.arcane.api.block.entity.IAuraometerOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemAuraometer extends Item {
    public ItemAuraometer() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity holder, int slot, boolean isHeld) {
        if (holder instanceof Player player) {
            BlockHitResult hit = Raycasting.blockRaycast(holder, player.getBlockReach(), false);
            if (hit == null)
                return;

            BlockEntity entity = level.getBlockEntity(hit.getBlockPos());
            if (entity instanceof IAuraometerOutput) {
                BlockState state = level.getBlockState(hit.getBlockPos());
                level.sendBlockUpdated(hit.getBlockPos(), state, state, 2);
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> text, @NotNull TooltipFlag flags) {
        text.add(Component.translatable("item.arcane.auraometer.tooltip"));
    }
}
