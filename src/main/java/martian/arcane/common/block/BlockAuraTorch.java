package martian.arcane.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BlockAuraTorch extends BaseTorchBlock {
    public static final MapCodec<BlockAuraTorch> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DustParticleOptions.CODEC.forGetter(it -> it.particleOptions),
            propertiesCodec()
    ).apply(instance, BlockAuraTorch::new));

    public final DustParticleOptions particleOptions;

    public @NotNull MapCodec<? extends BlockAuraTorch> codec() {
        return CODEC;
    }

    public BlockAuraTorch(DustParticleOptions particleOptions, BlockBehaviour.Properties properties) {
        super(properties);
        this.particleOptions = particleOptions;
    }

    public void animateTick(@NotNull BlockState state, Level level, BlockPos pos, @NotNull RandomSource random) {
        double d = (double)pos.getX() + 0.5;
        double e = (double)pos.getY() + 0.7;
        double f = (double)pos.getZ() + 0.5;
        level.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
        level.addParticle(particleOptions, d, e, f, 0.0, 0.0, 0.0);
    }
}
