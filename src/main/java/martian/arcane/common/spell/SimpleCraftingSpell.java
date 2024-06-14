package martian.arcane.common.spell;

import martian.arcane.api.block.BlockHelpers;
import martian.arcane.api.item.ItemHelpers;
import martian.arcane.api.recipe.RecipeOutput;
import martian.arcane.api.recipe.SimpleContainer;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.spell.CastResult;
import martian.arcane.api.spell.SpellConfig;
import martian.arcane.common.block.pedestal.BlockEntityPedestal;
import martian.arcane.common.recipe.SpellRecipe;
import martian.arcane.common.recipe.SpellRecipeType;
import martian.arcane.integration.photon.ArcaneFx;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.items.ItemStackHandler;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SimpleCraftingSpell extends AbstractSpell {
    private final SpellRecipeType type;

    public SimpleCraftingSpell(SpellRecipeType type) {
        this.type = type;
    }

    @Override
    public CastResult cast(CastContext c) {
        if (c.level.isClientSide)
            return CastResult.SUCCESS;

        BlockPos pos = c.getTarget();
        if (pos == null)
            return CastResult.FAILED;

        AtomicBoolean didCraft = new AtomicBoolean(false);

        if (c.level.getBlockEntity(pos) instanceof BlockEntityPedestal pedestal) {
            Optional<RecipeHolder<SpellRecipe>> recipe = SpellRecipe.getRecipeFor(type, c.level, new SimpleContainer(pedestal.getItem()));
            recipe.ifPresent(r -> {
                var stacks = r.value().getRecipeOutput()
                        .stream()
                        .map(RecipeOutput::roll)
                        .filter(stack -> !stack.isEmpty())
                        .toList();

                if (r.value().getRecipeOutput().size() == 1) {
                    pedestal.setItem(stacks.getFirst());
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
            Optional<RecipeHolder<SpellRecipe>> recipe = SpellRecipe.getRecipeFor(type, c.level, container);
            recipe.ifPresent(r -> {
                c.level.removeBlock(pos, false);

                r.value().getRecipeOutput()
                        .stream()
                        .map(RecipeOutput::roll)
                        .filter(stack -> !stack.isEmpty())
                        .forEach(stack -> ItemHelpers.addItemEntity(c.level, stack, pos.above()));

                ArcaneFx.ON_CAST_GRAVITY.goBlock(c.level, pos);
                didCraft.set(true);
            });
        }

        return new CastResult(didCraft.get(), null);
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
