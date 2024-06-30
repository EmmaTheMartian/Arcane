package martian.arcane.common.spell;

import martian.arcane.api.spell.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class SimplePlacementSpell extends AbstractSpell {
    private final Function<CastContext, @Nullable BlockState> block;

    public SimplePlacementSpell(Function<CastContext, @Nullable BlockState> block) {
        this.block = block;
    }

    @Override
    public CastResult cast(CastContext c) {
        if (c.level.isClientSide)
            return CastResult.SUCCESS;

        if (c.target.type() == CastTarget.Type.BLOCK) {
            BlockPos pos = ((BlockPos) c.target.value());

            if (c instanceof CastContext.WandContext wc) {
                HitResult hit = wc.raycast();
                if (hit.getType() != HitResult.Type.BLOCK)
                    return CastResult.FAILED;

                BlockHitResult bHit = (BlockHitResult) hit;
                pos = bHit.getBlockPos().relative(bHit.getDirection());
            }

            if (!c.level.isInWorldBounds(pos) || !c.level.getBlockState(pos).canBeReplaced())
                return CastResult.FAILED;

            BlockState state = block.apply(c);
            if (state != null)
                c.level.setBlockAndUpdate(pos, state);

            return CastResult.SUCCESS;
        }

        return CastResult.FAILED;
    }

    public static SimplePlacementSpell of(ResourceLocation id, int auraCost, int cooldown, int minLevel, Function<CastContext, @Nullable BlockState> block) {
        return new SimplePlacementSpell(block) {
            private final SpellConfig config = SpellConfig.basicConfig(id, auraCost, cooldown, minLevel).build();

            @Override
            protected SpellConfig getConfig() {
                return config;
            }
        };
    }
}
