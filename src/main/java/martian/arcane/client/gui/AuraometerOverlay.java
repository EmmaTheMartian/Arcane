package martian.arcane.client.gui;

import martian.arcane.api.Raycasting;
import martian.arcane.api.block.entity.IAuraometerOutput;
import martian.arcane.common.registry.ArcaneItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AuraometerOverlay implements LayeredDraw.Layer {
    private final Minecraft minecraft = Minecraft.getInstance();

    public static boolean isHoldingAuraometer(Player player) {
        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();
        return main.is(ArcaneItems.AURAOMETER.get()) || off.is(ArcaneItems.AURAOMETER.get());
    }

    @Override
    public void render(@NotNull GuiGraphics gui, float partialTicks) {
        final Player player = minecraft.player;
        if (player == null)
            return;

        boolean holdingAuraometer = isHoldingAuraometer(player);

        if (holdingAuraometer) {
            final Level level = minecraft.level;
            assert level != null;

            int x = gui.guiWidth() / 2 + 4;
            int y = gui.guiHeight() / 2 + 4;
            List<Component> text = new ArrayList<>();

            BlockHitResult hit = Raycasting.blockRaycast(player, player.blockInteractionRange(), false);
            if (hit == null)
                return;

            BlockState block = level.getBlockState(hit.getBlockPos());
            if (!block.hasBlockEntity())
                return;

            BlockEntity be = level.getBlockEntity(hit.getBlockPos());
            if (be instanceof IAuraometerOutput beAo) {
                text = beAo.getText(text, player.isCrouching());
                for (Component c : text) {
                    gui.drawString(Minecraft.getInstance().font, c, x, y, 0xFFFFFF, true);
                    y += Minecraft.getInstance().font.lineHeight + 2;
                }
            }
        }
    }
}
