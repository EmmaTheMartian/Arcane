package martian.arcane.common.block.pedestal;

import com.klikli_dev.modonomicon.client.gui.BookGuiManager;
import com.klikli_dev.modonomicon.item.ModonomiconItem;
import martian.arcane.api.block.BlockHelpers;
import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.common.item.ItemAuraWand;
import martian.arcane.common.item.ItemSpellTablet;
import martian.arcane.common.recipe.RecipePedestalCrafting;
import martian.arcane.common.registry.ArcaneBlockEntities;
import martian.arcane.common.registry.ArcaneItems;
import martian.arcane.common.registry.ArcaneRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.stream.Stream;

public class BlockPedestal extends Block implements EntityBlock {
    public static final VoxelShape SHAPE = Stream.of(
            Block.box(3, 0, 3, 13, 2, 13),
            Block.box(2, 10, 2, 14, 12, 14),
            Block.box(4, 2, 4, 12, 10, 12)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public final int defaultMaxAura;

    public BlockPedestal(int maxAura) {
        super(PropertyHelpers.basicAuraMachine().noOcclusion());
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
        this.defaultMaxAura = maxAura;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    @ParametersAreNonnullByDefault
    @NotNull
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof BlockEntityPedestal pedestal) {
            ItemStack pedestalStack = pedestal.getItem();

            // If the pedestal stack is a guidebook, open it!
            if (!player.isCrouching() && pedestalStack.getItem() instanceof ModonomiconItem) {
                BookGuiManager.get().openBook(ModonomiconItem.getBook(pedestalStack).getId());
                return ItemInteractionResult.SUCCESS;
            }

            if (level.isClientSide)
                return ItemInteractionResult.SUCCESS;

            // Set wand spell
            if (
                pedestalStack.getItem() instanceof ItemAuraWand wand &&
                stack.getItem() instanceof ItemSpellTablet &&
                ItemSpellTablet.hasSpell(stack)
            ) {
                // Prevent overriding spells
                if (ItemAuraWand.getSpellId(pedestalStack) != null)
                    return ItemInteractionResult.FAIL;

                ResourceLocation id = ItemSpellTablet.getSpellId(stack);
                if (id == null)
                    return ItemInteractionResult.FAIL;

                AbstractSpell spell = ArcaneRegistries.SPELLS.get(id);
                assert spell != null;
                if (spell.isValidWand(wand)) {
                    wand.setSpell(id, pedestalStack);
                    player.sendSystemMessage(Component.translatable("messages.arcane.pedestal_set_spell"));
                    stack.shrink(1);
                    level.sendBlockUpdated(pos, state, state, 2);
                    return ItemInteractionResult.SUCCESS;
                } else {
                    player.sendSystemMessage(Component.translatable("messages.arcane.invalid_wand_for_spell"));
                    return ItemInteractionResult.FAIL;
                }
            }

            // Remove wand spell
            if (
                pedestalStack.getItem() instanceof ItemAuraWand &&
                stack.is(ArcaneItems.ARCANE_BLEACH.get()) &&
                ItemAuraWand.hasSpell(pedestalStack)
            ) {
                ItemAuraWand.removeSpell(pedestalStack);
                stack.shrink(1);
                return ItemInteractionResult.CONSUME;
            }

            // Pedestal crafting
            if (
                !pedestalStack.isEmpty() &&
                !stack.isEmpty()
            ) {
                Optional<RecipeHolder<RecipePedestalCrafting>> recipe = RecipePedestalCrafting.getRecipeFor(level, pedestalStack, stack);
                if (recipe.isPresent()) {
                    recipe.get().value().assemble(pedestal, stack);
                    level.sendBlockUpdated(pos, state, state, 2);
                    return ItemInteractionResult.CONSUME;
                }
            }

            // Remove item from pedestal
            if (!pedestalStack.isEmpty()) {
                player.getInventory().placeItemBackInInventory(pedestal.getItem());
                pedestal.setItem(ItemStack.EMPTY);
                level.sendBlockUpdated(pos, state, state, 2);
                return ItemInteractionResult.CONSUME;
            }
            // Put item on pedestal
            else if (!stack.isEmpty()) {
                pedestal.setItem(stack.copyWithCount(1));
                stack.shrink(1);
                level.sendBlockUpdated(pos, state, state, 2);
                return ItemInteractionResult.CONSUME;
            }
        }

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    @ParametersAreNonnullByDefault
    @NotNull
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(level, pos, state, player);
        if (level.getBlockEntity(pos) instanceof BlockEntityPedestal pedestal)
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), pedestal.getItem()));
        return state;
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityPedestal(defaultMaxAura, pos, state);
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof BlockEntityPedestal pedestal)
            return pedestal.getItem().getCount() > 0 ? 15 : 0;
        return 0;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block fromBlock, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, fromBlock, fromPos, isMoving);
        if (
            level.getBlockEntity(pos) instanceof BlockEntityPedestal pedestal &&
            pedestal.hasSignal != level.hasNeighborSignal(pos)
        ) {
            pedestal.hasSignal = !pedestal.hasSignal;
            level.updateNeighbourForOutputSignal(pos, this);
            BlockHelpers.sync(pedestal);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide && type == ArcaneBlockEntities.PEDESTAL.get() ? BlockEntityPedestal::tick : null;
    }
}
