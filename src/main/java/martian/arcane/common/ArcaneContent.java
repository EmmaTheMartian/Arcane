package martian.arcane.common;

import com.klikli_dev.modonomicon.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import martian.arcane.ArcaneConfig;
import martian.arcane.ArcaneMod;
import martian.arcane.api.ArcaneRegistries;
import martian.arcane.api.colour.ColourPalette;
import martian.arcane.api.colour.UnpackedColour;
import martian.arcane.api.machine.MachineTier;
import martian.arcane.api.aura.AuraRecord;
import martian.arcane.api.aura.AuraStorage;
import martian.arcane.api.block.BasicLarimarBlock;
import martian.arcane.api.item.AbstractAuraItem;
import martian.arcane.api.recipe.ArcaneRecipeType;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.wand.WandData;
import martian.arcane.common.particle.MagicParticleType;
import martian.arcane.api.wand.WandbookData;
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
import net.minecraft.core.particles.ParticleType;
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
import java.util.Map;
import java.util.function.*;

import static martian.arcane.ArcaneMod.id;

@SuppressWarnings("unused")
public class ArcaneContent {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ArcaneMod.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ArcaneMod.MODID);
    public static final DeferredRegister<AttachmentType<?>> DATA_ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ArcaneMod.MODID);
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.createDataComponents(ArcaneMod.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ArcaneMod.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, ArcaneMod.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, ArcaneMod.MODID);
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(ArcaneRegistries.SPELLS, ArcaneMod.MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArcaneMod.MODID);
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, ArcaneMod.MODID);
    public static final DeferredRegister<ColourPalette> PIGMENTS = DeferredRegister.create(ArcaneRegistries.PIGMENTS, ArcaneMod.MODID);


    // Blocks
    public static final DeferredBlock<?>
        BLOCK_AURAGLASS = block("auraglass", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion())),
        BLOCK_SOUL_MAGMA = block("soul_magma", BlockSoulMagma::new),
        BLOCK_AURA_TORCH = block("aura_torch", () -> new BlockAuraTorch(ParticleHelper.MAGIC_PARTICLE_OPTIONS, BlockBehaviour.Properties.ofFullCopy(Blocks.TORCH).noLootTable())),
        BLOCK_CONJURED_BLOCK = block("conjured_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).instabreak())),
        BLOCK_CONJURED_CRAFTING_TABLE = block("conjured_crafting_table",BlockConjuredCraftingTable::new),

        BLOCK_FADED_LARIMAR_ORE = block("faded_larimar_ore", () -> new BasicLarimarBlock(null, BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE))),
        BLOCK_FADING_LARIMAR_ORE = block("fading_larimar_ore", () -> new BasicLarimarBlock(() -> BLOCK_FADED_LARIMAR_ORE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE))),
        BLOCK_LARIMAR_ORE = block("larimar_ore", () -> new BasicLarimarBlock(() -> BLOCK_FADING_LARIMAR_ORE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE))),
        BLOCK_FADED_DEEPSLATE_LARIMAR_ORE = block("faded_deepslate_larimar_ore", () -> new BasicLarimarBlock(null, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_COPPER_ORE))),
        BLOCK_FADING_DEEPSLATE_LARIMAR_ORE = block("fading_deepslate_larimar_ore", () -> new BasicLarimarBlock(() -> BLOCK_FADED_DEEPSLATE_LARIMAR_ORE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_COPPER_ORE))),
        BLOCK_DEEPSLATE_LARIMAR_ORE = block("deepslate_larimar_ore", () -> new BasicLarimarBlock(() -> BLOCK_FADING_DEEPSLATE_LARIMAR_ORE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_COPPER_ORE))),
        BLOCK_IDOCRASE_ORE = block("idocrase_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_ORE))),
        BLOCK_DEEPSLATE_IDOCRASE_ORE = block("deepslate_idocrase_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_GOLD_ORE))),
        BLOCK_NETHER_IDOCRASE_ORE = block("nether_idocrase_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_GOLD_ORE))),
        BLOCK_BLACKSTONE_IDOCRASE_ORE = block("blackstone_idocrase_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GILDED_BLACKSTONE))),

        BLOCK_FADED_LARIMAR = block("faded_larimar_block", () -> new BasicLarimarBlock(null, BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK))),
        BLOCK_FADING_LARIMAR = block("fading_larimar_block", () -> new BasicLarimarBlock(() -> BLOCK_FADED_LARIMAR.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK))),
        BLOCK_LARIMAR = block("larimar_block", () -> new BasicLarimarBlock(() -> BLOCK_FADING_LARIMAR.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK))),
        BLOCK_AURACHALCUM = block("aurachalcum_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK))),
        BLOCK_FROZEN_OBSIDIAN = block("frozen_obsidian", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN).friction(0.99F)))
    ;


    // Block and Block Entities
    public static final DeferredBlockAndTile<?, BlockEntityAuraConnector> BE_AURA_CONNECTOR = blockWithEntity("aura_connector", BlockAuraConnector::new, block -> () -> buildTile(BlockEntityAuraConnector::new, block.get()));
    public static final DeferredBlockAndTile<?, BlockEntityAuraBasin> BE_AURA_BASIN = blockWithEntity("aura_basin", BlockAuraBasin::new, block -> () -> buildTile(BlockEntityAuraBasin::new, block.get()));
    public static final DeferredBlockAndTile<?, BlockEntityPedestal> BE_PEDESTAL = blockWithEntity("pedestal", BlockPedestal::new, block -> () -> buildTile(BlockEntityPedestal::new, block.get()));
    public static final DeferredBlockAndTile<?, BlockEntityAuraInfuser> BE_AURA_INFUSER = blockWithEntity("aura_infuser", BlockAuraInfuser::new, block -> () -> buildTile(BlockEntityAuraInfuser::new, block.get()));
    public static final DeferredBlockAndTile<?, BlockEntitySpellCircle> BE_SPELL_CIRCLE = blockWithoutItemWithEntity("spell_circle", BlockSpellCircle::new, block -> () -> buildTile(BlockEntitySpellCircle::new, block.get()));
    public static final DeferredBlockAndTile<?, BlockEntityIgnisCollector> BE_HEAT_COLLECTOR = blockWithEntity("heat_collector", BlockIgnisCollector::new, block -> () -> buildTile(BlockEntityIgnisCollector::new, block.get()));
    public static final DeferredBlockAndTile<?, BlockEntityAquaCollector> BE_AQUA_COLLECTOR = blockWithEntity("aqua_collector", BlockAquaCollector::new, block -> () -> buildTile(BlockEntityAquaCollector::new, block.get()));


    // Data Attachments
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<AuraStorage>> DA_AURA = dataAttachment("aura_storage", () ->
            AttachmentType.builder(() -> new AuraStorage(-1, false, false)).serialize(AuraStorage.CODEC).build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<MachineTier>> DA_MACHINE_TIER = dataAttachment("machine_tier", () ->
            AttachmentType.builder(MachineTier.COPPER).serialize(MachineTier.CODEC).build());


    // Data Components
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<AuraRecord>> DC_AURA = dataComponent("aura", AuraRecord.CODEC, AuraRecord.STREAM_CODEC);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<@Nullable ResourceLocation>> DC_SPELL = dataComponent("spell", ResourceLocation.CODEC, ResourceLocation.STREAM_CODEC);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<@Nullable BlockPos>> DC_TARGET_POS = dataComponent("target_pos", BlockPos.CODEC);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> DC_ACTIVE = dataComponent("active", Codec.BOOL);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> DC_PUSH_RATE = dataComponent("push_rate", Codec.INT);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> DC_MODE = dataComponent("mode", Codec.STRING);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<WandbookData>> DC_WANDBOOK_DATA = dataComponent("wandbook_data", WandbookData.CODEC, WandbookData.STREAM_CODEC);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<WandData>> DC_WAND_DATA = dataComponent("wand_data", WandData.CODEC, WandData.STREAM_CODEC);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> DC_COLOUR_PALETTE = dataComponent("colour_palette", ResourceLocation.CODEC, ResourceLocation.STREAM_CODEC);


    // Items
    public static final DeferredItem<?>
            ITEM_AURAGLASS_BOTTLE = item("auraglass_bottle", () -> new ItemAuraglassBottle(() -> ArcaneConfig.smallAuraglassBottleAuraCapacity, () -> ArcaneConfig.smallAuraglassBottleRate)),
            ITEM_MEDIUM_AURAGLASS_BOTTLE = item("medium_auraglass_bottle", () -> new ItemAuraglassBottle(() -> ArcaneConfig.mediumAuraglassBottleAuraCapacity, () -> ArcaneConfig.mediumAuraglassBottleRate)),
            ITEM_LARGE_AURAGLASS_BOTTLE = item("large_auraglass_bottle", () -> new ItemAuraglassBottle(() -> ArcaneConfig.largeAuraglassBottleAuraCapacity, () -> ArcaneConfig.largeAuraglassBottleRate)),
            ITEM_EXTREME_AURAGLASS_BOTTLE = item("extreme_auraglass_bottle", () -> new ItemAuraglassBottle(() -> ArcaneConfig.extremeAuraglassBottleAuraCapacity, () -> ArcaneConfig.extremeAuraglassBottleRate)),
            ITEM_CREATIVE_AURAGLASS_BOTTLE = item("creative_auraglass_bottle", () -> new ItemAuraglassBottle(() -> Integer.MAX_VALUE, () -> Integer.MAX_VALUE)),

            ITEM_WAND = item("wand", () -> new ItemWand(() -> ArcaneConfig.basicWandAuraCapacity, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON))),
            ITEM_WANDBOOK = item("wandbook", ItemWandbook::new),
            ITEM_CHAINWAND = item("chainwand", () -> new ItemChainwand(() -> ArcaneConfig.chainwandSpellCapacity, () -> ArcaneConfig.chainwandAuraCapacity, 3)),

            ITEM_AURAOMETER = item("auraometer", ItemAuraometer::new),
            ITEM_AURA_WRENCH = item("aura_wrench", ItemAuraWrench::new),
            ITEM_AURA_CONFIGURATOR = item("aura_configurator", ItemAuraConfigurator::new),
            ITEM_AURA_MULTITOOL = item("aura_multitool", ItemAuraMultitool::new),
            ITEM_GEM_SAW = item("gem_saw", ItemGemSaw::new),
            ITEM_SPELL_TABLET = item("spell_tablet", ItemSpellTablet::new),
            ITEM_ARCANE_BLEACH = item("arcane_bleach"),
            ITEM_SPELL_CHALK = item("spell_chalk", ItemSpellChalk::new),
            ITEM_ENDERPACK = item("enderpack", ItemEnderpack::new),
            ITEM_AXOBOTTLE = item("axobottle"),
            ITEM_GUIDEBOOK = item("guidebook", ItemGuidebook::new),
            ITEM_UPGRADE_KIT_COPPER = item("upgrade_kit_copper", () -> new ItemUpgradeKit(MachineTier.COPPER)),
            ITEM_UPGRADE_KIT_LARIMAR = item("upgrade_kit_larimar", () -> new ItemUpgradeKit(MachineTier.LARIMAR)),
            ITEM_UPGRADE_KIT_AURACHALCUM = item("upgrade_kit_aurachalcum", () -> new ItemUpgradeKit(MachineTier.AURACHALCUM)),
            ITEM_ARCANE_PIGMENT = item("arcane_pigment", ItemArcanePigment::new),

            ITEM_RAW_AURACHALCUM = item("raw_aurachalcum"),
            ITEM_AURACHALCUM = item("aurachalcum"),
            ITEM_ELDRITCH_ALLOY = item("eldritch_alloy"),
            ITEM_COPPER_CORE = item("copper_core"),
            ITEM_LARIMAR_CORE = item("larimar_core"),
            ITEM_AURACHALCUM_CORE = item("aurachalcum_core"),
            ITEM_ELDRITCH_CORE = item("eldritch_core"),
            ITEM_SPELL_CIRCLE_CORE = item("spell_circle_core"),
            ITEM_AURAGLASS_SHARD = item("auraglass_shard"),
            ITEM_AURAGLASS_DUST = item("auraglass_dust"),

            ITEM_CRUSHED_RAW_COPPER = item("crushed_raw_copper"),
            ITEM_CRUSHED_RAW_IRON = item("crushed_raw_iron"),
            ITEM_CRUSHED_RAW_GOLD = item("crushed_raw_gold"),
            ITEM_PURIFIED_RAW_COPPER = item("purified_raw_copper"),
            ITEM_PURIFIED_RAW_IRON = item("purified_raw_iron"),
            ITEM_PURIFIED_RAW_GOLD = item("purified_raw_gold")
    ;

    public static final DeferredGemItems
            ITEMS_LARIMAR = gemItems("larimar"),
            ITEMS_FADED_LARIMAR = gemItems("faded_larimar"),
            ITEMS_IDOCRASE = gemItems("idocrase");


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
            SPELL_CRAFTING = spell("crafting", () -> SimplePlacementSpell.of(id("crafting"), 1, 20, 1, c -> BLOCK_CONJURED_CRAFTING_TABLE.get().defaultBlockState())),
            SPELL_ACTIVATOR = spell("activator", SpellSpellCircleActivator::new),
            SPELL_PRESERVATION = spell("preservation", SpellPreservation::new),
            SPELL_LIGHTING = spell("lighting", () -> SimplePlacementSpell.of(id("crafting"), 1, 20, 1, c -> BLOCK_AURA_TORCH.get().defaultBlockState())),
            SPELL_MIXING = spell("mixing", SpellMixing::new),
            SPELL_CONJURE_WATER = spell("conjure_water", () -> SimpleLiquidSpell.of(id("conjure_water"), 8, 20, 1,
                    c -> Fluids.WATER,
                    c -> Blocks.WATER.defaultBlockState(),
                    c -> Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3))),
            SPELL_FREEZING = spell("freezing", () -> SimpleCraftingSpell.of(id("freezing"), 4, 20, 1, RT_FREEZING)),
            SPELL_SMELTING = spell("smelting", SpellSmelting::new),
            SPELL_ENLARGING = registerIf(() -> spell("enlarging", SpellEnlarging::new), ArcaneIntegrations.PEHKUI.isLoaded()),
            SPELL_SHRINKING = registerIf(() -> spell("shrinking", SpellShrinking::new), ArcaneIntegrations.PEHKUI.isLoaded()),
            SPELL_DEPLOY_ELYTRA = spell("deploy_elytra", SpellDeployElytra::new)
    ;


    // Creative Tabs
    public static final Supplier<CreativeModeTab> ARCANE_TAB = TABS.register("arcane_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.arcane.arcane_tab"))
            .icon(() -> new ItemStack(ITEM_EXTREME_AURAGLASS_BOTTLE.get()))
            .displayItems((params, output) -> {
                final BiConsumer<ItemStack, UnaryOperator<ItemStack>> add = (stack, unaryOperator) ->
                        output.accept(unaryOperator.apply(stack).copy());

                // Guidebook!
                add.accept(new ItemStack(ITEM_GUIDEBOOK.get()), stack -> {
                    stack.set(DataComponentRegistry.BOOK_ID.get(), id("arcane_guidebook"));
                    return stack;
                });

                // Items
                List<Item> ignoredItems = new ArrayList<>();
                ignoredItems.add(ITEM_GUIDEBOOK.get());

                output.acceptAll(ITEMS
                        .getEntries()
                        .stream()
                        .filter(it -> !ignoredItems.contains(it.get()))
                        .map(it -> it.get().getDefaultInstance())
                        .toList());

                // Other items
                add.accept(new ItemStack(ITEM_CREATIVE_AURAGLASS_BOTTLE.get()), stack -> {
                    ((AbstractAuraItem)stack.getItem()).mutateAuraStorage(stack, aura -> {
                        aura.setAura(Integer.MAX_VALUE);
                        return aura;
                    });
                    return stack;
                });

                add.accept(new ItemStack(ITEM_WAND.get()), stack -> {
                    ItemWand.mutateWandData(stack, data -> data.withLevel(2));
                    return stack;
                });

                add.accept(new ItemStack(ITEM_WAND.get()), stack -> {
                    ItemWand.mutateWandData(stack, data -> data.withLevel(3));
                    return stack;
                });
            })
            .build());

    public static final Supplier<CreativeModeTab> ARCANE_SPELLS_TAB = TABS.register("arcane_spells_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.arcane.arcane_spells_tab"))
            .icon(() -> new ItemStack(ITEM_SPELL_TABLET.get()))
            .withTabsBefore(id("arcane_tab"))
            .withSearchBar()
            .displayItems((params, output) -> {
                ArcaneRegistries.SPELLS
                        .entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByKey())
                        .forEach(entry -> {
                            ItemStack stack = ITEM_SPELL_TABLET.get().getDefaultInstance();
                            ItemSpellTablet.setSpell(entry.getKey().location(), stack);
                            output.accept(stack);
                        });

                ArcaneRegistries.PIGMENTS
                        .entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByKey())
                        .forEach(entry -> {
                            ItemStack stack = ITEM_ARCANE_PIGMENT.get().getDefaultInstance();
                            stack.set(DC_COLOUR_PALETTE, entry.getKey().location());
                            output.accept(stack);
                        });
            })
            .build());


    // Particle Types
    public static final DeferredHolder<ParticleType<?>, MagicParticleType> PARTICLE_TYPE_MAGIC = PARTICLE_TYPES.register("magic", () -> MagicParticleType.INSTANCE);


    // Pigments
    public static final DeferredHolder<ColourPalette, ColourPalette>
            PIGMENT_MAGIC = PIGMENTS.register("magic", () -> new ColourPalette(
                    new UnpackedColour(240, 107, 146),
                    new UnpackedColour(237, 128, 160),
                    new UnpackedColour(240, 142, 192),
                    new UnpackedColour(244, 162, 215),
                    new UnpackedColour(240, 174, 217),
                    new UnpackedColour(244, 194, 227),
                    new UnpackedColour(248, 215, 237)
            )),
            PIGMENT_PRIDE = PIGMENTS.register("pride", () -> new ColourPalette(
                    new UnpackedColour(228,   3,   3),
                    new UnpackedColour(255, 140,   0),
                    new UnpackedColour(255, 237,   0),
                    new UnpackedColour(  0, 128,  38),
                    new UnpackedColour(  0,  76, 255),
                    new UnpackedColour(115,  41, 130)
            )),
            PIGMENT_TRANS = PIGMENTS.register("trans", () -> new ColourPalette(
                new UnpackedColour( 91, 206, 250),
                new UnpackedColour(245, 169, 184),
                new UnpackedColour(255, 255, 255)
            ))
    ;


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
        DATA_ATTACHMENTS.register(bus);
        DATA_COMPONENTS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
        RECIPE_SERIALIZERS.register(bus);
        RECIPE_TYPES.register(bus);
        TABS.register(bus);
        SPELLS.register(bus);
        PARTICLE_TYPES.register(bus);
        PIGMENTS.register(bus);
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
