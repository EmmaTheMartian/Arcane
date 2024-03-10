package martian.arcane.client;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ParticleHelper {
    private static final Vector3f MAGIC_PARTICLE_COLOR = new Vector3f(0.7f, 0.2f, 0.9f);

    public static void addMagicParticle(Level level, Vec3 pos, Vec3 vel) {
        level.addParticle(new DustParticleOptions(MAGIC_PARTICLE_COLOR, 1.0f), pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
    }

    public static void addMagicParticle(Level level, Vec3 pos) {
        addMagicParticle(level, pos, Vec3.ZERO);
    }
}
