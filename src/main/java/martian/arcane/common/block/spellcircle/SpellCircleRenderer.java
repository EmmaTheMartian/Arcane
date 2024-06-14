package martian.arcane.common.block.spellcircle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import martian.arcane.client.ArcaneClient;
import martian.arcane.ArcaneMod;
import martian.arcane.client.ArcaneModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class SpellCircleRenderer implements BlockEntityRenderer<BlockEntitySpellCircle> {
    private static final RenderType LAYER = RenderType.entityCutoutNoCull(ArcaneMod.id("textures/block/machines/spell_circle/basic.png"));
    private final ModelPart circleBot, circleMid, circleTop;

    public SpellCircleRenderer(BlockEntityRendererProvider.Context ctx) {
        ModelPart root = ctx.bakeLayer(ArcaneModelLayers.SPELL_CIRCLE);
        circleTop = root.getChild("top");
        circleMid = root.getChild("mid");
        circleBot = root.getChild("bot");
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(BlockEntitySpellCircle be, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        VertexConsumer consumer = buffer.getBuffer(LAYER);
        float tick = be.getActive() ? ArcaneClient.clientTicks + partialTick : 0;
        int light = be.getActive() ? LightTexture.FULL_BRIGHT : packedLight;

        stack.pushPose();

        var facing = be.getBlockState().getValue(BlockSpellCircle.FACING);
        var rotation = facing.getRotation();
        stack.mulPose(rotation);

        switch (facing) {
            case UP -> stack.translate( 0.5,  0.9,  0.5);
            case DOWN -> stack.translate( 0.5, -0.1, -0.5);
            case NORTH -> stack.translate(-0.5, -0.1, -0.5);
            case SOUTH -> stack.translate( 0.5,  0.9, -0.5);
            case WEST -> stack.translate( 0.5, -0.1, -0.5);
            case EAST -> stack.translate(-0.5,  0.9, -0.5);
        }

        stack.pushPose();
        stack.mulPose(Axis.YP.rotation(tick / 40f));
        circleBot.render(stack, consumer, light, packedOverlay);
        stack.popPose();

        stack.pushPose();
        stack.translate(0, 0.01, 0);
        stack.mulPose(Axis.YP.rotation(tick / -40f));
        circleMid.render(stack, consumer, light, packedOverlay);
        stack.popPose();

        stack.pushPose();
        stack.translate(0, 0.02, 0);
        stack.mulPose(Axis.YP.rotation(tick / 20f));
        circleTop.render(stack, consumer, light, packedOverlay);
        stack.popPose();

        stack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(@NotNull BlockEntitySpellCircle be) {
        return true;
    }

    public static MeshDefinition createMesh() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "top",
                CubeListBuilder.create().addBox(-16, 0, -16, 32, 0.5f, 32),
                PartPose.ZERO
        );

        root.addOrReplaceChild(
                "mid",
                CubeListBuilder.create().texOffs(0, 64).addBox(-16, 0, -16, 32, 0.5f, 32),
                PartPose.ZERO
        );

        root.addOrReplaceChild(
                "bot",
                CubeListBuilder.create().texOffs(0, 128).addBox(-16, 0, -16, 32, 0.5f, 32),
                PartPose.ZERO
        );

        return mesh;
    }
}
