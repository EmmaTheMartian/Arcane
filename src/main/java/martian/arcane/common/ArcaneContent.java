package martian.arcane.common;

import com.google.common.collect.ImmutableList;
import com.klikli_dev.modonomicon.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import martian.arcane.ArcaneConfig;
import martian.arcane.ArcaneMod;
import martian.arcane.api.ArcaneRegistries;
import martian.arcane.api.MachineTier;
import martian.arcane.api.aura.AuraRecord;
import martian.arcane.api.aura.AuraStorage;
import martian.arcane.api.block.BasicLarimarBlock;
import martian.arcane.api.item.AbstractAuraItem;
import martian.arcane.api.recipe.ArcaneRecipeType;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.common.spell.SimpleCraftingSpell;
import martian.arcane.api.spell.WandbookDataRecord;
import martian.arcane.client.ParticleHelper;
import martian.arcane.common.block.BlockAuraTorch;
import martian.arcane.common.block.BlockConjuredCraftingTable;
import martian.arcane.common.block.BlockSoulMagma;
import martian.arcane.common.block.basin.BlockAuraBasin;
import martian.arcane.common.block.basin.BlockEntityAuraBasin;
import martian.arcane.common.block.connector.BlockAuraConnector;
import martian.arcane.common.block.connector.BlockEntityAuraConnector;
import martian.arcane.common.block.generators.heat.BlockEntityIgnisCollector;
import martian.arcane.common.block.generators.heat.BlockIgnisCollector;
import martian.arcane.common.block.generators.water.BlockAquaCollector;
import martian.arcane.common.block.generators.water.BlockEntityAquaCollector;
import martian.arcane.common.block.infuser.BlockAuraInfuser;
import martian.arcane.common.block.infuser.BlockEntityAuraInfuser;
import martian.arcane.common.block.pedestal.BlockEntityPedestal;
import martian.arcane.common.block.pedestal.BlockPedestal;
import martian.arcane.common.block.spellcircle.BlockEntitySpellCircle;
import martian.arcane.common.block.spellcircle.BlockSpellCircle;
import martian.arcane.common.item.*;
import martian.arcane.common.recipe.*;
import martian.arcane.common.spell.*;
import martian.arcane.integration.ArcaneIntegrations;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static martian.arcane.ArcaneMod.id;

