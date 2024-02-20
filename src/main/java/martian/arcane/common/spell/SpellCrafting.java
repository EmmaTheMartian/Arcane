package martian.arcane.common.spell;

import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.spell.ICastingSource;
import martian.arcane.common.registry.ArcaneBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class SpellCrafting extends AbstractSpell {
    public SpellCrafting() {
        super(1);
    }

    @Override
    public int getAuraCost(int level) {
        return 1;
    }

    @Override
    public void cast(CastContext c) {
        if (c.level.isClientSide)
            return;

        BlockPos pos = null;

        if (c.source == ICastingSource.Type.WAND) {
            CastContext.WandContext wandContext = (CastContext.WandContext)c;
            HitResult hit = wandContext.raycast();
            if (hit.getType() != HitResult.Type.BLOCK)
                return;

            BlockHitResult bHit = (BlockHitResult)hit;
            pos = bHit.getBlockPos().relative(bHit.getDirection());
            if (!c.level.isInWorldBounds(pos))
                return;
        } else if (c.source == ICastingSource.Type.SPELL_CIRCLE) {
            CastContext.SpellCircleContext spellCircleContext = (CastContext.SpellCircleContext)c;
            pos = spellCircleContext.getTarget();
        }

        if (pos != null && c.level.isInWorldBounds(pos))
            c.level.setBlockAndUpdate(pos, ArcaneBlocks.CONJURED_CRAFTING_TABLE.get().defaultBlockState());
    }
}
