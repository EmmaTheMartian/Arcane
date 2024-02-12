package martian.arcane.block;

import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.block.entity.BlockEntityPedestal;
import martian.arcane.item.ItemAuraWand;
import martian.arcane.item.ItemSpellTablet;
import martian.arcane.recipe.RecipePedestalCrafting;
import martian.arcane.registry.ArcaneSpells;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
            Block.box(3, 12, 3, 13, 12.5, 13),
            Block.box(4, 2, 4, 12, 10, 12)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public BlockPedestal() {
        super(PropertyHelpers.basicAuraMachine().noOcclusion());
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
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (level.getBlockEntity(pos) instanceof BlockEntityPedestal pedestal) {
            ItemStack stack = player.getItemInHand(hand);
            ItemStack pedestalStack = pedestal.getItem();

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
                    //todo: translatable
                    player.sendSystemMessage(Component.literal("Invalid wand for spell."));
                    return InteractionResult.FAIL;
                }
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

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof BlockEntityPedestal pedestal) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), pedestal.getItem());
            // Note: If comparator support is added for this then it'll need to be updated here
            // See ChestBlock#onRemove
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityPedestal(pos, state);
    }
}
