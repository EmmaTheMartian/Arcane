package martian.arcane.common.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public record MagicParticleOptions(int packedColour) implements ParticleOptions {
    public static final MapCodec<MagicParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("packedColour").forGetter(MagicParticleOptions::packedColour)
    ).apply(instance, MagicParticleOptions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MagicParticleOptions> STREAM_CODEC = StreamCodec.of(
            (buf, it) -> buf.writeInt(it.packedColour),
            buf -> new MagicParticleOptions(buf.readInt())
    );

    @Override
    public @NotNull ParticleType<?> getType() {
        return MagicParticleType.INSTANCE;
    }
}
