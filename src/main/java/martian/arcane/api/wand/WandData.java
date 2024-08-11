package martian.arcane.api.wand;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import martian.arcane.ArcaneMod;
import martian.arcane.common.ArcaneContent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public record WandData(ResourceLocation stickTexture, int level, ResourceLocation pigment) {
    public static final WandData DEFAULT = new WandData(new ResourceLocation("minecraft:block/oak_log"), 1, ArcaneMod.id("magic"));

    public static final Codec<WandData> CODEC = RecordCodecBuilder.create(it -> it.group(
            ResourceLocation.CODEC.fieldOf("stickTexture").forGetter(WandData::stickTexture),
            Codec.INT.fieldOf("level").forGetter(WandData::level),
            ResourceLocation.CODEC.fieldOf("packedColor").forGetter(WandData::pigment)
    ).apply(it, WandData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, WandData> STREAM_CODEC = StreamCodec.of(
            (buf, it) -> {
                buf.writeResourceLocation(it.stickTexture);
                buf.writeInt(it.level);
                ResourceLocation.STREAM_CODEC.encode(buf, it.pigment);
            },
            buf -> new WandData(buf.readResourceLocation(), buf.readInt(), ResourceLocation.STREAM_CODEC.decode(buf))
    );

    public static WandData getOrCreate(ItemStack stack, Supplier<WandData> defaultDataRecord) {
        if (!stack.has(ArcaneContent.DC_WAND_DATA) || stack.get(ArcaneContent.DC_WAND_DATA) == null)
            stack.set(ArcaneContent.DC_WAND_DATA, defaultDataRecord.get());
        return stack.get(ArcaneContent.DC_WAND_DATA);
    }

    public WandData withTexture(ResourceLocation newStickTexture) {
        return new WandData(newStickTexture, level, pigment);
    }

    public WandData withLevel(int newLevel) {
        return new WandData(stickTexture, newLevel, pigment);
    }

    public WandData withColourPalette(ResourceLocation newPigment) {
        return new WandData(stickTexture, level, newPigment);
    }
}
