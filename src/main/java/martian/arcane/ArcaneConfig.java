package martian.arcane;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@EventBusSubscriber(modid = ArcaneMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ArcaneConfig
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue AURA_NODI_AURA = BUILDER
            .comment("Aura per aura nodi. Existing nodi will not have their maximums affected.")
            .defineInRange("auraNodiAura", 32768, 0, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int auraNodiAura;

    @SuppressWarnings("unused")
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        auraNodiAura = AURA_NODI_AURA.get();
    }
}
