package martian.arcane.api.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import martian.arcane.common.ArcaneContent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record WandbookDataRecord(List<ItemStack> wands, int selection, int maxWands) {
    public static final Codec<WandbookDataRecord> CODEC = RecordCodecBuilder.create(it -> it.group(
            ItemStack.OPTIONAL_CODEC.listOf().fieldOf("wands").forGetter(WandbookDataRecord::wands),
            Codec.INT.fieldOf("selection").forGetter(WandbookDataRecord::selection),
            Codec.INT.fieldOf("maxWands").forGetter(WandbookDataRecord::maxWands)
    ).apply(it, WandbookDataRecord::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, WandbookDataRecord> STREAM_CODEC = StreamCodec.of(
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
                return new WandbookDataRecord(wands, buf.readInt(), buf.readInt());
            }
    );

    public WandbookDataRecord(int maxWands) {
        this(blankItemStackList(maxWands), 0, maxWands);
    }

    public MutableWandbookData unfreeze() {
        return new MutableWandbookData(wands, selection, maxWands);
    }

    private static ArrayList<ItemStack> blankItemStackList(int size) {
        ArrayList<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < size; i++)
            list.add(ItemStack.EMPTY);
        return list;
    }

    public static WandbookDataRecord getOrCreate(ItemStack stack, Supplier<WandbookDataRecord> defaultDataRecord) {
        if (!stack.has(ArcaneContent.DC_WANDBOOK_DATA))
            stack.set(ArcaneContent.DC_WANDBOOK_DATA, defaultDataRecord.get());
        return stack.get(ArcaneContent.DC_WANDBOOK_DATA);
    }
}
