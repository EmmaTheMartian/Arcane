package martian.arcane.common.spell;

import martian.arcane.ArcaneMod;
import martian.arcane.api.item.ItemHelpers;
import martian.arcane.api.recipe.SingleItemContainer;
import martian.arcane.api.recipe.SingleItemEntityContainer;
import martian.arcane.api.spell.*;
import martian.arcane.common.block.pedestal.BlockEntityPedestal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class SpellSmelting extends AbstractSpell {
    private static final SpellConfig config = SpellConfig
            .basicConfig(ArcaneMod.id("smelting"), 8, 20, 2)
            .build();

    @Override
    protected SpellConfig getConfig() {
        return config;
    }

    @Override
    public CastResult cast(CastContext c) {
        if (c.level.isClientSide)
            return CastResult.SUCCESS;

        if (c.target.type() == CastTarget.Type.BLOCK) {
            BlockPos pos = ((BlockPos) c.target.value());
            BlockState state = c.level.getBlockState(pos);
            if (c.level.getBlockEntity(pos) instanceof BlockEntityPedestal pedestal) {
                var container = new SingleItemContainer(pedestal.getItem());
                var recipe = c.level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, container, c.level);
                recipe.ifPresent(it -> pedestal.setItem(it.value().assemble(container, c.level.registryAccess())));
                return recipe.isPresent() ? CastResult.SUCCESS : CastResult.FAILED;
            } else {
                var container = new SingleItemContainer(state.getBlock().asItem().getDefaultInstance());
                var recipe = c.level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, container, c.level);
                recipe.ifPresent(it -> {
                    var result = it.value().assemble(container, c.level.registryAccess());
                    if (result.getItem() instanceof BlockItem bi) {
                        c.level.setBlockAndUpdate(pos, bi.getBlock().defaultBlockState());
                    } else {
                        c.level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                        ItemHelpers.addItemEntity(c.level, result, pos);
                    }
                });
                return recipe.isPresent() ? CastResult.SUCCESS : CastResult.FAILED;
            }
        } else if (c.target.type() == CastTarget.Type.ENTITY) {
            if (c.target.value() instanceof ItemEntity ie) {
                var container = new SingleItemEntityContainer(ie);
                var recipe = c.level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, container, c.level);
                recipe.ifPresent(it -> it.value().assemble(container, c.level.registryAccess()));
                return recipe.isPresent() ? CastResult.SUCCESS : CastResult.FAILED;
            }
        }

        return CastResult.FAILED;
    }
}
