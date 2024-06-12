package martian.arcane.client;

import martian.arcane.ArcaneMod;
import martian.arcane.client.gui.AuraometerOverlay;
import martian.arcane.common.block.connector.ConnectorLinkRenderer;
import martian.arcane.common.block.infuser.AuraInfuserRenderer;
import martian.arcane.common.block.pedestal.PedestalRenderer;
import martian.arcane.common.block.spellcircle.SpellCircleRenderer;
import martian.arcane.common.registry.ArcaneBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;

@SuppressWarnings("unused")
public class ArcaneClient {
    public static int clientTicks = 0;

    public static void setup(IEventBus modBus) {
        modBus.addListener(ArcaneClient::registerGuiOverlays);
        modBus.addListener(ArcaneClient::registerBlockEntityRenderers);
        modBus.addListener(ArcaneClient::registerEntityLayers);
        modBus.addListener(ArcaneClient::registerKeybindings);

        NeoForge.EVENT_BUS.register(ArcaneClient.class);
    }

    @SubscribeEvent
    public static void onTick(ClientTickEvent.Post event) {
        if (!isGameActive())
            return;

        clientTicks++;

        ConnectorLinkRenderer.tick();
    }

    public static void registerGuiOverlays(RegisterGuiLayersEvent event) {
        event.registerBelowAll(ArcaneMod.id("auraometer_overlay"), new AuraometerOverlay());
    }

    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ArcaneBlockEntities.AURA_INFUSER.get(), AuraInfuserRenderer::new);
        event.registerBlockEntityRenderer(ArcaneBlockEntities.PEDESTAL.get(), PedestalRenderer::new);
        event.registerBlockEntityRenderer(ArcaneBlockEntities.SPELL_CIRCLE.get(), SpellCircleRenderer::new);
    }

    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ArcaneModelLayers.SPELL_CIRCLE, () -> LayerDefinition.create(SpellCircleRenderer.createMesh(), 128, 192));
    }

    public static void registerKeybindings(RegisterKeyMappingsEvent event) {
        event.register(ArcaneKeybindings.OPEN_ENDERPACK.get());
        event.register(ArcaneKeybindings.WANDBOOK_NEXT_SPELL.get());
        event.register(ArcaneKeybindings.WANDBOOK_PREV_SPELL.get());
    }

    public static boolean isGameActive() {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null) && !Minecraft.getInstance().isPaused();
    }
}
