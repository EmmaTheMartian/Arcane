package martian.arcane.common.item;

import martian.arcane.api.block.BlockHelpers;
import martian.arcane.api.Raycasting;
import martian.arcane.api.block.entity.IAuraometerOutput;
import martian.arcane.api.item.IAuraometer;
import martian.arcane.common.block.connector.BlockEntityAuraConnector;
import martian.arcane.common.block.connector.ConnectorLinkRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.fml.loading.FMLLoader;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemAuraometer extends Item implements IAuraometer {
    public ItemAuraometer() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hit = Raycasting.blockRaycast(player, player.blockInteractionRange(), false);

        // This is a server/client sync debug feature
        if (!FMLLoader.isProduction() && player.isCrouching() && hit != null) {
            if (!level.isClientSide)
                BlockHelpers.sync(Objects.requireNonNull(level.getBlockEntity(hit.getBlockPos())));

            player.sendSystemMessage(Component.literal(level instanceof ServerLevel ? "Server:" : "Client:"));

            List<Component> text = new ArrayList<>();
            BlockEntity be = level.getBlockEntity(hit.getBlockPos());
            if (be instanceof IAuraometerOutput beAo) {
                text = beAo.getText(text, new IAuraometerOutput.Context(ItemStack.EMPTY, player.isCrouching()));
                for (Component c : text)
                    player.sendSystemMessage(c);
            }
        }

        if (!level.isClientSide)
            return InteractionResultHolder.success(stack);

        if (player.isCrouching() && hit == null) {
            ConnectorLinkRenderer.selectedConnectors.clear();
        } else if (hit != null && level.getBlockEntity(hit.getBlockPos()) instanceof BlockEntityAuraConnector connector) {
            ConnectorLinkRenderer.selectedConnectors.add(connector.getBlockPos());
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> text, TooltipFlag flag) {
        super.appendHoverText(stack, context, text, flag);
        text.add(Component.translatable("item.arcane.auraometer.tooltip"));
    }
}
