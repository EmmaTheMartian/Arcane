package martian.arcane.api.spell;

import com.lowdragmc.photon.client.fx.BlockEffect;
import martian.arcane.api.BlockHelpers;
import martian.arcane.api.recipe.SimpleContainer;
import martian.arcane.client.ArcaneFx;
import martian.arcane.common.block.entity.BlockEntityPedestal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
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
                new BlockEffect(ArcaneFx.ON_CAST_GRAVITY, level, pos).start();
            }
        });
    }

    private void targetPedestal(Level level, BlockPos pos, BlockEntityPedestal pedestal) {
        Optional<T> recipe = getRecipeFor(level, new SimpleContainer(pedestal.getItem()));
        recipe.ifPresent(r -> pedestal.setItem(r.getResultItem(level.registryAccess())));
        BlockHelpers.sync(level, pos);
        new BlockEffect(ArcaneFx.ON_CAST_GRAVITY, level, pos.above()).start();
    }

    @Override
    public void cast(CastContext c) {
        if (c.level.isClientSide)
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
