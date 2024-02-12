package martian.arcane.spells;

import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.item.ItemAuraWand;
import martian.arcane.registry.ArcaneBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class SpellBuilding extends AbstractSpell {
    public SpellBuilding() {
        super(1);
    }

    @Override
    public int getAuraCost(ItemAuraWand wand, ItemStack stack) {
        return 1;
    }

    @Override
    public void cast(ItemAuraWand wand, ItemStack stack, Level level, Player caster, InteractionHand castHand, HitResult hit) {
        if (level.isClientSide())
            return;

        if (hit.getType() != HitResult.Type.BLOCK)
            return;

        BlockHitResult bHit = (BlockHitResult)hit;
        BlockPos pos = bHit.getBlockPos().relative(bHit.getDirection());
        if (!level.isInWorldBounds(pos))
            return;

        BlockState state = ArcaneBlocks.CONJURED_BLOCK.get().defaultBlockState();
        ItemStack offStack = caster.getOffhandItem();
        if (offStack.getItem() instanceof BlockItem bi) {
            state = bi.getBlock().defaultBlockState();
            if (!caster.isCreative())
                offStack.shrink(1);
        }

        level.setBlockAndUpdate(pos, state);
    }
}
