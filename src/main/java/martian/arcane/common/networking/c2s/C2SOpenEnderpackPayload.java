package martian.arcane.common.networking.c2s;

import io.netty.buffer.ByteBuf;
import martian.arcane.ArcaneMod;
import martian.arcane.common.ArcaneContent;
import martian.arcane.common.item.ItemEnderpack;
import martian.arcane.integration.curios.CuriosIntegration;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

public record C2SOpenEnderpackPayload() implements CustomPacketPayload {
    public static final Type<C2SOpenEnderpackPayload> TYPE = new Type<>(ArcaneMod.id("open_enderpack"));
    public static final StreamCodec<ByteBuf, C2SOpenEnderpackPayload> CODEC = StreamCodec.unit(new C2SOpenEnderpackPayload());

    @Override
    @NotNull
    public Type<C2SOpenEnderpackPayload> type() {
        return TYPE;
    }

    public static void handler(final C2SOpenEnderpackPayload ignoredPayload, final IPayloadContext context) {
        Player sender = context.player();

        if (CuriosIntegration.INSTANCE.isLoaded()) {
            var optionalInv = CuriosApi.getCuriosInventory(sender);
            if (optionalInv.isPresent()) {
                var inv = optionalInv.get();
                var res = inv.findFirstCurio(ArcaneContent.ENDERPACK.get());
                if (res.isPresent()) {
                    ItemEnderpack.open(res.get().stack(), sender.level(), sender);
                    return;
                }
            }
        }

        Optional<ItemStack> slot = sender.getInventory().items.stream().filter(stack -> stack.is(ArcaneContent.ENDERPACK)).findFirst();
        slot.ifPresent(stack -> ItemEnderpack.open(stack, sender.level(), sender));
    }
}
