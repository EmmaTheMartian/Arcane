package martian.arcane.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

// Behold: code copied from EnchantmentTableBlockEntity and EnchantTableRenderer
// (i.e, credit to the MC devs for this code. All I did was reformat it to match my programming style)
public class BookModelRenderer {
    private static final RandomSource RANDOM = RandomSource.create();

    private final BookModel bookModel;
    private int time;
    private float
            flip, oFlip, flipT, flipA,
            open, oOpen,
            rot, oRot, tRot;

    public BookModelRenderer(BlockEntityRendererProvider.Context context) {
        this.bookModel = new BookModel(context.bakeLayer(ModelLayers.BOOK));
    }

    public void render(float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.75F, 0.5F);
        float f = (float)time + partialTick;
        poseStack.translate(0.0F, 0.1F + Mth.sin(f * 0.1F) * 0.01F, 0.0F);

        float g;
        g = rot - oRot;
        while (g >= 3.1415927F)
            g -= 6.2831855F;

        while (g < -3.1415927F)
            g += 6.2831855F;

        float h = oRot + g * partialTick;
        poseStack.mulPose(Axis.YP.rotation(-h));
        poseStack.mulPose(Axis.ZP.rotationDegrees(80.0F));
        float i = Mth.lerp(partialTick, oFlip, flip);
        float j = Mth.frac(i + 0.25F) * 1.6F - 0.3F;
        float k = Mth.frac(i + 0.75F) * 1.6F - 0.3F;
        float l = Mth.lerp(partialTick, oOpen, open);
        this.bookModel.setupAnim(f, Mth.clamp(j, 0.0F, 1.0F), Mth.clamp(k, 0.0F, 1.0F), l);
        VertexConsumer vertexConsumer = EnchantTableRenderer.BOOK_LOCATION.buffer(buffer, RenderType::entitySolid);
        this.bookModel.render(poseStack, vertexConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    public static void tick(BookModelRenderer renderer, Level level, BlockPos pos) {
        renderer.oOpen = renderer.open;
        renderer.oRot = renderer.rot;
        Player player = level.getNearestPlayer((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, 3.0, false);

        if (player != null) {
            double d = player.getX() - ((double)pos.getX() + 0.5);
            double e = player.getZ() - ((double)pos.getZ() + 0.5);
            renderer.tRot = (float) Mth.atan2(e, d);
            renderer.open += 0.1F;

            if (renderer.open < 0.5F || RANDOM.nextInt(40) == 0) {
                float f = renderer.flipT;
                do renderer.flipT += (float)(RANDOM.nextInt(4) - RANDOM.nextInt(4));
                while(f == renderer.flipT);
            }
        } else {
            renderer.tRot += 0.02F;
            renderer.open -= 0.1F;
        }

        while (renderer.rot >= 3.1415927F)
            renderer.rot -= 6.2831855F;

        while (renderer.rot < -3.1415927F)
            renderer.rot += 6.2831855F;

        while (renderer.tRot >= 3.1415927F)
            renderer.tRot -= 6.2831855F;

        while (renderer.tRot < -3.1415927F)
            renderer.tRot += 6.2831855F;

        float g;
        g = renderer.tRot - renderer.rot;
        while (g >= 3.1415927F)
            g -= 6.2831855F;

        while (g < -3.1415927F)
            g += 6.2831855F;

        renderer.rot += g * 0.4F;
        renderer.open = Mth.clamp(renderer.open, 0.0F, 1.0F);
        ++renderer.time;
        renderer.oFlip = renderer.flip;
        float h = (renderer.flipT - renderer.flip) * 0.4F;
        h = Mth.clamp(h, -0.2F, 0.2F);
        renderer.flipA += (h - renderer.flipA) * 0.9F;
        renderer.flip += renderer.flipA;
    }
}
