package martian.arcane.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import martian.arcane.ArcaneConfig;
import martian.arcane.ArcaneMod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.function.Supplier;

public record MachineTier(ResourceLocation id, int auraLoss, int ioRate, double maxAuraMultiplier) {
    public int getMaxAuraForMachine(int machineAura) {
        return (int) Math.round(machineAura * maxAuraMultiplier);
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
            Codec.DOUBLE.fieldOf("maxAuraMultiplier").forGetter(MachineTier::maxAuraMultiplier)
    ).apply(instance, MachineTier::new));

    public static final Lazy<MachineTier> COPPER = Lazy.of(() -> new MachineTier(ArcaneMod.id("copper"), ArcaneConfig.copperTierAuraLoss, ArcaneConfig.copperTierConnectorRate, ArcaneConfig.copperTierMaxAuraMultiplier));
    public static final Lazy<MachineTier> LARIMAR = Lazy.of(() -> new MachineTier(ArcaneMod.id("larimar"), ArcaneConfig.larimarTierAuraLoss, ArcaneConfig.larimarTierConnectorRate, ArcaneConfig.larimarTierMaxAuraMultiplier));
    public static final Lazy<MachineTier> AURACHALCUM = Lazy.of(() -> new MachineTier(ArcaneMod.id("aurachalcum"), ArcaneConfig.aurachalcumTierAuraLoss, ArcaneConfig.aurachalcumTierConnectorRate, ArcaneConfig.aurachalcumTierMaxAuraMultiplier));
}
