package martian.arcane.item;

import martian.arcane.api.MathHelpers;
import martian.arcane.api.Raycasting;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.api.capability.AuraStorage;
import martian.arcane.api.capability.IAuraStorage;
import martian.arcane.block.entity.BlockEntityAuraExtractor;
import martian.arcane.registry.ArcaneCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ItemAuraometer extends Item {
    public ItemAuraometer() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            return InteractionResultHolder.success(stack);
        }

        BlockHitResult hit = Raycasting.blockRaycast(player, player.getBlockReach(), false);
        if (hit == null) {
            return InteractionResultHolder.pass(stack);
        }
        BlockState block = level.getBlockState(hit.getBlockPos());

        if (!block.hasBlockEntity())
        {
            return InteractionResultHolder.fail(stack);
        }

        BlockEntity be = level.getBlockEntity(hit.getBlockPos());

        if (be instanceof AbstractAuraBlockEntity auraBe) {
            Optional<IAuraStorage> auraStorageOptional = auraBe.getAuraStorage();
            if (auraStorageOptional.isEmpty()) {
                return InteractionResultHolder.fail(stack);
            }

            IAuraStorage aura = auraStorageOptional.get();
            player.sendSystemMessage(Component.literal("Aura: " + aura.getAura() + "/" + aura.getMaxAura()));

            if (auraBe instanceof BlockEntityAuraExtractor extractor) {
                boolean linked = extractor.targetPos != null;
                player.sendSystemMessage(Component.literal("Linked?: " + linked));
            }
        }

        return InteractionResultHolder.fail(stack);
    }
}
