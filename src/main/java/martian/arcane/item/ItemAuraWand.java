package martian.arcane.item;

import martian.arcane.api.capability.IAuraStorage;
import martian.arcane.api.item.AbstractAuraItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemAuraWand extends AbstractAuraItem {
    public ItemAuraWand(int maxAura, Properties properties) {
        super(maxAura, false, true, properties);
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isCrouching())
        {
            return super.use(level, player, hand);
        }
        else
        {
            if (level.isClientSide())
                return InteractionResultHolder.success(stack);

            IAuraStorage aura = getAuraStorage(stack).orElseThrow();

            if (aura.getAura() <= 0) {
                player.sendSystemMessage(Component.translatable("messages.arcane.not_enough_aura"));
                return InteractionResultHolder.fail(stack);
            }
            aura.setAura(aura.getAura() - 1);
            player.sendSystemMessage(Component.translatable("messages.arcane.cast_spell"));
        }

        return InteractionResultHolder.success(stack);
    }
}
