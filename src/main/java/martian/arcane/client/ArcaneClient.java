package martian.arcane.client;

import martian.arcane.ArcaneMod;
import martian.arcane.client.gui.AuraometerOverlay;
import martian.arcane.client.renderers.ExtractorLinkingRenderer;
import martian.arcane.client.renderers.be.AuraInfuserRenderer;
import martian.arcane.client.renderers.be.PedestalRenderer;
import martian.arcane.client.renderers.be.SpellCircleRenderer;
import martian.arcane.common.registry.ArcaneBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = ArcaneMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ArcaneClient {
    public static int clientTicks = 0;

    public static void setup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.addListener(ArcaneClient::onTick);
    }

    public static void onTick(TickEvent.ClientTickEvent event) {
        if (!isGameActive())
            return;

        clientTicks++;

        ExtractorLinkingRenderer.tick();
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerBelowAll(ArcaneMod.MODID + ".auraometer_overlay", new AuraometerOverlay());
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ArcaneBlockEntities.AURA_INFUSER.get(), AuraInfuserRenderer::new);
        event.registerBlockEntityRenderer(ArcaneBlockEntities.PEDESTAL.get(), PedestalRenderer::new);
        event.registerBlockEntityRenderer(ArcaneBlockEntities.SPELL_CIRCLE.get(), SpellCircleRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ArcaneModelLayers.initLayers(event::registerLayerDefinition);
    }

    @SubscribeEvent
    public static void registerKeybindings(RegisterKeyMappingsEvent event) {
        event.register(ArcaneKeybindings.OPEN_ENDERPACK.get());
    }

    public static boolean isGameActive() {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null) && !Minecraft.getInstance().isPaused();
    }
}
