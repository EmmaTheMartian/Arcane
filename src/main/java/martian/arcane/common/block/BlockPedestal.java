package martian.arcane.common.block;

import com.klikli_dev.modonomicon.client.gui.BookGuiManager;
import com.klikli_dev.modonomicon.item.ModonomiconItem;
import martian.arcane.api.block.BlockHelpers;
import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.common.block.entity.BlockEntityPedestal;
import martian.arcane.common.item.ItemAuraWand;
import martian.arcane.common.item.ItemSpellTablet;
import martian.arcane.common.recipe.RecipePedestalCrafting;
import martian.arcane.common.registry.ArcaneItems;
import martian.arcane.common.registry.ArcaneSpells;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
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

    public BlockPedestal() {
        super(PropertyHelpers.basicAuraMachine().noOcclusion());
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @SuppressWarnings("deprecation")
    @Override
    @ParametersAreNonnullByDefault
    @NotNull
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @SuppressWarnings("deprecation")
    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof BlockEntityPedestal pedestal) {
            ItemStack stack = player.getItemInHand(hand);
            ItemStack pedestalStack = pedestal.getItem();

            // If the player is holding a wand with a spell, they are probably trying to cast it
            if (stack.getItem() instanceof ItemAuraWand && ItemAuraWand.hasSpell(stack))
                return InteractionResult.PASS;

            // If the pedestal stack is a guidebook, open it!
            if (!player.isCrouching() && pedestalStack.getItem() instanceof ModonomiconItem) {
                BookGuiManager.get().openBook(ModonomiconItem.getBook(pedestalStack).getId());
                return InteractionResult.SUCCESS;
            }

            if (level.isClientSide)
                return InteractionResult.SUCCESS;

            // Set wand spell
            if (
                pedestalStack.getItem() instanceof ItemAuraWand wand &&
                stack.getItem() instanceof ItemSpellTablet &&
                ItemSpellTablet.hasSpell(stack)
            ) {
                // Prevent overriding spells
                if (ItemAuraWand.getSpellId(pedestalStack) != null)
                    return InteractionResult.FAIL;

                ResourceLocation id = ItemSpellTablet.getSpell(stack);
                if (id == null)
                    return InteractionResult.FAIL;

                AbstractSpell spell = ArcaneSpells.getSpellById(id);
                if (spell.isValidWand(wand)) {
                    wand.setSpell(id, pedestalStack);
                    player.sendSystemMessage(Component.translatable("messages.arcane.pedestal_set_spell"));
                    stack.shrink(1);
                    level.sendBlockUpdated(pos, state, state, 2);
                    return InteractionResult.SUCCESS;
                } else {
                    player.sendSystemMessage(Component.translatable("messages.arcane.invalid_wand_for_spell"));
                    return InteractionResult.FAIL;
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
                return InteractionResult.CONSUME;
            }

            // Pedestal crafting
            if (
                !pedestalStack.isEmpty() &&
                !stack.isEmpty()
            ) {
                Optional<RecipePedestalCrafting> recipe = RecipePedestalCrafting.getRecipeFor(level, pedestalStack, stack);
                if (recipe.isPresent()) {
                    recipe.get().assemble(pedestal, stack);
                    level.sendBlockUpdated(pos, state, state, 2);
                    return InteractionResult.CONSUME;
                }
            }

            // Remove item from pedestal
            if (!pedestalStack.isEmpty()) {
                player.getInventory().placeItemBackInInventory(pedestal.getItem());
                pedestal.setItem(ItemStack.EMPTY);
                level.sendBlockUpdated(pos, state, state, 2);
                return InteractionResult.CONSUME;
            }
            // Put item on pedestal
            else if (!stack.isEmpty() && pedestalStack.isEmpty()) {
                pedestal.setItem(stack.copyWithCount(1));
                stack.shrink(1);
                level.sendBlockUpdated(pos, state, state, 2);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(level, pos, state, player);
        if (level.getBlockEntity(pos) instanceof BlockEntityPedestal pedestal)
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), pedestal.getItem()));
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityPedestal(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof BlockEntityPedestal pedestal)
            return pedestal.getItem().getCount() > 0 ? 15 : 0;
        return 0;
    }

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
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
}
