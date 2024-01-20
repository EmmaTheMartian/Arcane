package martian.arcane.client.gui;

import martian.arcane.api.Raycasting;
import martian.arcane.api.block.entity.IAuraometerOutput;
import martian.arcane.registry.ArcaneItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.ArrayList;
import java.util.List;

public class AuraometerOverlay implements IGuiOverlay {
    private final Minecraft minecraft = Minecraft.getInstance();

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partialTicks, int width, int height) {
        final Player player = minecraft.player;
        if (player == null)
            return;

        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();
        boolean holdingAuraometer = main.is(ArcaneItems.AURAOMETER.get()) || off.is(ArcaneItems.AURAOMETER.get());

        if (holdingAuraometer) {
            final Level level = minecraft.level;
            assert level != null;

            int x = width / 2 + 4;
            int y = height / 2 + 4;
            final Font font = forgeGui.getFont();
            List<Component> text = new ArrayList<>();

            BlockHitResult hit = Raycasting.blockRaycast(player, player.getBlockReach(), false);
            if (hit == null)
                return;

            BlockState block = level.getBlockState(hit.getBlockPos());
            if (!block.hasBlockEntity())
                return;

            BlockEntity be = level.getBlockEntity(hit.getBlockPos());
            if (be instanceof IAuraometerOutput beAo) {
                text = beAo.getText(text, player.isCrouching());
                for (Component c : text) {
                    guiGraphics.drawString(font, c, x, y, 0xFFFFFF, true);
                    y += font.lineHeight + 2;
                }
            }
        }
    }
}
