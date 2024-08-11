package martian.arcane.client.particle;

import martian.arcane.api.colour.ColourPalette;
import martian.arcane.api.colour.UnpackedColour;
import martian.arcane.client.ArcaneClient;
import martian.arcane.common.particle.MagicParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class MagicParticle extends TextureSheetParticle {
    private static final RandomSource random = RandomSource.create();

    public final SpriteSet spriteSet;

    public MagicParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, MagicParticleOptions options) {
        super(level, x, y, z, 0, 0, 0);
        this.spriteSet = spriteSet;

        this.lifetime = random.nextInt(16, 32);

        this.rCol = FastColor.ARGB32.red(options.packedColour()) / 255f;
        this.gCol = FastColor.ARGB32.green(options.packedColour()) / 255f;
        this.bCol = FastColor.ARGB32.blue(options.packedColour()) / 255f;
        this.alpha = FastColor.ARGB32.alpha(options.packedColour()) / 255f;

        // From net.minecraft.client.particle.HeartParticle
        this.speedUpWhenYMotionIsBlocked = true;
        this.friction = 0.86F;
        this.xd *= 0.009999999776482582;
        this.yd *= 0.009999999776482582;
        this.zd *= 0.009999999776482582;
        this.yd += 0.05;
        this.hasPhysics = false;

        setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        setSpriteFromAge(spriteSet);
        super.tick();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static void spawn(Level level, Vec3 pos, float heightMod, ColourPalette palette) {
        final double step = Math.PI / 18;
        UnpackedColour colour;
        for (double r = 0; r < 2 * Math.PI; r += step) {
            colour = palette.getRandom().modulateAlpha(30);
            level.addParticle(
                    new MagicParticleOptions(colour.pack()),
                    pos.x + Math.sin(r),
                    pos.y + (ArcaneClient.RANDOM.nextDouble() * heightMod),
                    pos.z + Math.cos(r),
                    0, 0, 0
            );
        }
    }
}
