package martian.arcane.common.spell;

import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.spell.CastResult;
import martian.arcane.api.spell.SpellConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.function.Function;

public abstract class SimplePlacementSpell extends AbstractSpell {
    private final Function<CastContext, BlockState> block;

    public SimplePlacementSpell(Function<CastContext, BlockState> block) {
        this.block = block;
    }

    @Override
    public CastResult cast(CastContext c) {
        if (c.level.isClientSide)
            return CastResult.SUCCESS;

        BlockPos pos = c.getTarget();
        if (pos == null)
            return CastResult.FAILED;

        if (c instanceof CastContext.WandContext wc) {
            HitResult hit = wc.raycast();
            if (hit.getType() != HitResult.Type.BLOCK)
                return CastResult.FAILED;

            BlockHitResult bHit = (BlockHitResult)hit;
            pos = bHit.getBlockPos().relative(bHit.getDirection());
        }

        if (!c.level.isInWorldBounds(pos))
            return CastResult.FAILED;

        c.level.setBlockAndUpdate(pos, block.apply(c));
        return CastResult.SUCCESS;
    }

    public static SimplePlacementSpell of(ResourceLocation id, int auraCost, int cooldown, int minLevel, Function<CastContext, BlockState> block) {
        return new SimplePlacementSpell(block) {
            private final SpellConfig config = SpellConfig.basicConfig(id, auraCost, cooldown, minLevel).build();

            @Override
            protected SpellConfig getConfig() {
                return config;
            }
        };
    }
}
