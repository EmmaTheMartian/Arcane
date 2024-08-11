package martian.arcane.common.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public class MagicParticleType extends ParticleType<MagicParticleOptions> {
    public static final MagicParticleType INSTANCE = new MagicParticleType(false);

    protected MagicParticleType(boolean overrideLimiter) {
        super(overrideLimiter);
    }

    @Override
    public @NotNull MapCodec<MagicParticleOptions> codec() {
        return MagicParticleOptions.CODEC;
    }

    @Override
    public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, MagicParticleOptions> streamCodec() {
        return MagicParticleOptions.STREAM_CODEC;
    }
}
