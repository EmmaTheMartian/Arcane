package martian.arcane.common.networking.c2s;

import martian.arcane.common.item.ItemEnderpack;
import martian.arcane.common.registry.ArcaneItems;
import martian.arcane.integration.curios.CuriosIntegration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.function.Supplier;

public class C2SPacketOpenEnderpack {
    public void encoder(FriendlyByteBuf ignored) {
    }

    public static C2SPacketOpenEnderpack decoder(FriendlyByteBuf ignored) {
        return new C2SPacketOpenEnderpack();
    }

    public void messageConsumer(Supplier<NetworkEvent.Context> c) {
        System.out.println("consumer");
        ServerPlayer sender = c.get().getSender();
        if (sender == null)
            return;

        System.out.println("getting curio");
        if (CuriosIntegration.curiosLoaded) {
            Optional<SlotResult> res = CuriosApi.getCuriosInventory(sender).resolve().orElseThrow().findFirstCurio(ArcaneItems.ENDERPACK.get());
            if (res.isPresent()) {
                ItemEnderpack.open(res.get().stack(), sender.level(), sender);
                return;
            }
        }

        Optional<ItemStack> slot = sender.getInventory().items.stream().filter(stack -> stack.is(ArcaneItems.ENDERPACK.get())).findFirst();
        slot.ifPresent(stack -> ItemEnderpack.open(stack, sender.level(), sender));
    }
}
