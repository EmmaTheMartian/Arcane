package martian.arcane.client;

import martian.arcane.ArcaneMod;
import martian.arcane.client.renderers.be.SpellCircleRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ArcaneModelLayers {
    public static final ModelLayerLocation SPELL_CIRCLE = new ModelLayerLocation(ArcaneMod.id("spell_circle"), "main");

    public static void initLayers(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> consumer) {
        consumer.accept(SPELL_CIRCLE, () -> LayerDefinition.create(SpellCircleRenderer.createMesh(), 128, 192));
    }
}
