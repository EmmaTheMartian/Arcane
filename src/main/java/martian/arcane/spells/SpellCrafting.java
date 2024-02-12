package martian.arcane.spells;

import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.item.ItemAuraWand;
import martian.arcane.registry.ArcaneBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class SpellCrafting extends AbstractSpell {
    public SpellCrafting() {
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

        level.setBlockAndUpdate(pos, ArcaneBlocks.CONJURED_CRAFTING_TABLE.get().defaultBlockState());
    }
}
