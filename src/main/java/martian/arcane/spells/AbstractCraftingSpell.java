package martian.arcane.spells;

import martian.arcane.api.recipe.SimpleContainer;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.block.entity.BlockEntityPedestal;
import martian.arcane.item.ItemAuraWand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Optional;

public abstract class AbstractCraftingSpell<T extends Recipe<?>> extends AbstractSpell {
    public AbstractCraftingSpell(int minLevel) {
        super(minLevel);
    }

    @Override
    public void cast(ItemAuraWand wand, ItemStack stack, Level level, Player caster, InteractionHand castHand, HitResult hit) {
        if (level.isClientSide())
            return;

        if (hit.getType() != HitResult.Type.BLOCK)
            return;

        BlockHitResult bHit = (BlockHitResult)hit;

        if (level.getBlockEntity(bHit.getBlockPos()) instanceof BlockEntityPedestal pedestal) {
            // Target pedestal with item
            SimpleContainer container = new SimpleContainer(pedestal.inv);
            Optional<T> recipe = getRecipeFor(level, container);
            recipe.ifPresent(r -> pedestal.setItem(r.getResultItem(level.registryAccess())));
            BlockState state = level.getBlockState(bHit.getBlockPos());
            level.sendBlockUpdated(bHit.getBlockPos(), state, state, 2);
        } else {
            // Target block directly
            Item item = level.getBlockState(bHit.getBlockPos()).getBlock().asItem();
            SimpleContainer container = new SimpleContainer(new ItemStackHandler());
            container.setItem(new ItemStack(item));
            Optional<T> recipe = getRecipeFor(level, container);
            recipe.ifPresent(r -> {
                BlockPos pos = bHit.getBlockPos();
                if (r.getResultItem(level.registryAccess()).getItem() instanceof BlockItem bi) {
                    level.setBlockAndUpdate(pos, bi.getBlock().defaultBlockState());
                } else {
                    level.removeBlock(pos, false);
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), r.getResultItem(level.registryAccess()));
                }
            });
        }
    }

    protected abstract Optional<T> getRecipeFor(Level level, SimpleContainer container);
}
