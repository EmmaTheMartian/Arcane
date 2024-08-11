package martian.arcane.client.model;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import martian.arcane.ArcaneMod;
import martian.arcane.api.wand.WandData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.model.*;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.geometry.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.function.Function;

public record DynamicWandModel(ResourceLocation wandTexture) implements IUnbakedGeometry<DynamicWandModel> {
    private static final Map<WandData, BakedModel> cache = Maps.newHashMap();
    private static UnbakedModel unbakedWandModel = null;

    private static final ResourceLocation missing = new ResourceLocation("minecraft:missingno");

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull BakedModel bake(
            IGeometryBakingContext context,
            ModelBaker modelBaker,
            Function<Material, TextureAtlasSprite> spriteGetter,
            ModelState modelState,
            ItemOverrides itemOverrides,
            ResourceLocation modelLocation
    ) {
        if (unbakedWandModel == null) {
            unbakedWandModel = modelBaker.getModel(ArcaneMod.id("item/template_wand"));
        }

        Function<Material, TextureAtlasSprite> betterSpriteGetter = material ->
                spriteGetter.apply(!material.texture().equals(missing) ?
                        material :
                        ClientHooks.getBlockMaterial(wandTexture));
        BakedModel wand = unbakedWandModel.bake(modelBaker, betterSpriteGetter, modelState, modelLocation);
        assert wand != null;
        var builder = CompositeModel.Baked.builder(
                context,
                betterSpriteGetter.apply(context.getMaterial("gem")),
                new OverrideHandler(itemOverrides, modelBaker, context),
                context.getTransforms()
        );
        builder.addLayer(wand);
        builder.setParticle(betterSpriteGetter.apply(context.getMaterial("gem")));
        return builder.build();
    }

    public static final class Loader implements IGeometryLoader<DynamicWandModel> {
        public static final Loader INSTANCE = new Loader();

        private Loader() {
        }

        @Override
        @ParametersAreNonnullByDefault
        public @NotNull DynamicWandModel read(JsonObject json, JsonDeserializationContext context) {
            return new DynamicWandModel(new ResourceLocation("minecraft:block/oak_log"));
        }
    }

    private static final class OverrideHandler extends ItemOverrides {
        private final ItemOverrides nested;
        private final ModelBaker baker;
        private final IGeometryBakingContext owner;

        private OverrideHandler(ItemOverrides nested, ModelBaker baker, IGeometryBakingContext owner) {
            this.nested = nested;
            this.baker = baker;
            this.owner = owner;
        }

        @Override
        @ParametersAreNonnullByDefault
        public BakedModel resolve(BakedModel originalModel, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
            BakedModel overridden = nested.resolve(originalModel, stack, level, entity, seed);
            if (overridden != originalModel)
                return overridden;
            WandData wandData = WandData.getOrCreate(stack, () -> WandData.DEFAULT);
            if (!cache.containsKey(wandData)) {
                DynamicWandModel unbaked = new DynamicWandModel(wandData.stickTexture());
                BakedModel baked = unbaked.bake(owner, baker, Material::sprite, BlockModelRotation.X0_Y0, this, ArcaneMod.id("wand"));
                cache.put(wandData, baked);
                return baked;
            }
            return cache.get(wandData);
        }
    }

    public static final class Builder extends CustomLoaderBuilder<ItemModelBuilder> {
        public Builder(ResourceLocation loaderId, ItemModelBuilder parent, ExistingFileHelper efh, boolean allowInlineElements) {
            super(loaderId, parent, efh, allowInlineElements);
        }
    }
}
