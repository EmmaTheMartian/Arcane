package martian.arcane.common.networking.s2c;

import martian.arcane.ArcaneMod;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.api.aura.AuraRecord;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record S2CSyncAuraAttachment(AuraRecord storage, BlockPos pos) implements CustomPacketPayload {
    public static final Type<S2CSyncAuraAttachment> TYPE = new Type<>(ArcaneMod.id("sync_aura_attachment"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CSyncAuraAttachment> CODEC = StreamCodec.of(
            (RegistryFriendlyByteBuf buf, S2CSyncAuraAttachment it) -> {
                AuraRecord.STREAM_CODEC.encode(buf, it.storage);
                buf.writeBlockPos(it.pos);
            },
            (RegistryFriendlyByteBuf buf) -> new S2CSyncAuraAttachment(AuraRecord.STREAM_CODEC.decode(buf), buf.readBlockPos())
    );

    public static void handler(final S2CSyncAuraAttachment payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().level().getBlockEntity(payload.pos) instanceof AbstractAuraBlockEntity be)
                be.setAuraStorage(payload.storage);
        });
    }

    @Override
    public @NotNull Type<S2CSyncAuraAttachment> type() {
        return TYPE;
    }
}
