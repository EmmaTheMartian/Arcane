package martian.arcane.api.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MutableWandbookData {
    public static final Codec<MutableWandbookData> CODEC = RecordCodecBuilder.create(it -> it.group(
            ItemStack.OPTIONAL_CODEC.listOf().fieldOf("wands").forGetter(o -> o.wands),
            Codec.INT.fieldOf("selection").forGetter(o -> o.selection),
            Codec.INT.fieldOf("maxWands").forGetter(o -> o.maxWands)
    ).apply(it, MutableWandbookData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MutableWandbookData> STREAM_CODEC = StreamCodec.of(
            (buf, it) -> {
                buf.writeInt(it.wands.size());
                it.wands.forEach(wand -> ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, wand));
                buf.writeInt(it.selection);
                buf.writeInt(it.maxWands);
            },
            buf -> {
                int wandCount = buf.readInt();
                List<ItemStack> wands = new ArrayList<>();
                for (int i = 0; i < wandCount; i++) {
                    wands.add(ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
                }
                return new MutableWandbookData(wands, buf.readInt(), buf.readInt());
            }
    );

    public List<ItemStack> wands;
    public int selection;
    public int maxWands;

    public MutableWandbookData(List<ItemStack> wands, int selection, int maxWands) {
        this.wands = wands;
        this.selection = selection;
        this.maxWands = maxWands;
    }

    public WandbookDataRecord freeze() {
        return new WandbookDataRecord(wands, selection, maxWands);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (MutableWandbookData) obj;
        return Objects.equals(this.wands, that.wands) &&
                this.selection == that.selection &&
                this.maxWands == that.maxWands;
    }

    @Override
    public int hashCode() {
        return Objects.hash(wands, selection, maxWands);
    }

    @Override
    public String toString() {
        return "MutableWandbookData[" +
                "wands=" + wands + ", " +
                "selection=" + selection + ", " +
                "maxWands=" + maxWands + ']';
    }
}
