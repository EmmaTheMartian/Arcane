package martian.arcane.spells;

import martian.arcane.api.AOEHelpers;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.item.ItemAuraWand;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class SpellBreaking extends AbstractSpell {
    public SpellBreaking() {
        super(1);
    }

    @Override
    public int getAuraCost(ItemAuraWand wand, ItemStack stack) {
        return 1;
    }

    @Override
    public void cast(ItemAuraWand wand, ItemStack stack, Level level, Player caster, InteractionHand castHand, HitResult hit) {
        if (hit.getType() != HitResult.Type.BLOCK)
            return;

        BlockHitResult bHit = (BlockHitResult)hit;
        if (wand.level == 1)
            tryBreak(level, bHit.getBlockPos());
        else
            AOEHelpers.streamAOE(bHit.getBlockPos(), bHit.getDirection(), getRadius(wand)).forEach(pos -> tryBreak(level, pos));
    }

    private static void tryBreak(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.is(BlockTags.MINEABLE_WITH_PICKAXE) || state.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
            level.destroyBlock(pos, true);
        }
    }

    private static int getRadius(ItemAuraWand wand) {
        return switch (wand.level) {
            case 2 -> 1;
            case 3 -> 3;
            default -> 0;
        };
    }
}
