package martian.arcane.api.wand;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import martian.arcane.ArcaneMod;
import martian.arcane.common.ArcaneContent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record WandbookData(List<ItemStack> wands, int selection, int maxWands, ResourceLocation pigment) {
    public static final Codec<WandbookData> CODEC = RecordCodecBuilder.create(it -> it.group(
            ItemStack.OPTIONAL_CODEC.listOf().fieldOf("wands").forGetter(WandbookData::wands),
            Codec.INT.fieldOf("selection").forGetter(WandbookData::selection),
            Codec.INT.fieldOf("maxWands").forGetter(WandbookData::maxWands),
            ResourceLocation.CODEC.fieldOf("pigment").forGetter(WandbookData::pigment)
    ).apply(it, WandbookData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, WandbookData> STREAM_CODEC = StreamCodec.of(
            (buf, it) -> {
                buf.writeInt(it.wands.size());
                it.wands.forEach(wand -> ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, wand));
                buf.writeInt(it.selection);
                buf.writeInt(it.maxWands);
                buf.writeResourceLocation(it.pigment);
            },
            buf -> {
                int wandCount = buf.readInt();
                List<ItemStack> wands = new ArrayList<>();
                for (int i = 0; i < wandCount; i++) {
                    wands.add(ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
                }
                return new WandbookData(wands, buf.readInt(), buf.readInt(), buf.readResourceLocation());
            }
    );

    public WandbookData(int maxWands) {
        this(blankItemStackList(maxWands), 0, maxWands, ArcaneMod.id("magic"));
    }

    public WandbookData withWands(List<ItemStack> newWands) {
        return new WandbookData(newWands, selection, maxWands, pigment);
    }

    public WandbookData withSelection(int newSelection) {
        return new WandbookData(wands, newSelection, maxWands, pigment);
    }

    public WandbookData withMaxWands(int newMaxWands) {
        return new WandbookData(wands, selection, newMaxWands, pigment);
    }

    public WandbookData withPigment(ResourceLocation newPigment) {
        return new WandbookData(wands, selection, maxWands, newPigment);
    }

    private static ArrayList<ItemStack> blankItemStackList(int size) {
        ArrayList<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < size; i++)
            list.add(ItemStack.EMPTY);
        return list;
    }

    public static WandbookData getOrCreate(ItemStack stack, Supplier<WandbookData> defaultDataRecord) {
        if (!stack.has(ArcaneContent.DC_WANDBOOK_DATA) || stack.get(ArcaneContent.DC_WANDBOOK_DATA) == null)
            stack.set(ArcaneContent.DC_WANDBOOK_DATA, defaultDataRecord.get());
        return stack.get(ArcaneContent.DC_WANDBOOK_DATA);
    }
}
