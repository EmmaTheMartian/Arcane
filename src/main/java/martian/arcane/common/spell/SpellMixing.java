package martian.arcane.common.spell;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneTags;
import martian.arcane.api.recipe.ItemEntityContainer;
import martian.arcane.api.spell.*;
import martian.arcane.common.recipe.RecipeCauldronMixing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class SpellMixing extends AbstractSpell {
    private static final SpellConfig config = SpellConfig
            .basicConfig(ArcaneMod.id("mixing"), 4, 20, 1)
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
            BlockPos target = ((BlockPos) c.target.value());
            BlockState state = c.level.getBlockState(target);

            if (state.is(ArcaneTags.MIXING_CAULDRONS)) {
                NonNullList<ItemEntity> items = NonNullList.create();
                items.addAll(c.level.getEntitiesOfClass(ItemEntity.class, new AABB(target)));
                ItemEntityContainer container = new ItemEntityContainer(items);
                var recipe = RecipeCauldronMixing.getRecipeFor(c.level, container, state);
                recipe.ifPresent(it -> it.value().assemble(container, c.level, target));
                return recipe.isPresent() ? CastResult.SUCCESS : CastResult.FAILED;
            }
        }

        return CastResult.FAILED;
    }
}
