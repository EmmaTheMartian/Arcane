package martian.arcane.block;

import martian.arcane.ArcaneTags;
import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.block.entity.BlockEntitySpellcraftingCore;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockSpellcraftingCore extends AbstractAuraMachine {
    public BlockSpellcraftingCore() {
        super(PropertyHelpers.basicAuraMachine().noOcclusion(), BlockEntitySpellcraftingCore::new);
    }

    @SuppressWarnings("deprecation")
    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide())
            return InteractionResult.SUCCESS;

        if (level.getBlockEntity(pos) instanceof BlockEntitySpellcraftingCore core) {
            ItemStack stack = player.getItemInHand(hand);

            if (stack.is(ArcaneTags.WANDS))
            {
                BlockEntitySpellcraftingCore.SpellcraftingCraftStatus status =
                        BlockEntitySpellcraftingCore.craft(level, pos, state, core);

                switch (status) {
                    case SUCCESS -> player.sendSystemMessage(Component.literal("Crafted!"));
                    case INVALID_MULTIBLOCK -> player.sendSystemMessage(Component.literal("Invalid multiblock."));
                    case INSUFFICIENT_AURA -> player.sendSystemMessage(Component.literal("Insufficient aura."));
                    case NOT_A_RECIPE -> player.sendSystemMessage(Component.literal("No such recipe."));
                }
            } else {
                if (!core.getItem().isEmpty()) {
                    player.getInventory().placeItemBackInInventory(core.getItem());
                    core.setItem(ItemStack.EMPTY);
                } else if (!stack.isEmpty()) {
                    core.setItem(stack.copyWithCount(1));
                    stack.shrink(1);
                }
            }

            level.sendBlockUpdated(pos, state, state, 2);
        }

        return InteractionResult.SUCCESS;
    }
}
