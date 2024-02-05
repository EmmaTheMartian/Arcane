package martian.arcane.spells;

import martian.arcane.api.recipe.SimpleContainer;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.block.entity.BlockEntityPedestal;
import martian.arcane.item.ItemAuraWand;
import martian.arcane.recipe.RecipeHammering;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Optional;

public class SpellHammering extends AbstractSpell {
    public SpellHammering() {
        super(2);
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
        if (level.getBlockEntity(bHit.getBlockPos()) instanceof BlockEntityPedestal pedestal) {
            SimpleContainer container = new SimpleContainer(pedestal.inv);
            Optional<RecipeHammering> recipe = RecipeHammering.getRecipeFor(level, container);
            recipe.ifPresent(r -> pedestal.setItem(r.getResultItem()));
            BlockState state = level.getBlockState(bHit.getBlockPos());
            level.sendBlockUpdated(bHit.getBlockPos(), state, state, 2);
        }
    }
}
