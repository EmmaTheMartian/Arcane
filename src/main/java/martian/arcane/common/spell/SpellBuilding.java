package martian.arcane.common.spell;

import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.spell.ICastingSource;
import martian.arcane.common.registry.ArcaneBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class SpellBuilding extends AbstractSpell {
    public SpellBuilding() {
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

        BlockState state = ArcaneBlocks.CONJURED_BLOCK.get().defaultBlockState();
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

            ItemStack offStack = wandContext.caster.getMainHandItem() == ((CastContext.WandContext) c).castingStack
                    ? wandContext.caster.getOffhandItem()
                    : wandContext.caster.getMainHandItem();

            if (offStack.getItem() instanceof BlockItem bi) {
                state = bi.getBlock().defaultBlockState();
                if (!wandContext.caster.isCreative())
                    offStack.shrink(1);
            }
        } else if (c.source == ICastingSource.Type.SPELL_CIRCLE) {
            CastContext.SpellCircleContext spellCircleContext = (CastContext.SpellCircleContext)c;
            pos = spellCircleContext.getTarget();
        }

        if (pos != null && c.level.getBlockState(pos).canBeReplaced())
            c.level.setBlockAndUpdate(pos, state);
    }
}
