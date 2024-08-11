package martian.arcane.client.particle;

import martian.arcane.common.particle.MagicParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class MagicParticleProvider implements ParticleProvider<MagicParticleOptions> {
    public final SpriteSet spriteSet;

    public MagicParticleProvider(SpriteSet spriteSet) {
        this.spriteSet = spriteSet;
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Particle createParticle(MagicParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return new MagicParticle(level, x, y, z, spriteSet, options);
    }
}
