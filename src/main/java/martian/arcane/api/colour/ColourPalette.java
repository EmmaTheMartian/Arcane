package martian.arcane.api.colour;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import martian.arcane.client.ArcaneClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Arrays;
import java.util.List;

public record ColourPalette(UnpackedColour... colours) {
    public static final Codec<ColourPalette> CODEC = RecordCodecBuilder.create(it -> it.group(
            Codec.list(Codec.INT).xmap(
                    packedColours -> packedColours.stream().map(UnpackedColour::new).toList(),
                    unpackedColours -> unpackedColours.stream().map(UnpackedColour::pack).toList()
            ).fieldOf("colours").forGetter(colourPalette -> Arrays.asList(colourPalette.colours))
    ).apply(it, ColourPalette::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ColourPalette> STREAM_CODEC = StreamCodec.of(
            (buf, it) -> {
                buf.writeInt(it.colours.length);
                for (UnpackedColour colour : it.colours)
                    buf.writeInt(colour.pack());
            },
            buf -> {
                int length = buf.readInt();
                UnpackedColour[] colours = new UnpackedColour[length];
                for (int i = 0; i < length; i++)
                    colours[i] = new UnpackedColour(buf.readInt());
                return new ColourPalette(colours);
            }
    );

    public ColourPalette(List<UnpackedColour> colours) {
        this(colours.toArray(new UnpackedColour[] { }));
    }

    public UnpackedColour getRandom() {
        return colours[ArcaneClient.RANDOM.nextInt(colours.length)];
    }
}
