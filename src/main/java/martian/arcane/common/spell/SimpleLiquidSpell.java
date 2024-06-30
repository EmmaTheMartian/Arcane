package martian.arcane.common.spell;

import martian.arcane.api.spell.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class SimpleLiquidSpell extends AbstractSpell {
    private final Function<CastContext, Fluid> fluid;
    private final Function<CastContext, BlockState> liquidState;
    private final @Nullable Function<CastContext, BlockState> cauldronState;

    public SimpleLiquidSpell(Function<CastContext, Fluid> fluid, Function<CastContext, BlockState> liquidState, @Nullable Function<CastContext, BlockState> cauldronState) {
        this.fluid = fluid;
        this.liquidState = liquidState;
        this.cauldronState = cauldronState;
    }

    @Override
    public CastResult cast(CastContext c) {
        if (c.level.isClientSide)
            return CastResult.SUCCESS;

        if (c.target.type() == CastTarget.Type.BLOCK) {
            BlockPos target = ((BlockPos) c.target.value());
            BlockState targetState = c.level.getBlockState(target);

            if (targetState.hasBlockEntity()) {
                BlockEntity entity = c.level.getBlockEntity(target);
                var cap = c.level.getCapability(Capabilities.FluidHandler.BLOCK, target, targetState, entity, null);
                if (cap != null)
                    cap.fill(new FluidStack(fluid.apply(c), 1000), IFluidHandler.FluidAction.EXECUTE);
            } else if (this.cauldronState != null && targetState.is(Blocks.CAULDRON)) {
                c.level.setBlockAndUpdate(target, cauldronState.apply(c));
            } else {
                BlockPos pos = target;

                if (c instanceof CastContext.WandContext wc) {
                    HitResult hit = wc.raycast();
                    if (hit.getType() != HitResult.Type.BLOCK)
                        return CastResult.FAILED;

                    BlockHitResult bHit = (BlockHitResult) hit;
                    pos = bHit.getBlockPos().relative(bHit.getDirection());
                }

                if (!c.level.isInWorldBounds(pos) || !c.level.getBlockState(pos).canBeReplaced())
                    return CastResult.FAILED;

                c.level.setBlockAndUpdate(pos, liquidState.apply(c));
            }
        }

        return CastResult.FAILED;
    }

    public static SimpleLiquidSpell of(ResourceLocation id, int auraCost, int cooldown, int minLevel, Function<CastContext, Fluid> fluid, Function<CastContext, BlockState> liquidState, @Nullable Function<CastContext, BlockState> cauldronState) {
        return new SimpleLiquidSpell(fluid, liquidState, cauldronState) {
            private final SpellConfig config = SpellConfig.basicConfig(id, auraCost, cooldown, minLevel).build();

            @Override
            protected SpellConfig getConfig() {
                return config;
            }
        };
    }
}
