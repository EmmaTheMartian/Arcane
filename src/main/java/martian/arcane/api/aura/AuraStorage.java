package martian.arcane.api.aura;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import martian.arcane.common.ArcaneContent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class AuraStorage implements IMutableAuraStorage {
    public static final Codec<AuraStorage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("maxAura").forGetter(AuraStorage::getMaxAura),
            Codec.INT.fieldOf("currentAura").forGetter(AuraStorage::getAura),
            Codec.BOOL.fieldOf("extractable").forGetter(AuraStorage::canExtract),
            Codec.BOOL.fieldOf("insertable").forGetter(AuraStorage::canInsert)
    ).apply(instance, AuraStorage::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AuraStorage> STREAM_CODEC = StreamCodec.of(
            (RegistryFriendlyByteBuf buf, AuraStorage storage) -> {
                buf.writeInt(storage.getMaxAura());
                buf.writeInt(storage.getAura());
                buf.writeBoolean(storage.canExtract());
                buf.writeBoolean(storage.canInsert());
            },
            (RegistryFriendlyByteBuf buf) -> new AuraStorage(buf.readInt(), buf.readInt(), buf.readBoolean(), buf.readBoolean())
    );

    protected int maxAura;
    protected int currentAura;
    /* If this storage can have aura extracted from it */
    protected boolean extractable;
    /* If this storage can have aura sent to it */
    protected boolean insertable;

    public AuraStorage(int maxAura, boolean extractable, boolean insertable) {
        this(maxAura, 0, extractable, insertable);
    }

    public AuraStorage(int maxAura, int currentAura, boolean extractable, boolean insertable) {
        this.maxAura = maxAura;
        this.currentAura = currentAura;
        this.extractable = extractable;
        this.insertable = insertable;
    }

    public AuraStorage(IAuraStorage record) {
        this(record.getMaxAura(), record.getAura(), record.canExtract(), record.canInsert());
    }

    @Override
    public int getAura() {
        return currentAura;
    }

    @Override
    public boolean canExtract() {
        return extractable;
    }

    @Override
    public boolean canInsert() {
        return insertable;
    }

    @Override
    public int getMaxAura() {
        return maxAura;
    }

    @Override
    public void setAura(int value) {
        currentAura = value;
    }

    @Override
    public void setMaxAura(int value) {
        maxAura = value;
    }

    @Override
    public void setExtractable(boolean value) {
        extractable = value;
    }

    @Override
    public void setInsertable(boolean value) {
        insertable = value;
    }

    @Override
    public void extractAuraFrom(IMutableAuraStorage other, int maxExtract) {
        if (!other.canExtract() || getAura() >= getMaxAura()) {
            return;
        }

        int auraUntilFull = getMaxAura() - getAura();
        int auraToExtract = Math.min(other.getAura(), auraUntilFull);
        if (maxExtract > -1) {
            auraToExtract = Math.min(maxExtract, auraToExtract);
        }

        other.setAura(other.getAura() - auraToExtract);
        setAura(getAura() + auraToExtract);
    }

    @Override
    public void sendAuraTo(IMutableAuraStorage other, int maxPush) {
        if (!other.canInsert() || other.getAura() >= other.getMaxAura()) {
            return;
        }

        int auraUntilFull = other.getMaxAura() - other.getAura();
        int auraToPush = Math.min(getAura(), auraUntilFull);
        if (maxPush > -1) {
            auraToPush = Math.min(maxPush, auraToPush);
        }

        setAura(getAura() - auraToPush);
        other.setAura(other.getAura() + auraToPush);
    }

    @Override
    public int addAura(int value) {
        currentAura += value;
        if (currentAura > getMaxAura()) {
            int overflow = currentAura - getMaxAura();
            currentAura = getMaxAura();
            return overflow;
        }
        return 0;
    }

    @Override
    public int removeAura(int value) {
        currentAura -= value;
        if (currentAura < 0) {
            int underflow = Math.abs(currentAura);
            currentAura = 0;
            return underflow;
        }
        return 0;
    }

    public AuraRecord freeze() {
        return new AuraRecord(maxAura, currentAura, extractable, insertable);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof AuraStorage aura) {
            return freeze().equals(aura.freeze());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return freeze().hashCode();
    }

    public static AuraRecord getOrCreate(ItemStack stack, Supplier<AuraRecord> defaultAuraStorage) {
        if (!stack.has(ArcaneContent.DC_AURA))
            stack.set(ArcaneContent.DC_AURA, defaultAuraStorage.get());
        return stack.get(ArcaneContent.DC_AURA);
    }
}
