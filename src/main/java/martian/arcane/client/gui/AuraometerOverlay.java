package martian.arcane.client.gui;

import martian.arcane.api.Raycasting;
import martian.arcane.api.block.entity.IAuraometerOutput;
import martian.arcane.api.item.IAuraometer;
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
    private static final Minecraft minecraft = Minecraft.getInstance();

    public static boolean isHoldingAuraometer(Player player) {
        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();
        return main.getItem() instanceof IAuraometer || off.getItem() instanceof IAuraometer;
    }

    public static boolean hasAuraometer(Player player) {
        return player.getInventory().hasAnyMatching(it -> it.getItem() instanceof IAuraometer);
    }

    @Override
    public void render(@NotNull GuiGraphics gui, float partialTicks) {
        final Player player = minecraft.player;
        if (player == null)
            return;

        if (isHoldingAuraometer(player)) {
            final Level level = minecraft.level;
            assert level != null;

            final int x = gui.guiWidth() / 2 + 4;
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
                text = beAo.getText(text, new IAuraometerOutput.Context(ItemStack.EMPTY, player.isCrouching()));
                for (Component c : text) {
                    gui.drawString(minecraft.font, c, x, y, 0xFFFFFF, true);
                    y += minecraft.font.lineHeight + 2;
                }
            }
        }

        if (hasAuraometer(player)) {
            List<Component> text = new ArrayList<>();

            ItemStack main = player.getMainHandItem(), off = player.getOffhandItem();

            if (main.getItem() instanceof IAuraometerOutput iAo)
                iAo.getText(text, new IAuraometerOutput.Context(main, player.isCrouching()));
            else if (off.getItem() instanceof IAuraometerOutput iAo)
                iAo.getText(text, new IAuraometerOutput.Context(off, player.isCrouching()));

            if (!text.isEmpty()) {
                final int x = 4;
                final int lineHeight = minecraft.font.lineHeight + 2;
                int y = gui.guiHeight() / 2 + 4 - (lineHeight * text.size() / 2);
                for (Component c : text) {
                    gui.drawString(minecraft.font, c, x, y, 0xFFFFFF, true);
                    y += lineHeight;
                }
            }
        }
    }
}
