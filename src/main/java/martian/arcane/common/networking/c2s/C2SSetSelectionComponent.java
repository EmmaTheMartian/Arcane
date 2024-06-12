package martian.arcane.common.networking.c2s;

import io.netty.buffer.ByteBuf;
import martian.arcane.ArcaneMod;
import martian.arcane.common.item.ItemWandbook;
import martian.arcane.common.registry.ArcaneDataComponents;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record C2SSetSelectionComponent(int slot, int val) implements CustomPacketPayload {
    public static final Type<C2SSetSelectionComponent> TYPE = new Type<>(ArcaneMod.id("set_selection"));
    public static final StreamCodec<ByteBuf, C2SSetSelectionComponent> CODEC = StreamCodec.of(
            (buf, it) -> {
                buf.writeInt(it.slot);
                buf.writeInt(it.val);
            },
            (buf) -> new C2SSetSelectionComponent(buf.readInt(), buf.readInt())
    );

    @Override
    @NotNull
    public Type<C2SSetSelectionComponent> type() {
        return TYPE;
    }

    public static void handler(final C2SSetSelectionComponent payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            ItemStack stack = sender.getInventory().getItem(payload.slot);

            if (stack.getItem() instanceof ItemWandbook wandbook) {
                wandbook.mutateData(stack, it -> {
                    it.selection = payload.val;
                    return it;
                });
                sender.getInventory().setChanged();
            } else {
                ArcaneMod.LOGGER.error("C2sSetSelectionComponent sent but the target item was not an ItemWandbook");
            }
        });
    }
}
