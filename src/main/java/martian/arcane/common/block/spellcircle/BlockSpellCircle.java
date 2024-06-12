package martian.arcane.common.block.spellcircle;

import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.common.item.ItemSpellTablet;
import martian.arcane.common.registry.ArcaneBlockEntities;
import martian.arcane.common.registry.ArcaneItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

public class BlockSpellCircle extends AbstractAuraMachine {
    public static Map<Direction, VoxelShape> SHAPES = Map.of(
            Direction.UP, Block.box(0, 14, 0, 16, 16, 16),
            Direction.DOWN, Block.box(0, 0, 0, 16, 2, 16),
            Direction.NORTH, Block.box(0, 0, 0, 16, 16, 2),
            Direction.SOUTH, Block.box(0, 0, 14, 16, 16, 16),
            Direction.EAST, Block.box(14, 0, 0, 16, 16, 16),
            Direction.WEST, Block.box(0, 0, 0, 2, 16, 16)
    );

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BlockSpellCircle(int maxAura, int castRateTicks, int castingLevel) {
        super(BlockBehaviour.Properties.of().noOcclusion().noCollission().sound(SoundType.EMPTY), (pos, state) ->
                new BlockEntitySpellCircle(maxAura, castRateTicks, castingLevel, pos, state));
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.DOWN));
    }

    @Override
    @ParametersAreNonnullByDefault
    @NotNull
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide)
            return ItemInteractionResult.SUCCESS;

        ItemStack held = player.getItemInHand(hand);
        BlockEntitySpellCircle circle = (BlockEntitySpellCircle) level.getBlockEntity(pos);

        if (held.isEmpty() || circle == null)
            return ItemInteractionResult.FAIL;

        if (held.is(ArcaneItems.SPELL_TABLET.get()) && !circle.hasSpell()) {
            circle.setSpell(ItemSpellTablet.getSpellId(held));
            held.shrink(1);
            return ItemInteractionResult.CONSUME;
        } else if (held.is(ArcaneItems.ARCANE_BLEACH.get()) && circle.hasSpell()) {
            circle.setSpell(null);
            held.shrink(1);
            circle.setActive(false);
            return ItemInteractionResult.CONSUME;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

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
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide && type == ArcaneBlockEntities.SPELL_CIRCLE.get() ? BlockEntitySpellCircle::tick : null;
    }

    @Override
    @ParametersAreNonnullByDefault
    @NotNull
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
    }
}
