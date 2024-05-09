package martian.arcane.common.block.aura.infuser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import martian.arcane.client.ArcaneClient;
import martian.arcane.client.ParticleHelper;
import martian.arcane.common.block.pedestal.BlockPedestal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;

public class AuraInfuserRenderer implements BlockEntityRenderer<BlockEntityAuraInfuser> {
    public AuraInfuserRenderer(BlockEntityRendererProvider.Context ignored) {
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(BlockEntityAuraInfuser infuser, float partialTick, PoseStack ps, MultiBufferSource buffer, int light, int overlay) {
        Level level = infuser.getLevel();
        if (level == null)
            return;

        if (!infuser.getItem().isEmpty()) {
            long time = level.getGameTime();
            float verticalOffset = Mth.sin((time + partialTick) / 10F) / 10F;

            ps.pushPose();
            ps.translate(0.5F, 1F + verticalOffset, 0.5F);
            ps.mulPose(infuser.getBlockState().getValue(BlockPedestal.FACING).getCounterClockWise().getRotation());
            ps.mulPose(Axis.YP.rotationDegrees(90));
            ps.mulPose(Axis.ZP.rotationDegrees(270));

            // According to Ginger, this line will cause problems
            if (infuser.isActive && ArcaneClient.isGameActive() && ArcaneClient.clientTicks % 8 == 0) {
                ParticleHelper.addMagicParticle(level, infuser.getBlockPos().getCenter().add(0, 0.3D, 0), new Vec3(0, 0.7D, 0));
            }

            Minecraft.getInstance().getItemRenderer().renderStatic(
                    infuser.getItem(),
                    ItemDisplayContext.GROUND,
                    light,
                    overlay,
                    ps,
                    buffer,
                    level,
                    (int) infuser.getBlockPos().asLong()
            );

            ps.popPose();
        }
    }
}
