package martian.arcane.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BlockConjuredCraftingTable extends Block {
    // Using funny java features to "override" the default container title of CraftingTableBlock's screen.
    @SuppressWarnings("unused")
    private static final Component CONTAINER_TITLE = Component.translatable("container.arcane.conjured_crafting_table");

    public BlockConjuredCraftingTable() {
        super(Properties.copy(Blocks.GLASS).instabreak().noParticlesOnBreak());
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(level, pos));
            return InteractionResult.CONSUME;
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((windowId, inventory, player) -> {
            //noinspection CodeBlock2Expr
            return new CraftingMenu(windowId, inventory, ContainerLevelAccess.create(level, pos)) {
                @Override
                public boolean stillValid(Player player) {
                    return true;
                }
            };
        }, CONTAINER_TITLE);
    }
}
