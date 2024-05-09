package martian.arcane.common.item;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.item.AbstractAuraItem;
import martian.arcane.client.ArcaneKeybindings;
import martian.arcane.common.networking.c2s.C2SPacketOpenEnderpack;
import martian.arcane.common.registry.ArcaneNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class ItemEnderpack extends AbstractAuraItem {
    private static final Component CONTAINER_TITLE = Component.translatable("container.arcane.enderpack");

    public ItemEnderpack() {
        super(ArcaneStaticConfig.AuraMaximums.ENDERPACK, false, true, new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    public static InteractionResultHolder<ItemStack> open(ItemStack stack, Level level, Player player) {
        return ((ItemEnderpack)stack.getItem()).mapAuraStorage(stack, storage -> {
            if (storage.getAura() < ArcaneStaticConfig.Consumption.ENDERPACK)
                return InteractionResultHolder.fail(stack);

            if (!level.isClientSide) {
                storage.removeAura(ArcaneStaticConfig.Consumption.ENDERPACK);
                PlayerEnderChestContainer playerEnderChestContainer = player.getEnderChestInventory();
                player.openMenu(new SimpleMenuProvider(
                        (windowId, inventory, ignoredPlayer) -> ChestMenu.threeRows(windowId, inventory, playerEnderChestContainer),
                        CONTAINER_TITLE
                ));
                player.awardStat(Stats.OPEN_ENDERCHEST);
            }

            return InteractionResultHolder.consume(stack);
        }).orElseThrow();
    }

    public void tick(Entity entity) {
        if (entity.level().isClientSide && entity instanceof Player && ArcaneKeybindings.OPEN_ENDERPACK.get().consumeClick())
            ArcaneNetworking.CHANNEL.sendToServer(new C2SPacketOpenEnderpack());
    }

    @ParametersAreNonnullByDefault
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        tick(entity);
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return open(player.getItemInHand(hand), level, player);
    }
}
