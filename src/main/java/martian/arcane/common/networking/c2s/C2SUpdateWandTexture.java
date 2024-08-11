package martian.arcane.common.networking.c2s;

import io.netty.buffer.ByteBuf;
import martian.arcane.ArcaneMod;
import martian.arcane.common.item.ItemWand;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record C2SUpdateWandTexture(int slot, ResourceLocation val) implements CustomPacketPayload {
    public static final Type<C2SUpdateWandTexture> TYPE = new Type<>(ArcaneMod.id("update_wand_texture"));
    public static final StreamCodec<ByteBuf, C2SUpdateWandTexture> CODEC = StreamCodec.of(
            (buf, it) -> {
                buf.writeInt(it.slot);
                ResourceLocation.STREAM_CODEC.encode(buf, it.val);
            },
            (buf) -> new C2SUpdateWandTexture(buf.readInt(), ResourceLocation.STREAM_CODEC.decode(buf))
    );

    @Override
    @NotNull
    public Type<C2SUpdateWandTexture> type() {
        return TYPE;
    }

    public static void handler(final C2SUpdateWandTexture payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            ItemStack stack = sender.getInventory().getItem(payload.slot);

            if (stack.getItem() instanceof ItemWand wand) {
                ItemWand.mutateWandData(stack, it -> it.withTexture(payload.val));
                sender.getInventory().setChanged();
            } else {
                ArcaneMod.LOGGER.error("C2SUpdateWandTexture sent but the target item was not an ItemAuraWand");
            }
        });
    }
}
