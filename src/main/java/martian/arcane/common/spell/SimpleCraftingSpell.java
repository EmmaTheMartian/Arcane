package martian.arcane.common.spell;

import martian.arcane.api.item.ItemHelpers;
import martian.arcane.api.recipe.RecipeOutput;
import martian.arcane.api.recipe.SingleItemContainer;
import martian.arcane.api.spell.*;
import martian.arcane.common.block.pedestal.BlockEntityPedestal;
import martian.arcane.common.recipe.SpellRecipe;
import martian.arcane.common.recipe.SpellRecipeType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public abstract class SimpleCraftingSpell extends AbstractSpell {
    private final SpellRecipeType type;

    public SimpleCraftingSpell(SpellRecipeType type) {
        this.type = type;
    }

    @Override
    public CastResult cast(CastContext c) {
        if (c.level.isClientSide)
            return CastResult.SUCCESS;

        return switch (c.target.type()) {
            case MISS -> CastResult.FAILED;
            case BLOCK -> new CastResult(craftUsingBlock(type, c.level, (BlockPos)c.target.value()), null);
            case ENTITY -> c.target.value() instanceof ItemEntity ie ?
                    new CastResult(craftUsingItemEntity(ie, type, c.level, ie.blockPosition()), null) :
                    CastResult.FAILED;
        };
    }

    public static boolean mapRecipeIfExists(SpellRecipeType type, Level level, BlockPos pos, SingleItemContainer container, BiConsumer<RecipeHolder<SpellRecipe>, List<ItemStack>> consumer) {
        AtomicBoolean didCraft = new AtomicBoolean(false);
        SpellRecipe.getRecipeFor(type, level, container).ifPresent(r -> {
            var stacks = r.value().getRecipeOutput()
                    .stream()
                    .map(RecipeOutput::roll)
                    .filter(stack -> !stack.isEmpty())
                    .toList();
            consumer.accept(r, stacks);
            didCraft.set(true);
        });
        return didCraft.get();
    }

    public static boolean craftUsingBlock(SpellRecipeType type, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof BlockEntityPedestal pedestal) {
            return mapRecipeIfExists(type, level, pos, new SingleItemContainer(pedestal.getItem()), (r, stacks) -> {
                if (r.value().getRecipeOutput().size() == 1) {
                    pedestal.setItem(stacks.getFirst());
                } else {
                    pedestal.setItem(ItemStack.EMPTY);
                    stacks.forEach(stack -> ItemHelpers.addItemEntity(level, stack, pos));
                }
            });
        } else {
            return mapRecipeIfExists(type, level, pos, new SingleItemContainer(level.getBlockState(pos).getBlock().asItem().getDefaultInstance()), (r, stacks) -> {
                level.removeBlock(pos, false);
                if (stacks.size() == 1 && stacks.getFirst().getItem() instanceof BlockItem bi)
                    level.setBlockAndUpdate(pos, bi.getBlock().defaultBlockState());
                else
                    stacks.forEach(stack -> ItemHelpers.addItemEntity(level, stack, pos));
            });
        }
    }

    public static boolean craftUsingItemEntity(ItemEntity item, SpellRecipeType type, Level level, BlockPos pos) {
        return mapRecipeIfExists(type, level, pos, new SingleItemContainer(item.getItem()), (r, stacks) -> {
            item.remove(Entity.RemovalReason.DISCARDED);

            if (stacks.size() == 1 && stacks.getFirst().getItem() instanceof BlockItem bi)
                level.setBlockAndUpdate(pos, bi.getBlock().defaultBlockState());
            else
                stacks.forEach(stack -> ItemHelpers.addItemEntity(level, stack, pos.above()));
        });
    }

    public static SimpleCraftingSpell of(ResourceLocation id, int auraCost, int cooldown, int minLevel, SpellRecipeType type) {
        return new SimpleCraftingSpell(type) {
            private final SpellConfig config = SpellConfig.basicConfig(id, auraCost, cooldown, minLevel).build();

            @Override
            protected SpellConfig getConfig() {
                return config;
            }
        };
    }
}
