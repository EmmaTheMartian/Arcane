package martian.arcane.api.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record BlockIngredient(Optional<Block> block, Optional<TagKey<Block>> tag) {
    public BlockIngredient(Block block) {
        this(Optional.of(block), Optional.empty());
    }

    public BlockIngredient(TagKey<Block> blockTag) {
        this(Optional.empty(), Optional.of(blockTag));
    }

    public boolean test(BlockState state) {
        return (block.isPresent() && state.is(block.get())) || (tag.isPresent() && state.is(tag.get()));
    }
    
    public Ingredient toNonBlockIngredient() {
        if (block.isPresent()) {
            return Ingredient.of(block.get().asItem());
        } else if (tag.isPresent()) {
            List<ItemLike> blocks = new ArrayList<>();
            BuiltInRegistries.BLOCK.getTag(tag.get()).ifPresent(tagEntries ->
                tagEntries.forEach(entry -> blocks.add(entry.value())));
            return Ingredient.of(blocks.toArray(new ItemLike[]{}));
        }

        throw new UnsupportedOperationException("Cannot invoke BlockIngredient.toNonBlockIngredient where neither block nor tag are present.");
    }

    public BIRL toBIRL() {
        return new BIRL(
                block.map(BuiltInRegistries.BLOCK::getKey),
                tag.map(TagKey::location)
        );
    }

    // Name stands for "Block Ingredient from Resource Location"
    public record BIRL(Optional<ResourceLocation> block, Optional<ResourceLocation> tag) {
        public BlockIngredient toIngredient() {
            return new BlockIngredient(
                    block.map(BuiltInRegistries.BLOCK::get),
                    tag.map(it -> TagKey.create(Registries.BLOCK, it))
            );
        }
    }

    public static final Codec<BIRL> BIRL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("block").forGetter(BIRL::block),
            ResourceLocation.CODEC.optionalFieldOf("tag").forGetter(BIRL::tag)
    ).apply(instance, BIRL::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BIRL> BIRL_STREAM_CODEC = StreamCodec.of(
            (buf, it) -> {
                buf.writeOptional(it.block, ResourceLocation.STREAM_CODEC);
                buf.writeOptional(it.tag, ResourceLocation.STREAM_CODEC);
            },
            buf -> new BIRL(
                    buf.readOptional(ResourceLocation.STREAM_CODEC),
                    buf.readOptional(ResourceLocation.STREAM_CODEC)
            )
    );

    public static final Codec<BlockIngredient> CODEC = BIRL_CODEC.xmap(BIRL::toIngredient, BlockIngredient::toBIRL);
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockIngredient> STREAM_CODEC = BIRL_STREAM_CODEC.map(BIRL::toIngredient, BlockIngredient::toBIRL);
}
