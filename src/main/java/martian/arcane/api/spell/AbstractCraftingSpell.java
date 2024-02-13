package martian.arcane.api.spell;

import martian.arcane.api.recipe.SimpleContainer;
import martian.arcane.block.entity.BlockEntityPedestal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Optional;

public abstract class AbstractCraftingSpell<T extends Recipe<?>> extends AbstractSpell {
    public AbstractCraftingSpell(int minLevel) {
        super(minLevel);
    }

    private void targetBlock(Level level, BlockPos pos) {
        Item item = level.getBlockState(pos).getBlock().asItem();
        SimpleContainer container = new SimpleContainer(new ItemStackHandler());
        container.setItem(new ItemStack(item));
        Optional<T> recipe = getRecipeFor(level, container);
        recipe.ifPresent(r -> {
            if (r.getResultItem(level.registryAccess()).getItem() instanceof BlockItem bi) {
                level.setBlockAndUpdate(pos, bi.getBlock().defaultBlockState());
            } else {
                level.removeBlock(pos, false);
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), r.getResultItem(level.registryAccess()));
            }
        });
    }

    private void targetPedestal(Level level, BlockPos pos, BlockEntityPedestal pedestal) {
        SimpleContainer container = new SimpleContainer(pedestal.inv);
        Optional<T> recipe = getRecipeFor(level, container);
        recipe.ifPresent(r -> pedestal.setItem(r.getResultItem(level.registryAccess())));
        BlockState state = level.getBlockState(pos);
        level.sendBlockUpdated(pos, state, state, 2);
    }

    @Override
    public void cast(CastContext c) {
        if (c.level.isClientSide())
            return;

        BlockPos target = c.getTarget();
        if (target == null)
            return;

        if (c.level.getBlockEntity(target) instanceof BlockEntityPedestal pedestal)
            targetPedestal(c.level, target, pedestal);
        else
            targetBlock(c.level, target);
    }

    protected abstract Optional<T> getRecipeFor(Level level, SimpleContainer container);
}
