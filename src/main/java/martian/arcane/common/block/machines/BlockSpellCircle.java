package martian.arcane.common.block.machines;

import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.common.block.entity.machines.BlockEntitySpellCircle;
import martian.arcane.common.item.ItemSpellTablet;
import martian.arcane.common.registry.ArcaneBlockEntities;
import martian.arcane.common.registry.ArcaneItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockSpellCircle extends AbstractAuraMachine {
    public BlockSpellCircle(int maxAura, int castRateTicks, int castingLevel) {
        super(BlockBehaviour.Properties.of().noOcclusion().noCollission().sound(SoundType.EMPTY), (pos, state) ->
                new BlockEntitySpellCircle(maxAura, castRateTicks, castingLevel, pos, state));
    }

    @SuppressWarnings("deprecation")
    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        ItemStack held = player.getItemInHand(hand);
        BlockEntitySpellCircle circle = (BlockEntitySpellCircle) level.getBlockEntity(pos);

        if (held.isEmpty() || circle == null)
            return InteractionResult.FAIL;

        if (held.is(ArcaneItems.SPELL_TABLET.get()) && !circle.hasSpell()) {
            circle.setSpell(ItemSpellTablet.getSpell(held));
            held.shrink(1);
            return InteractionResult.CONSUME;
        } else if (held.is(ArcaneItems.ARCANE_BLEACH.get()) && circle.hasSpell()) {
            circle.setSpell(null);
            held.shrink(1);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @ParametersAreNonnullByDefault
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide && type == ArcaneBlockEntities.SPELL_CIRCLE.get() ? BlockEntitySpellCircle::tick : null;
    }
}
