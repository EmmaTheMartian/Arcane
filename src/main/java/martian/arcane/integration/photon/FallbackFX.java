package martian.arcane.integration.photon;

//import com.lowdragmc.photon.client.fx.BlockEffect;
//import com.lowdragmc.photon.client.fx.EntityEffect;
//import com.lowdragmc.photon.client.fx.FX;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class FallbackFX {
//    private final @Nullable FX fx;

//    public FallbackFX(@Nullable FX fx) {
//        this.fx = fx;
//    }

    public void goBlock(Level level, BlockPos pos) {
//        if (fx != null) goBlockPhoton(level, pos);
//        else
        goBlockFallback(level, pos);
    }

    public void goEntity(Level level, Entity entity) {
//        if (this.fx != null) goEntityPhoton(level, entity);
//        else
        goEntityFallback(level, entity);
    }

//    public void goBlockPhoton(Level level, BlockPos pos) {
//        new BlockEffect(fx, level, pos).start();
//    }

    public void goBlockFallback(Level level, BlockPos pos) {}

//    public void goEntityPhoton(Level level, Entity entity) {
//        new EntityEffect(fx, level, entity).start();
//    }

    public void goEntityFallback(Level level, Entity entity) {}
}
