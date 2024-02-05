package martian.arcane.client.renderers.be;

import com.mojang.blaze3d.vertex.PoseStack;
import martian.arcane.block.entity.BlockEntityAuraInfuser;
import martian.arcane.block.entity.BlockEntityPedestal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.ParametersAreNonnullByDefault;

public class PedestalRenderer implements BlockEntityRenderer<BlockEntityPedestal> {
    public PedestalRenderer(BlockEntityRendererProvider.Context ignored) {
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(BlockEntityPedestal infuser, float partialTick, PoseStack ps, MultiBufferSource buffer, int light, int overlay) {
        Level level = infuser.getLevel();
        if (level == null)
            return;

        long time = level.getGameTime();

        ps.pushPose();
        float verticalOffset = Mth.sin((time + partialTick) / 10F) / 10F;
        ps.translate(0.5F, 1F + verticalOffset, 0.5F);

        if (!infuser.getItem().isEmpty()) {
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
        }

        ps.popPose();
    }
}