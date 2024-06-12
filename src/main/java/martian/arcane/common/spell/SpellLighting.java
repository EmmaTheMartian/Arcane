package martian.arcane.common.spell;

import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.spell.CastResult;
import martian.arcane.common.ArcaneContent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;

public class SpellLighting extends AbstractSpell {
    public SpellLighting() {
        super(1);
    }

    @Override
    public int getAuraCost(CastContext context) {
        return 1;
    }

    @Override
    public CastResult cast(CastContext context) {
        if (context.level.isClientSide)
            return CastResult.SUCCESS;

        BlockPos target = context.getTarget();
        if (target == null)
            return CastResult.FAILED;

        if (context instanceof CastContext.WandContext wc && wc.raycast() instanceof BlockHitResult bHit)
            target = bHit.getBlockPos().relative(bHit.getDirection());

        context.level.setBlockAndUpdate(target, ArcaneContent.AURA_TORCH.get().defaultBlockState());

        return CastResult.SUCCESS;
    }
}
