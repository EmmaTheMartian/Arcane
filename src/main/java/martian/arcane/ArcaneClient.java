package martian.arcane;

import martian.arcane.client.renderers.be.AuraInfuserRenderer;
import martian.arcane.client.gui.AuraometerOverlay;
import martian.arcane.client.renderers.be.ItemPylonRenderer;
import martian.arcane.registry.ArcaneBlockEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = ArcaneMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ArcaneClient {
    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerBelowAll(ArcaneMod.MODID + ".auraometer_overlay", new AuraometerOverlay());
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ArcaneBlockEntities.AURA_INFUSER_BE.get(), AuraInfuserRenderer::new);
        event.registerBlockEntityRenderer(ArcaneBlockEntities.ITEM_PYLON_BE.get(), ItemPylonRenderer::new);
    }
}
