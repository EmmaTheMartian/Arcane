package martian.arcane.block;

import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.block.entity.BlockEntityAuraInfuser;
import martian.arcane.recipe.aurainfuser.RecipeAuraInfusion;
import martian.arcane.registry.ArcaneBlockEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

public class BlockAuraInfuser extends AbstractAuraMachine {
    public BlockAuraInfuser() {
        super(PropertyHelpers.basicAuraMachine().noOcclusion(), BlockEntityAuraInfuser::new);
    }

    @Override
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide() && type == ArcaneBlockEntities.AURA_INFUSER_BE.get() ? BlockEntityAuraInfuser::tick : null;
    }

    @SuppressWarnings("deprecation")
    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        player.sendSystemMessage(Component.literal("Recipes registered on ").append(level.isClientSide() ? "client" : "server"));
        for (RecipeAuraInfusion recipe : RecipeAuraInfusion.getAllRecipes(level)) {
            player.sendSystemMessage(Component
                    .literal("Recipe: ")
                    .append(recipe.input().toString())
                    .append(" -> ")
                    .append(recipe.result().getDisplayName())
                    .append(" (aura: ")
                    .append(Integer.toString(recipe.aura()))
                    .append(")"));
        }

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (level.getBlockEntity(pos) instanceof BlockEntityAuraInfuser infuser) {
            ItemStack playerHeldStack = player.getItemInHand(hand);

            if (player.isCrouching()) {
                player.sendSystemMessage(Component.literal("Holding: ").append(infuser.getItem().getDisplayName()));
                player.sendSystemMessage(Component.literal("Inner Aura: ").append(Integer.toString(infuser.auraProgress)));

                Optional<RecipeAuraInfusion> optionalRecipe = infuser.getRecipeIgnoringAura();
                if (optionalRecipe.isPresent()) {
                    RecipeAuraInfusion recipe = optionalRecipe.get();

                    player.sendSystemMessage(Component
                            .literal("Crafting: ")
                            .append(recipe.result().getDisplayName()));

                    player.sendSystemMessage(Component
                            .literal("Crafting Progress: ")
                            .append(Integer.toString(infuser.auraProgress))
                            .append("/")
                            .append(Integer.toString(recipe.aura())));
                } else {
                    player.sendSystemMessage(Component
                            .literal("No recipe using this item.")
                            .withStyle(ChatFormatting.RED));
                }

                return InteractionResult.SUCCESS;
            }

            if (!infuser.getItem().isEmpty()) {
                player.getInventory().placeItemBackInInventory(infuser.getItem());
                infuser.setItem(ItemStack.EMPTY);
            } else if (!playerHeldStack.isEmpty()) {
                infuser.setItem(playerHeldStack.copyWithCount(1));
                playerHeldStack.shrink(1);
            }

            level.sendBlockUpdated(pos, state, state, 2);
        }

        return InteractionResult.CONSUME;
    }
}
