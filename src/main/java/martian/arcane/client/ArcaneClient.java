package martian.arcane.client;

import martian.arcane.ArcaneMod;
import martian.arcane.client.gui.AuraometerOverlay;
import martian.arcane.client.model.DynamicWandModel;
import martian.arcane.client.particle.MagicParticleProvider;
import martian.arcane.common.ArcaneContent;
import martian.arcane.common.block.connector.ConnectorLinkRenderer;
import martian.arcane.common.block.infuser.AuraInfuserRenderer;
import martian.arcane.common.block.pedestal.PedestalRenderer;
import martian.arcane.common.block.spellcircle.SpellCircleRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.util.RandomSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;

@SuppressWarnings("unused")
public class ArcaneClient {
    public static final RandomSource RANDOM = RandomSource.create();

    public static int clientTicks = 0;

    public static void setup(IEventBus modBus) {
        modBus.addListener(ArcaneClient::registerGuiOverlays);
        modBus.addListener(ArcaneClient::registerBlockEntityRenderers);
        modBus.addListener(ArcaneClient::registerEntityLayers);
        modBus.addListener(ArcaneClient::registerKeybindings);
        modBus.addListener(ArcaneClient::registerModelLoaders);
        modBus.addListener(ArcaneClient::registerParticleProviders);

        NeoForge.EVENT_BUS.register(ArcaneClient.class);
    }

    @SubscribeEvent
    static void onTick(ClientTickEvent.Post event) {
        if (!isGameActive())
            return;

        clientTicks++;

        ConnectorLinkRenderer.tick();
    }

    static void registerGuiOverlays(RegisterGuiLayersEvent event) {
        event.registerBelowAll(ArcaneMod.id("auraometer_overlay"), new AuraometerOverlay());
    }

    static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ArcaneContent.BE_AURA_INFUSER.tile().get(), AuraInfuserRenderer::new);
        event.registerBlockEntityRenderer(ArcaneContent.BE_PEDESTAL.tile().get(), PedestalRenderer::new);
        event.registerBlockEntityRenderer(ArcaneContent.BE_SPELL_CIRCLE.tile().get(), SpellCircleRenderer::new);
    }

    static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ArcaneModelLayers.SPELL_CIRCLE, () -> LayerDefinition.create(SpellCircleRenderer.createMesh(), 128, 192));
    }

    static void registerKeybindings(RegisterKeyMappingsEvent event) {
        event.register(ArcaneKeybindings.OPEN_ENDERPACK.get());
        event.register(ArcaneKeybindings.WANDBOOK_NEXT_SPELL.get());
        event.register(ArcaneKeybindings.WANDBOOK_PREV_SPELL.get());
    }

    static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register(ArcaneMod.id("wand"), DynamicWandModel.Loader.INSTANCE);
    }

    static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ArcaneContent.PARTICLE_TYPE_MAGIC.get(), MagicParticleProvider::new);
    }

    public static boolean isGameActive() {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null) && !Minecraft.getInstance().isPaused();
    }
}
