package martian.arcane.integration.emi;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;
import martian.arcane.api.recipe.BlockIngredient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.List;

public class BlockWidget extends Widget {
    protected final int x, y;
    protected final BlockIngredient ingredient;
    protected final BlockState state;
    protected final BakedModel model;

    public BlockWidget(int x, int y, BlockIngredient ingredient) {
        this.x = x;
        this.y = y;
        this.ingredient = ingredient;
        assert ingredient.block().isPresent();
        this.state = ingredient.block().get().defaultBlockState();
        this.model = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(state);
    }

    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, 18, 18);
    }

    private final BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();

    @Override
    public void render(GuiGraphics draw, int mouseX, int mouseY, float delta) {
        PoseStack stack = draw.pose();
        stack.pushPose();
        PoseStack.Pose last = stack.last();
        stack.translate(x + 3, y + 4, 150);
        stack.scale(8.5f, -8.5f, 8.5f);
        stack.translate(0, -1, 0);
        stack.mulPose(Axis.XP.rotationDegrees(30));
        stack.mulPose(Axis.YP.rotationDegrees(45));
        blockRenderer.getModelRenderer().renderModel(last, draw.bufferSource().getBuffer(RenderType.solid()), state, model, 1, 1, 1, LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.solid());
        stack.popPose();

        var bounds = getBounds();
        if (bounds.contains(mouseX, mouseY)) {
            stack.pushPose();
            stack.translate(0, 0, 200);
            RenderSystem.colorMask(true, true, true, false);
            draw.fill(bounds.x() + 1, bounds.y() + 1, bounds.x() + bounds.width() - 2, bounds.y() + bounds.height() - 2, -2130706433);
            RenderSystem.colorMask(true, true, true, true);
            stack.popPose();
        }
    }

    @Override
    public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
        return List.of(EmiTooltipComponents.of(state.getBlock().getName()));
    }
}
