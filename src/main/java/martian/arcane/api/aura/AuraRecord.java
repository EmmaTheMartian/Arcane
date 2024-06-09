package martian.arcane.api.aura;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record AuraRecord(int maxAura, int aura, boolean canExtract, boolean canInsert) implements IAuraStorage {
    public static final Codec<AuraRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("maxAura").forGetter(AuraRecord::maxAura),
            Codec.INT.fieldOf("currentAura").forGetter(AuraRecord::aura),
            Codec.BOOL.fieldOf("extractable").forGetter(AuraRecord::canExtract),
            Codec.BOOL.fieldOf("insertable").forGetter(AuraRecord::canInsert)
    ).apply(instance, AuraRecord::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AuraRecord> STREAM_CODEC = StreamCodec.of(
            (RegistryFriendlyByteBuf buf, AuraRecord storage) -> {
                buf.writeInt(storage.maxAura);
                buf.writeInt(storage.aura);
                buf.writeBoolean(storage.canExtract);
                buf.writeBoolean(storage.canInsert);
            },
            (RegistryFriendlyByteBuf buf) -> new AuraRecord(buf.readInt(), buf.readInt(), buf.readBoolean(), buf.readBoolean())
    );

    public AuraRecord(IMutableAuraStorage record) {
        this(record.getMaxAura(), record.getAura(), record.canExtract(), record.canInsert());
    }

    public AuraStorage unfreeze() {
        return new AuraStorage(this);
    }

    @Override
    public int getAura() {
        return aura;
    }

    @Override
    public int getMaxAura() {
        return maxAura;
    }
}
