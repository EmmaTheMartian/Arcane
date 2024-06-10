package martian.arcane.common.spell;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.spell.CastResult;
import martian.arcane.common.registry.ArcaneBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class SpellCrafting extends AbstractSpell {
    public SpellCrafting() {
        super(ArcaneStaticConfig.SpellMinLevels.CRAFTING);
    }

    @Override
    public int getAuraCost(CastContext c) {
        return ArcaneStaticConfig.SpellCosts.CRAFTING;
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

        c.level.setBlockAndUpdate(pos, ArcaneBlocks.CONJURED_CRAFTING_TABLE.get().defaultBlockState());
        return CastResult.SUCCESS;
    }
}
