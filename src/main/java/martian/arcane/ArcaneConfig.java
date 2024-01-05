package martian.arcane;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.common.ForgeConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = ArcaneMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArcaneConfig
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue AURA_NODI_AURA = BUILDER
            .comment("Aura per aura nodi. Existing nodi will not have their maximums affected.")
            .defineInRange("auraNodiAura", 32768, 0, Integer.MAX_VALUE);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int auraNodiAura;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        auraNodiAura = AURA_NODI_AURA.get();
    }
}
