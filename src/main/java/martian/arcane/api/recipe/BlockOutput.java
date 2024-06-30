package martian.arcane.api.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public record BlockOutput(Block block) {
    public BORL toBORL() {
        return new BORL(BuiltInRegistries.BLOCK.getKey(block));
    }

    // Stands for "Block Output Resource Location"
    public record BORL(ResourceLocation block) {
        public BlockOutput toBlockOutput() {
            return new BlockOutput(BuiltInRegistries.BLOCK.get(block));
        }
    }

    public static final Codec<BORL> BORL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("block").forGetter(BORL::block)
    ).apply(instance, BORL::new));

    public static final StreamCodec<FriendlyByteBuf, BORL> BORL_STREAM_CODEC = StreamCodec.of(
            (buf, it) -> buf.writeResourceLocation(it.block),
            buf -> new BORL(buf.readResourceLocation())
    );

    public static final Codec<BlockOutput> CODEC = BORL_CODEC.xmap(BORL::toBlockOutput, BlockOutput::toBORL);
    public static final StreamCodec<FriendlyByteBuf, BlockOutput> STREAM_CODEC = BORL_STREAM_CODEC.map(BORL::toBlockOutput, BlockOutput::toBORL);
}
