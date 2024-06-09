package martian.arcane.datagen.util;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RecipeDataHelper {
    private final RecipeOutput writer;
    private final String modid;
    // If true, recipes that would have an ID of- for example- "minecraft:stone" would become "arcane:minecraft/stone"
    // to prevent the generated data from having a folder for "minecraft" too. This applies to all namespaces, not just
    // the "minecraft" namespace.
    public boolean moveNamespacesToModid = true;
    public RecipeCategory defaultCategory;
    // Used to add prefixes to recipe IDs based on their provider.
    public Map<Class<?>, String> prefixesByProvider = new HashMap<>();

    public RecipeDataHelper(RecipeOutput writer, String modid, RecipeCategory defaultCategory) {
        this.writer = writer;
        this.modid = modid;
        this.defaultCategory = defaultCategory;
    }

    // Shaped
    public BuilderWrapper<ShapedRecipeBuilder> shaped(Item output, int count) {
        return new BuilderWrapper<>(new ShapedRecipeBuilder(defaultCategory, output, count), this);
    }

    public BuilderWrapper<ShapedRecipeBuilder> shaped(Item output) {
        return shaped(output, 1);
    }

    // Shapeless
    public BuilderWrapper<ShapelessRecipeBuilder> shapeless(Item output, int count) {
        return new BuilderWrapper<>(new ShapelessRecipeBuilder(defaultCategory, output, count), this);
    }

    public BuilderWrapper<ShapelessRecipeBuilder> shapeless(Item output) {
        return shapeless(output, 1);
    }

    public BuilderWrapper<ShapelessRecipeBuilder> shapeless(Item output, int count, Map<Ingredient, Integer> items) {
        return shapeless(output, count).requires(items);
    }

    public BuilderWrapper<ShapelessRecipeBuilder> shapeless(Item output, Map<Ingredient, Integer> items) {
        return shapeless(output, 1, items);
    }

    public void swappable(Item a, Item b) {
        shapeless(a).unlockedWith(b).requires(b).save(itemKey(a).withSuffix("_swappable"));
        shapeless(b).unlockedWith(a).requires(a).save(itemKey(b).withSuffix("_swappable"));
    }

    // Others
    public <B extends RecipeBuilder> BuilderWrapper<B> wrap(B provider) {
        return new BuilderWrapper<>(provider, this);
    }

    public static class BuilderWrapper<T extends RecipeBuilder> {
        private final T builder;
        private final RecipeDataHelper helper;

        public BuilderWrapper(T builder, RecipeDataHelper helper) {
            this.builder = builder;
            this.helper = helper;
        }

        public T getBuilder() {
            return builder;
        }

        public void save(ResourceLocation id) {
            String namespace = id.getNamespace();
            String path = id.getPath();

            // Prefix depending on RecipeProvider, if applicable
            if (helper.prefixesByProvider.containsKey(builder.getClass()))
                path = helper.prefixesByProvider.get(builder.getClass()) + "/" + path;

            // Prefix with the mod ID to help prevent conflicts and keep generated data clean
            if (helper.moveNamespacesToModid && !id.getNamespace().equals(helper.modid)) {
                path = id.getNamespace() + "/" + path;
                namespace = helper.modid;
            }

            builder.save(helper.writer, new ResourceLocation(namespace, path));
        }

        public void save(String id) {
            save(new ResourceLocation(helper.modid, id));
        }

        public void save() {
            save(RecipeDataHelper.itemKey(builder.getResult()));
        }

        public BuilderWrapper<T> unlockedWith(Item item) {
            builder.unlockedBy("has_item", has(item));
            return this;
        }

        // ShapedRecipeBuilder helpers
        public BuilderWrapper<T> pattern(String a, String b, String c) {
            assert builder instanceof ShapedRecipeBuilder;
            ((ShapedRecipeBuilder) builder).pattern(a).pattern(b).pattern(c);
            return this;
        }

        public BuilderWrapper<T> pattern(String a, String b) {
            assert builder instanceof ShapedRecipeBuilder;
            ((ShapedRecipeBuilder) builder).pattern(a).pattern(b);
            return this;
        }

        public BuilderWrapper<T> pattern(String a) {
            assert builder instanceof ShapedRecipeBuilder;
            ((ShapedRecipeBuilder) builder).pattern(a);
            return this;
        }

        public BuilderWrapper<T> define(char ch, Ingredient item) {
            assert builder instanceof ShapedRecipeBuilder;
            ((ShapedRecipeBuilder) builder).define(ch, item);
            return this;
        }

        public BuilderWrapper<T> define(char ch, TagKey<Item> item) {
            assert builder instanceof ShapedRecipeBuilder;
            ((ShapedRecipeBuilder) builder).define(ch, item);
            return this;
        }

        public BuilderWrapper<T> define(Map<Character, Ingredient> defs) {
            assert builder instanceof ShapedRecipeBuilder;
            ShapedRecipeBuilder shapelessBuilder = (ShapedRecipeBuilder) builder;
            defs.forEach(shapelessBuilder::define);
            return this;
        }

        // ShapelessRecipeBuilder helpers
        public BuilderWrapper<T> requires(Item item, int count) {
            assert builder instanceof ShapelessRecipeBuilder;
            ((ShapelessRecipeBuilder) builder).requires(item, count);
            return this;
        }

        public BuilderWrapper<T> requires(Item item) {
            return requires(item, 1);
        }

        public BuilderWrapper<T> requires(TagKey<Item> item, int count) {
            assert builder instanceof ShapelessRecipeBuilder;
            ((ShapelessRecipeBuilder) builder).requires(Ingredient.of(item), count);
            return this;
        }

        public BuilderWrapper<T> requires(Map<Ingredient, Integer> items) {
            assert builder instanceof ShapelessRecipeBuilder;
            ShapelessRecipeBuilder shapelessBuilder = (ShapelessRecipeBuilder) builder;
            items.forEach(shapelessBuilder::requires);
            return this;
        }
    }

    public static ResourceLocation itemKey(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    public static Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike item) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(new ItemLike[]{item}).build());
    }

    public static Criterion<InventoryChangeTrigger.TriggerInstance> has(TagKey<Item> tag) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(tag).build());
    }

    public static Criterion<InventoryChangeTrigger.TriggerInstance> inventoryTrigger(ItemPredicate... predicates) {
        return CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of(predicates)));
    }
}
