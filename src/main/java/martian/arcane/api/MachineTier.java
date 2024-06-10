package martian.arcane.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneStaticConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record MachineTier(ResourceLocation id, int auraLoss, int ioRate, float maxAuraMultiplier) {
    public int getMaxAuraForMachine(int machineAura) {
        return Math.round(machineAura * maxAuraMultiplier);
    }

    public String getTranslationKey() {
        return "arcane.machine_tier." + id.getNamespace() + "." + id.getPath() + ".name";
    }

    public Component getName() {
        return Component.translatable(getTranslationKey());
    }

    public static final Codec<MachineTier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(MachineTier::id),
            Codec.INT.fieldOf("auraLoss").forGetter(MachineTier::auraLoss),
            Codec.INT.fieldOf("ioRate").forGetter(MachineTier::ioRate),
            Codec.FLOAT.fieldOf("maxAuraMultiplier").forGetter(MachineTier::maxAuraMultiplier)
    ).apply(instance, MachineTier::new));

    public static final MachineTier COPPER = new MachineTier(ArcaneMod.id("copper"), ArcaneStaticConfig.AuraLoss.COPPER_TIER, ArcaneStaticConfig.Rates.COPPER_AURA_CONNECTOR_RATE, 1);
    public static final MachineTier LARIMAR = new MachineTier(ArcaneMod.id("larimar"), ArcaneStaticConfig.AuraLoss.LARIMAR_TIER, ArcaneStaticConfig.Rates.LARIMAR_AURA_CONNECTOR_RATE, 2);
    public static final MachineTier AURACHALCUM = new MachineTier(ArcaneMod.id("aurachalcum"), ArcaneStaticConfig.AuraLoss.AURACHALCUM_TIER, ArcaneStaticConfig.Rates.AURACHALCUM_AURA_CONNECTOR_RATE, 4);
}
