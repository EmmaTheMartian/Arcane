package martian.arcane.item;

import martian.arcane.api.NBTHelpers;
import martian.arcane.api.Raycasting;
import martian.arcane.block.entity.BlockEntityAuraExtractor;
import martian.arcane.block.entity.BlockEntityAuraInserter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class ItemAuraWrench extends Item {
    public ItemAuraWrench() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        // Guard clauses for days...
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide())
            return InteractionResultHolder.success(stack);

        CompoundTag nbt = stack.getOrCreateTag();
        initNbt(nbt); // Init just-in-case

        if (player.isCrouching()) {
            nbt.putBoolean(NBTHelpers.KEY_WRENCH_HASP1, false);
            return InteractionResultHolder.success(stack);
        }

        BlockHitResult hit = Raycasting.blockRaycast(player, player.getBlockReach(), false);
        if (hit == null)
            return InteractionResultHolder.fail(stack);

        if (nbt.getBoolean(NBTHelpers.KEY_WRENCH_HASP1)) {
            BlockEntity l = level.getBlockEntity(hit.getBlockPos());
            if (l == null)
                return InteractionResultHolder.fail(stack);

            if (l instanceof BlockEntityAuraInserter inserter) {
                BlockPos pos1 = NBTHelpers.getBlockPos(nbt, NBTHelpers.KEY_WRENCH_P1);
                BlockEntity e = level.getBlockEntity(pos1);

                if (e instanceof BlockEntityAuraExtractor extractor) {
                    BlockEntityAuraExtractor.setTarget(extractor, inserter);
                    player.sendSystemMessage(Component.translatable("messages.arcane.linked"));
                    nbt.putBoolean(NBTHelpers.KEY_WRENCH_HASP1, false);
                    return InteractionResultHolder.success(stack);
                }
            }
        } else {
            BlockEntity e = level.getBlockEntity(hit.getBlockPos());
            if (e == null)
                return InteractionResultHolder.fail(stack);
            if (e instanceof BlockEntityAuraExtractor) {
                nbt.putBoolean(NBTHelpers.KEY_WRENCH_HASP1, true);
                NBTHelpers.putBlockPos(nbt, NBTHelpers.KEY_WRENCH_P1, hit.getBlockPos());
                player.sendSystemMessage(Component.translatable("messages.arcane.selected"));
                return InteractionResultHolder.success(stack);
            }
        }

        return InteractionResultHolder.fail(stack);
    }

    public static void initNbt(CompoundTag nbt) {
        NBTHelpers.init(nbt, NBTHelpers.KEY_WRENCH_HASP1, (nbt_, key) -> {
            nbt.putBoolean(key, false);
        });
        NBTHelpers.init(nbt, NBTHelpers.KEY_WRENCH_P1, (nbt_, key) -> {
            NBTHelpers.putBlockPos(nbt, key, new BlockPos(0, 0, 0));
        });
    }
}
