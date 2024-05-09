package martian.arcane.api.spell;

import martian.arcane.api.block.BlockHelpers;
import martian.arcane.api.item.ItemHelpers;
import martian.arcane.api.recipe.ISpellRecipe;
import martian.arcane.api.recipe.RecipeOutput;
import martian.arcane.api.recipe.SimpleContainer;
import martian.arcane.common.block.pedestal.BlockEntityPedestal;
import martian.arcane.integration.photon.ArcaneFx;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractCraftingSpell<T extends ISpellRecipe> extends AbstractSpell {
    public final int costPerCraft;

    public AbstractCraftingSpell(int minLevel, int costPerCraft) {
        super(minLevel);
        this.costPerCraft = costPerCraft;
    }

    @Override
    public CastResult cast(CastContext c) {
        if (c.level.isClientSide)
            return CastResult.PASS;

        BlockPos pos = c.getTarget();
        if (pos == null)
            return CastResult.FAILED;

        AtomicBoolean didCraft = new AtomicBoolean(false);

        if (c.level.getBlockEntity(pos) instanceof BlockEntityPedestal pedestal) {
            Optional<T> recipe = getRecipeFor(c.level, new SimpleContainer(pedestal.getItem()));
            recipe.ifPresent(r -> {
                var stacks = r.getRecipeOutput()
                        .stream()
                        .map(RecipeOutput::roll)
                        .filter(stack -> !stack.isEmpty())
                        .toList();

                if (r.getRecipeOutput().size() == 1) {
                    pedestal.setItem(stacks.get(0));
                } else {
                    pedestal.setItem(ItemStack.EMPTY);
                    stacks.forEach(stack -> ItemHelpers.addItemEntity(c.level, stack, pos.above()));
                }

                BlockHelpers.sync(c.level, pos);
                ArcaneFx.ON_CAST_GRAVITY.goBlock(c.level, pos.above());
                didCraft.set(true);
            });
        } else {
            Item item = c.level.getBlockState(pos).getBlock().asItem();
            SimpleContainer container = new SimpleContainer(new ItemStackHandler());
            container.setItem(new ItemStack(item));
            Optional<T> recipe = getRecipeFor(c.level, container);
            recipe.ifPresent(r -> {
                c.level.removeBlock(pos, false);

                r.getRecipeOutput()
                        .stream()
                        .map(RecipeOutput::roll)
                        .filter(stack -> !stack.isEmpty())
                        .forEach(stack -> ItemHelpers.addItemEntity(c.level, stack, pos.above()));

                ArcaneFx.ON_CAST_GRAVITY.goBlock(c.level, pos);
                didCraft.set(true);
            });
        }

        return didCraft.get() ? new CastResult(costPerCraft, false) : CastResult.FAILED;
    }

    protected abstract Optional<T> getRecipeFor(Level level, SimpleContainer container);
}