@SuppressWarnings("unused")
public class ArcaneContent {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ArcaneMod.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ArcaneMod.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ArcaneMod.MODID);
    public static final DeferredRegister<AttachmentType<?>> DATA_ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ArcaneMod.MODID);
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.createDataComponents(ArcaneMod.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, ArcaneMod.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, ArcaneMod.MODID);
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(ArcaneRegistries.SPELLS, ArcaneMod.MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArcaneMod.MODID);


    // Blocks
    public static final DeferredBlock<?>
        AURAGLASS = block("auraglass", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion())),
        SOUL_MAGMA = block("soul_magma", BlockSoulMagma::new),
        AURA_TORCH = block("aura_torch", () -> new BlockAuraTorch(ParticleHelper.MAGIC_PARTICLE_OPTIONS, BlockBehaviour.Properties.ofFullCopy(Blocks.TORCH).noLootTable())),
        CONJURED_BLOCK = block("conjured_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).instabreak())),
        CONJURED_CRAFTING_TABLE = block("conjured_crafting_table",BlockConjuredCraftingTable::new),

        FADED_LARIMAR_ORE = block("faded_larimar_ore", () -> new BasicLarimarBlock(null, BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE))),
        FADING_LARIMAR_ORE = block("fading_larimar_ore", () -> new BasicLarimarBlock(() -> FADED_LARIMAR_ORE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE))),
        LARIMAR_ORE = block("larimar_ore", () -> new BasicLarimarBlock(() -> FADING_LARIMAR_ORE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE))),
        FADED_DEEPSLATE_LARIMAR_ORE = block("faded_deepslate_larimar_ore", () -> new BasicLarimarBlock(null, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_COPPER_ORE))),
        FADING_DEEPSLATE_LARIMAR_ORE = block("fading_deepslate_larimar_ore", () -> new BasicLarimarBlock(() -> FADED_DEEPSLATE_LARIMAR_ORE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_COPPER_ORE))),
        DEEPSLATE_LARIMAR_ORE = block("deepslate_larimar_ore", () -> new BasicLarimarBlock(() -> FADING_DEEPSLATE_LARIMAR_ORE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_COPPER_ORE))),
        IDOCRASE_ORE = block("idocrase_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_ORE))),
        DEEPSLATE_IDOCRASE_ORE = block("deepslate_idocrase_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_GOLD_ORE))),
        NETHER_IDOCRASE_ORE = block("nether_idocrase_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_GOLD_ORE))),
        BLACKSTONE_IDOCRASE_ORE = block("blackstone_idocrase_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GILDED_BLACKSTONE))),

        FADED_LARIMAR_BLOCK = block("faded_larimar_block", () -> new BasicLarimarBlock(null, BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK))),
        FADING_LARIMAR_BLOCK = block("fading_larimar_block", () -> new BasicLarimarBlock(() -> FADED_LARIMAR_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK))),
        LARIMAR_BLOCK = block("larimar_block", () -> new BasicLarimarBlock(() -> FADING_LARIMAR_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK))),
        AURACHALCUM_BLOCK = block("aurachalcum_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK))),
        FROZEN_OBSIDIAN_BLOCK = block("frozen_obsidian", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN).friction(0.99F)))
    ;


    // Block and Block Entities
    public static final DeferredBlockAndTile<?, BlockEntityAuraConnector> AURA_CONNECTOR = blockWithEntity("aura_connector", BlockAuraConnector::new, block -> () -> buildTile(BlockEntityAuraConnector::new, block.get()));
    public static final DeferredBlockAndTile<?, BlockEntityAuraBasin> AURA_BASIN = blockWithEntity("aura_basin", BlockAuraBasin::new, block -> () -> buildTile(BlockEntityAuraBasin::new, block.get()));
    public static final DeferredBlockAndTile<?, BlockEntityPedestal> PEDESTAL = blockWithEntity("pedestal", BlockPedestal::new, block -> () -> buildTile(BlockEntityPedestal::new, block.get()));
    public static final DeferredBlockAndTile<?, BlockEntityAuraInfuser> AURA_INFUSER = blockWithEntity("aura_infuser", BlockAuraInfuser::new, block -> () -> buildTile(BlockEntityAuraInfuser::new, block.get()));
    public static final DeferredBlockAndTile<?, BlockEntitySpellCircle> SPELL_CIRCLE = blockWithoutItemWithEntity("spell_circle", BlockSpellCircle::new, block -> () -> buildTile(BlockEntitySpellCircle::new, block.get()));
    public static final DeferredBlockAndTile<?, BlockEntityIgnisCollector> HEAT_COLLECTOR = blockWithEntity("heat_collector", BlockIgnisCollector::new, block -> () -> buildTile(BlockEntityIgnisCollector::new, block.get()));
    public static final DeferredBlockAndTile<?, BlockEntityAquaCollector> AQUA_COLLECTOR = blockWithEntity("aqua_collector", BlockAquaCollector::new, block -> () -> buildTile(BlockEntityAquaCollector::new, block.get()));


    // Items
    private static final Supplier<Item> BASIC_WAND_SUPPLIER = () -> new ItemAuraWand(() -> ArcaneConfig.basicWandAuraCapacity, 1, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    private static final Supplier<Item> ADVANCED_WAND_SUPPLIER = () -> new ItemAuraWand(() -> ArcaneConfig.advancedWandAuraCapacity, 2, new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
    private static final Supplier<Item> MYSTICAL_WAND_SUPPLIER = () -> new ItemAuraWand(() -> ArcaneConfig.mysticWandAuraCapacity, 3, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

    public static final DeferredItem<?>
            AURAGLASS_BOTTLE = item("auraglass_bottle", () -> new ItemAuraglassBottle(() -> ArcaneConfig.smallAuraglassBottleAuraCapacity, () -> ArcaneConfig.smallAuraglassBottleRate)),
            MEDIUM_AURAGLASS_BOTTLE = item("medium_auraglass_bottle", () -> new ItemAuraglassBottle(() -> ArcaneConfig.mediumAuraglassBottleAuraCapacity, () -> ArcaneConfig.mediumAuraglassBottleRate)),
            LARGE_AURAGLASS_BOTTLE = item("large_auraglass_bottle", () -> new ItemAuraglassBottle(() -> ArcaneConfig.largeAuraglassBottleAuraCapacity, () -> ArcaneConfig.largeAuraglassBottleRate)),
            EXTREME_AURAGLASS_BOTTLE = item("extreme_auraglass_bottle", () -> new ItemAuraglassBottle(() -> ArcaneConfig.extremeAuraglassBottleAuraCapacity, () -> ArcaneConfig.extremeAuraglassBottleRate)),
            CREATIVE_AURAGLASS_BOTTLE = item("creative_auraglass_bottle", () -> new ItemAuraglassBottle(() -> Integer.MAX_VALUE, () -> Integer.MAX_VALUE)),

            WAND_ACACIA = item("wand_acacia", BASIC_WAND_SUPPLIER),
            WAND_BAMBOO = item("wand_bamboo", BASIC_WAND_SUPPLIER),
            WAND_BIRCH = item("wand_birch", BASIC_WAND_SUPPLIER),
            WAND_CHERRY = item("wand_cherry", BASIC_WAND_SUPPLIER),
            WAND_DARK_OAK = item("wand_dark_oak", BASIC_WAND_SUPPLIER),
            WAND_JUNGLE = item("wand_jungle", BASIC_WAND_SUPPLIER),
            WAND_MANGROVE = item("wand_mangrove", BASIC_WAND_SUPPLIER),
            WAND_OAK = item("wand_oak", BASIC_WAND_SUPPLIER),
            WAND_SPRUCE = item("wand_spruce", BASIC_WAND_SUPPLIER),
            WAND_WARPED = item("wand_warped", BASIC_WAND_SUPPLIER),
            WAND_CRIMSON = item("wand_crimson", BASIC_WAND_SUPPLIER),
            WAND_COPPER = item("wand_copper", BASIC_WAND_SUPPLIER),
            WAND_LARIMAR = item("wand_larimar", ADVANCED_WAND_SUPPLIER),
            WAND_AURACHALCUM = item("wand_aurachalcum", MYSTICAL_WAND_SUPPLIER),
            WAND_ELDRITCH = item("wand_eldritch", MYSTICAL_WAND_SUPPLIER),
            WANDBOOK = item("wandbook", ItemWandbook::new),

            AURAOMETER = item("auraometer", ItemAuraometer::new),
            AURA_WRENCH = item("aura_wrench", ItemAuraWrench::new),
            AURA_CONFIGURATOR = item("aura_configurator", ItemAuraConfigurator::new),
            AURA_MULTITOOL = item("aura_multitool", ItemAuraMultitool::new),
            GEM_SAW = item("gem_saw", ItemGemSaw::new),
            SPELL_TABLET = item("spell_tablet", ItemSpellTablet::new),
            ARCANE_BLEACH = item("arcane_bleach"),
            SPELL_CHALK = item("spell_chalk", ItemSpellChalk::new),
            ENDERPACK = item("enderpack", ItemEnderpack::new),
            AXOBOTTLE = item("axobottle"),
            GUIDEBOOK = item("guidebook", ItemGuidebook::new),
            UPGRADE_KIT_COPPER = item("upgrade_kit_copper", () -> new ItemUpgradeKit(MachineTier.COPPER)),
            UPGRADE_KIT_LARIMAR = item("upgrade_kit_larimar", () -> new ItemUpgradeKit(MachineTier.LARIMAR)),
            UPGRADE_KIT_AURACHALCUM = item("upgrade_kit_aurachalcum", () -> new ItemUpgradeKit(MachineTier.AURACHALCUM)),

            RAW_AURACHALCUM = item("raw_aurachalcum"),
            AURACHALCUM = item("aurachalcum"),
            ELDRITCH_ALLOY = item("eldritch_alloy"),
            COPPER_CORE = item("copper_core"),
            LARIMAR_CORE = item("larimar_core"),
            AURACHALCUM_CORE = item("aurachalcum_core"),
            ELDRITCH_CORE = item("eldritch_core"),
            SPELL_CIRCLE_CORE = item("spell_circle_core"),
            AURAGLASS_SHARD = item("auraglass_shard"),
            AURAGLASS_DUST = item("auraglass_dust"),

            CRUSHED_RAW_COPPER = item("crushed_raw_copper"),
            CRUSHED_RAW_IRON = item("crushed_raw_iron"),
            CRUSHED_RAW_GOLD = item("crushed_raw_gold"),
            PURIFIED_RAW_COPPER = item("purified_raw_copper"),
            PURIFIED_RAW_IRON = item("purified_raw_iron"),
            PURIFIED_RAW_GOLD = item("purified_raw_gold")
    ;

    public static final DeferredGemItems
            LARIMAR = gemItems("larimar"),
            FADED_LARIMAR = gemItems("faded_larimar"),
            IDOCRASE = gemItems("idocrase");

    public static final ImmutableList<DeferredItem<?>> WANDS = ImmutableList.of(
            WAND_ACACIA,
            WAND_BAMBOO,
            WAND_BIRCH,
            WAND_CHERRY,
            WAND_DARK_OAK,
            WAND_JUNGLE,
            WAND_MANGROVE,
            WAND_OAK,
            WAND_SPRUCE,
            WAND_WARPED,
            WAND_CRIMSON,
            WAND_COPPER,
            WAND_LARIMAR,
            WAND_AURACHALCUM,
            WAND_ELDRITCH
    );


    // Data Attachments
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<AuraStorage>> DA_AURA = dataAttachment("aura_storage", () ->
            AttachmentType.builder(() -> new AuraStorage(-1, false, false)).serialize(AuraStorage.CODEC).build());
    public static DeferredHolder<AttachmentType<?>, AttachmentType<MachineTier>> DA_MACHINE_TIER = dataAttachment("machine_tier", () ->
            AttachmentType.builder(MachineTier.COPPER).serialize(MachineTier.CODEC).build());


    // Data Components
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<AuraRecord>> DC_AURA = dataComponent("aura", AuraRecord.CODEC, AuraRecord.STREAM_CODEC);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<@Nullable ResourceLocation>> DC_SPELL = dataComponent("spell", ResourceLocation.CODEC, ResourceLocation.STREAM_CODEC);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<@Nullable BlockPos>> DC_TARGET_POS = dataComponent("target_pos", BlockPos.CODEC);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> DC_ACTIVE = dataComponent("active", Codec.BOOL);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> DC_PUSH_RATE = dataComponent("push_rate", Codec.INT);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> DC_MODE = dataComponent("mode", Codec.STRING);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<WandbookDataRecord>> DC_WANDBOOK_DATA = dataComponent("wandbook_data", WandbookDataRecord.CODEC, WandbookDataRecord.STREAM_CODEC);


    // Recipe Types
    public static final RecipeAuraInfusionType RT_AURA_INFUSION = recipeTypeAndSerializer("aura_infusion", RecipeAuraInfusion.TYPE);
    public static final RecipePedestalType RT_PEDESTAL = recipeTypeAndSerializer("pedestal", RecipePedestalCrafting.TYPE);
    public static final RecipeCauldronMixingType RT_CAULDRON_MIXING = recipeTypeAndSerializer("cauldron_mixing", RecipeCauldronMixing.TYPE);
    public static final SpellRecipeType
            RT_HAMMERING = recipeTypeAndSerializer("hammering", new SpellRecipeType()),
            RT_CLEANSING = recipeTypeAndSerializer("cleansing", new SpellRecipeType()),
            RT_PURIFYING = recipeTypeAndSerializer("purifying", new SpellRecipeType()),
            RT_FREEZING = recipeTypeAndSerializer("freezing", new SpellRecipeType());


    // Spells
    public static final DeferredHolder<AbstractSpell, ?>
            SPELL_BREAKING = spell("breaking", SpellBreaking::new),
            SPELL_HAMMERING = spell("hammering", () -> SimpleCraftingSpell.of(id("hammering"), 4, 20, 2, RT_HAMMERING)),
            SPELL_PURIFYING = spell("purifying", () -> SimpleCraftingSpell.of(id("purifying"), 4, 20, 2, RT_PURIFYING)),
            SPELL_CLEANSING = spell("cleansing", () -> SimpleCraftingSpell.of(id("cleansing"), 2, 20, 2, RT_CLEANSING)),
            SPELL_BUILDING = spell("building", SpellBuilding::new),
            SPELL_DASHING = spell("dashing", SpellDashing::new),
            SPELL_CRAFTING = spell("crafting", () -> SimplePlacementSpell.of(id("crafting"), 1, 20, 1, c -> CONJURED_CRAFTING_TABLE.get().defaultBlockState())),
            SPELL_ACTIVATOR = spell("activator", SpellSpellCircleActivator::new),
            SPELL_PRESERVATION = spell("preservation", SpellPreservation::new),
            SPELL_LIGHTING = spell("lighting", () -> SimplePlacementSpell.of(id("crafting"), 1, 20, 1, c -> AURA_TORCH.get().defaultBlockState())),
            SPELL_MIXING = spell("mixing", SpellMixing::new),
            SPELL_CONJURE_WATER = spell("conjure_water", () -> SimpleLiquidSpell.of(id("conjure_water"), 8, 20, 1,
                    c -> Fluids.WATER,
                    c -> Blocks.WATER.defaultBlockState(),
                    c -> Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3))),
            SPELL_FREEZING = spell("freezing", () -> SimpleCraftingSpell.of(id("freezing"), 4, 20, 1, RT_FREEZING)),
            SPELL_SMELTING = spell("smelting", SpellSmelting::new),
            SPELL_ENLARGING = registerIf(() -> spell("enlarging", SpellEnlarging::new), ArcaneIntegrations.PEHKUI.isLoaded()),
            SPELL_SHRINKING = registerIf(() -> spell("shrinking", SpellShrinking::new), ArcaneIntegrations.PEHKUI.isLoaded())
    ;


    // Creative Tabs
    public static final Supplier<CreativeModeTab> ARCANE_TAB = TABS.register("arcane_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.arcane.arcane_tab"))
            .icon(() -> new ItemStack(EXTREME_AURAGLASS_BOTTLE.get()))
            .displayItems((params, output) -> {
                // Guidebook!
                ItemStack stack = new ItemStack(GUIDEBOOK.get());
                stack.set(DataComponentRegistry.BOOK_ID.get(), id("arcane_guidebook"));
                output.accept(stack.copy());

                // Items
                List<Item> ignoredItems = new ArrayList<>();
                ignoredItems.add(GUIDEBOOK.get());

                output.acceptAll(ITEMS
                        .getEntries()
                        .stream()
                        .filter(it -> !ignoredItems.contains(it.get()))
                        .map(it -> it.get().getDefaultInstance())
                        .toList());

                // Other items
                stack = new ItemStack(CREATIVE_AURAGLASS_BOTTLE.get());
                ((AbstractAuraItem)stack.getItem()).mutateAuraStorage(stack, aura -> {
                    aura.setAura(Integer.MAX_VALUE);
                    return aura;
                });
                output.accept(stack.copy());
            })
            .build());

    public static final Supplier<CreativeModeTab> ARCANE_SPELLS_TAB = TABS.register("arcane_spells_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.arcane.arcane_spells_tab"))
            .icon(() -> new ItemStack(SPELL_TABLET.get()))
            .withTabsBefore(id("arcane_tab"))
            .displayItems((params, output) -> {
                ArcaneRegistries.SPELLS.entrySet().forEach(entry -> {
                    ItemStack stack = SPELL_TABLET.get().getDefaultInstance();
                    ItemSpellTablet.setSpell(entry.getKey().location(), stack);
                    output.accept(stack);
                });
            })
            .build());


    // Helpers
    private static <T extends Block> DeferredBlock<T> block(String id, Supplier<T> block) {
        var reg = BLOCKS.register(id, block);
        blockItem(id, reg);
        return reg;
    }

    private static <T extends Block> DeferredBlock<T> blockWithoutItem(String id, Supplier<T> block) {
        return BLOCKS.register(id, block);
    }

    private static <BT extends Block, TT extends BlockEntity> DeferredBlockAndTile<BT, TT> blockWithEntity(String id, Supplier<BT> block, Function<DeferredBlock<BT>, Supplier<BlockEntityType<TT>>> tile) {
        var b = block(id, block);
        return new DeferredBlockAndTile<>(b, tile(id, tile.apply(b)));
    }

    private static <BT extends Block, TT extends BlockEntity> DeferredBlockAndTile<BT, TT> blockWithoutItemWithEntity(String id, Supplier<BT> block, Function<DeferredBlock<BT>, Supplier<BlockEntityType<TT>>> tile) {
        var b = blockWithoutItem(id, block);
        return new DeferredBlockAndTile<>(b, tile(id, tile.apply(b)));
    }

    private static <T extends BlockEntity> BlockEntityType<T> buildTile(BlockEntityType.BlockEntitySupplier<T> sup, Block... blocks) {
        //noinspection DataFlowIssue
        return BlockEntityType.Builder.of(sup, blocks).build(null);
    }

    private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> tile(String name, Supplier<BlockEntityType<T>> sup) {
        return BLOCK_ENTITIES.register(name, sup);
    }

    private static <T extends Item> DeferredItem<T> item(String id, Supplier<T> item) {
        return ITEMS.register(id, item);
    }

    private static DeferredItem<Item> item(String id, Item.Properties properties) {
        return item(id, () -> new Item(properties));
    }

    private static DeferredItem<Item> item(String id) {
        return item(id, new Item.Properties());
    }

    private static DeferredItem<BlockItem> blockItem(String id, DeferredBlock<?> block) {
        return ITEMS.register(id, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static DeferredGemItems gemItems(String id) {
        return new DeferredGemItems(
                item("rough_" + id),
                item("smooth_" + id),
                item("sandy_polished_" + id),
                item("polished_" + id),
                item("exquisite_" + id)
        );
    }

    private static <T> DeferredHolder<AttachmentType<?>, AttachmentType<T>> dataAttachment(String name, Supplier<AttachmentType<T>> func) {
        return DATA_ATTACHMENTS.register(name, func);
    }

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> dataComponent(String name, Supplier<DataComponentType<T>> func) {
        return DATA_COMPONENTS.register(name, func);
    }

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> dataComponent(String name, UnaryOperator<DataComponentType.Builder<T>> func) {
        return DATA_COMPONENTS.register(name, () -> func.apply(DataComponentType.builder()).build());
    }

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> dataComponent(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return dataComponent(name, builder -> builder.persistent(codec).networkSynchronized(streamCodec));
    }

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> dataComponent(String name, Codec<T> codec) {
        return dataComponent(name, builder -> builder.persistent(codec));
    }

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> dataComponent(String name, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        return dataComponent(name, builder -> builder.networkSynchronized(codec));
    }

    private static <T extends ArcaneRecipeType<?>> T recipeTypeAndSerializer(String name, T it) {
        RECIPE_SERIALIZERS.register(name, () -> it);
        RECIPE_TYPES.register(name, () -> it);
        return it;
    }

    private static <T extends AbstractSpell> DeferredHolder<AbstractSpell, T> spell(String name, Supplier<T> spell) {
        return SPELLS.register(name, spell);
    }


    private static <T> @Nullable T registerIf(Supplier<T> thing, boolean condition) {
        if (condition)
            return thing.get();
        return null;
    }

    public static void init(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
        DATA_ATTACHMENTS.register(bus);
        DATA_COMPONENTS.register(bus);
        RECIPE_SERIALIZERS.register(bus);
        RECIPE_TYPES.register(bus);
        TABS.register(bus);
        SPELLS.register(bus);
    }


    public record DeferredBlockAndTile<BlockT extends Block, TileT extends BlockEntity>(
            DeferredBlock<BlockT> block,
            DeferredHolder<BlockEntityType<?>, BlockEntityType<TileT>> tile
    ) { }

    public record DeferredGemItems(
            DeferredItem<Item> rough,
            DeferredItem<Item> smooth,
            DeferredItem<Item> sandyPolished,
            DeferredItem<Item> polished,
            DeferredItem<Item> exquisite
    ) { }
}
