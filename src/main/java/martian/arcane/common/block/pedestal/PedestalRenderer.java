package martian.arcane.common.block.pedestal;

import com.klikli_dev.modonomicon.item.ModonomiconItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import martian.arcane.client.renderers.BookModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

public class PedestalRenderer implements BlockEntityRenderer<BlockEntityPedestal> {
    public final BookModelRenderer bookRenderer;

    public PedestalRenderer(BlockEntityRendererProvider.Context context) {
        bookRenderer = new BookModelRenderer(context);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(BlockEntityPedestal infuser, float partialTick, PoseStack ps, MultiBufferSource buffer, int light, int overlay) {
        Level level = infuser.getLevel();
        if (level == null)
            return;

        if (!infuser.getItem().isEmpty()) {
            long time = level.getGameTime();
            float verticalOffset = Mth.sin((time + partialTick) / 10F) / 10F;

            if (infuser.getItem().getItem() instanceof ModonomiconItem) {
                BookModelRenderer.tick(bookRenderer, infuser.getLevel(), infuser.getBlockPos());
                bookRenderer.render(partialTick, ps, buffer, light, overlay);
                return;
            }

            ps.pushPose();
            ps.translate(0.5F, 1F + verticalOffset, 0.5F);
            ps.mulPose(infuser.getBlockState().getValue(BlockPedestal.FACING).getCounterClockWise().getRotation());
            ps.mulPose(Axis.YP.rotationDegrees(90));
            ps.mulPose(Axis.ZP.rotationDegrees(270));

            Minecraft.getInstance().getItemRenderer().renderStatic(
                    infuser.getItem(),
                    ItemDisplayContext.GROUND,
                    light,
                    overlay,
                    ps,
                    buffer,
                    level,
                    (int)infuser.getBlockPos().asLong()
            );

            ps.popPose();
        }
    }
}
