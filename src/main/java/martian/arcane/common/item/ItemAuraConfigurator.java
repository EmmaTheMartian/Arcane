package martian.arcane.common.item;

import martian.arcane.api.NBTHelpers;
import martian.arcane.api.Raycasting;
import martian.arcane.common.block.entity.machines.BlockEntityAuraExtractor;
import martian.arcane.common.block.entity.machines.BlockEntityAuraInserter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemAuraConfigurator extends Item {
    public ItemAuraConfigurator() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide)
            return InteractionResultHolder.success(stack);

        CompoundTag nbt = stack.getOrCreateTag();
        initNBT(nbt); // Init just-in-case

        BlockHitResult hit = Raycasting.blockRaycast(player, player.getBlockReach(), false);

        if (player.isCrouching()) {
            if (hit == null) {
                nbt.putBoolean(NBTHelpers.KEY_CONFIGURATOR_HASP1, false);
                return InteractionResultHolder.success(stack);
            }

            BlockEntity l = level.getBlockEntity(hit.getBlockPos());
            if (l instanceof BlockEntityAuraExtractor extractor) {
                BlockEntityAuraExtractor.removeTarget(extractor);
                BlockState state = level.getBlockState(hit.getBlockPos());
                level.sendBlockUpdated(hit.getBlockPos(), state, state, 2);
            }
        }

        if (hit == null)
            return InteractionResultHolder.fail(stack);

        if (nbt.getBoolean(NBTHelpers.KEY_CONFIGURATOR_HASP1)) {
            BlockEntity l = level.getBlockEntity(hit.getBlockPos());
            if (l instanceof BlockEntityAuraInserter inserter) {
                BlockPos pos1 = NBTHelpers.getBlockPos(nbt, NBTHelpers.KEY_CONFIGURATOR_P1);
                BlockEntity e = level.getBlockEntity(pos1);

                if (e instanceof BlockEntityAuraExtractor extractor) {
                    BlockEntityAuraExtractor.setTarget(extractor, inserter);
                    BlockState state = level.getBlockState(pos1);
                    level.sendBlockUpdated(hit.getBlockPos(), state, state, 2);
                    player.sendSystemMessage(Component.translatable("messages.arcane.linked"));
                    nbt.putBoolean(NBTHelpers.KEY_CONFIGURATOR_HASP1, false);
                    return InteractionResultHolder.success(stack);
                }
            }
        } else {
            BlockEntity e = level.getBlockEntity(hit.getBlockPos());
            if (e instanceof BlockEntityAuraExtractor) {
                nbt.putBoolean(NBTHelpers.KEY_CONFIGURATOR_HASP1, true);
                NBTHelpers.putBlockPos(nbt, NBTHelpers.KEY_CONFIGURATOR_P1, hit.getBlockPos());
                player.sendSystemMessage(Component.translatable("messages.arcane.selected"));
                return InteractionResultHolder.success(stack);
            }
        }

        return InteractionResultHolder.fail(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, @NotNull List<Component> text, @NotNull TooltipFlag flags) {
        text.add(Component.translatable("item.arcane.aura_configurator.tooltip.1"));
        text.add(Component.translatable("item.arcane.aura_configurator.tooltip.2"));
        CompoundTag nbt = stack.getOrCreateTag();
        initNBT(nbt);
        if (nbt.getBoolean(NBTHelpers.KEY_CONFIGURATOR_HASP1)) {
            text.add(Component
                    .translatable("messages.arcane.linking_from")
                    .append(NBTHelpers.getBlockPos(nbt, NBTHelpers.KEY_CONFIGURATOR_P1).toShortString())
                    .withStyle(ChatFormatting.AQUA));
        }
    }

    public static void initNBT(CompoundTag nbt) {
        NBTHelpers.init(nbt, NBTHelpers.KEY_CONFIGURATOR_HASP1, (nbt_, key) -> nbt.putBoolean(key, false));
        NBTHelpers.init(nbt, NBTHelpers.KEY_CONFIGURATOR_P1, (nbt_, key) -> NBTHelpers.putBlockPos(nbt, key, new BlockPos(0, 0, 0)));
    }
}
