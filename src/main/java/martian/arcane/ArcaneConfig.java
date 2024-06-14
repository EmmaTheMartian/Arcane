package martian.arcane;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.neoforged.neoforge.common.ModConfigSpec.*;

@EventBusSubscriber(modid = ArcaneMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ArcaneConfig {
    public static final Builder BUILDER = new Builder();

    // General config
    private static final IntValue TICKS_UNTIL_CONSIDERED_IDLE = BUILDER
            .comment("How many ticks should pass until an Aura machine with no activity is considered idle. Use -1 to disable aura loss entirely.")
            .defineInRange("ticksUntilConsideredIdle", 80, -1, Integer.MAX_VALUE);

    private static final IntValue AURA_LOSS_TICKS = BUILDER
            .comment("How often Aura loss should occur once a machine is idle.", "Setting this to `1` will cause machines to be drained very fast once they go idle.", "Arguably it is hilarious to see an entire Aura network collapse as soon as it goes idle.")
            .defineInRange("auraLossTicks", 10, 1, Integer.MAX_VALUE);

    private static final DoubleValue AURA_CONNECTOR_MAX_DISTANCE = BUILDER
            .comment("The maximum distance two Aura Connectors can be in blocks. Use -1 to allow infinite range.", "Note that infinite range means it would be VERY easy for someone to crash a server due to how links check for specific blocks (aurachalcum, copper, and larimar blocks).", "I recommend keeping this value below around 32.")
            .defineInRange("auraConnectorMaxDistance", 10.0d, -1, Double.MAX_VALUE);

    //todo
    private static final ConfigValue<List<? extends String>> DISABLED_SPELLS = BUILDER
            .comment("A list of spells by their ID that should be disabled. These spells will not be removed however they will be blacklisted from crafting and cannot be casted.")
            .defineListAllowEmpty("disabledSpells", List.of(), it -> it instanceof String str && ResourceLocation.isValidResourceLocation(str));

    // Aura maximums
    private static final IntValue
            BASIC_WAND_AURA_CAPACITY = BUILDER
                    .defineInRange("itemAuraCapacities.basicWand", 16, 1, Integer.MAX_VALUE),
            ADVANCED_WAND_AURA_CAPACITY = BUILDER
                    .defineInRange("itemAuraCapacities.advancedWand", 32, 1, Integer.MAX_VALUE),
            MYSTIC_WAND_AURA_CAPACITY = BUILDER
                    .defineInRange("itemAuraCapacities.mysticWand", 64, 1, Integer.MAX_VALUE),
            WANDBOOK_AURA_CAPACITY = BUILDER
                    .defineInRange("itemAuraCapacities.wandbook", 64, 1, Integer.MAX_VALUE),
            SMALL_AURAGLASS_BOTTLE_AURA_CAPACITY = BUILDER
                    .defineInRange("itemAuraCapacities.smallAuraglassBottle", 16, 1, Integer.MAX_VALUE),
            MEDIUM_AURAGLASS_BOTTLE_AURA_CAPACITY = BUILDER
                    .defineInRange("itemAuraCapacities.mediumAuraglassBottle", 32, 1, Integer.MAX_VALUE),
            LARGE_AURAGLASS_BOTTLE_AURA_CAPACITY = BUILDER
                    .defineInRange("itemAuraCapacities.largeAuraglassBottle", 64, 1, Integer.MAX_VALUE),
            EXTREME_AURAGLASS_BOTTLE_AURA_CAPACITY = BUILDER
                    .defineInRange("itemAuraCapacities.extremeAuraglassBottle", 128, 1, Integer.MAX_VALUE),
            ENDERPACK_AURA_CAPACITY = BUILDER
                    .defineInRange("itemAuraCapacities.enderpack", 16, 1, Integer.MAX_VALUE),
            AURA_CONNECTORS_AURA_CAPACITY = BUILDER
                    .defineInRange("blockAuraCapacities.auraConnectors", 16, 1, Integer.MAX_VALUE),
            AURA_BASIN_AURA_CAPACITY = BUILDER
                    .defineInRange("blockAuraCapacities.auraBasin", 256, 1, Integer.MAX_VALUE),
            AURA_INFUSER_AURA_CAPACITY = BUILDER
                    .defineInRange("blockAuraCapacities.auraInfuser", 32, 1, Integer.MAX_VALUE),
            PEDESTAL_AURA_CAPACITY = BUILDER
                    .defineInRange("blockAuraCapacities.pedestal", 16, 1, Integer.MAX_VALUE),
            COLLECTORS_AURA_CAPACITY = BUILDER
                    .defineInRange("blockAuraCapacities.collectors", 8, 1, Integer.MAX_VALUE),
            SPELL_CIRCLE_AURA_CAPACITY = BUILDER
                    .defineInRange("blockAuraCapacities.spellCircle", 8, 1, Integer.MAX_VALUE)
    ;

    // Tier configs
    private static final IntValue
            COPPER_TIER_AURA_LOSS = BUILDER
                    .comment("Aura loss for machines at copper tier.")
                    .defineInRange("tiers.copper.auraLoss", 4, 0, Integer.MAX_VALUE),
            LARIMAR_TIER_AURA_LOSS = BUILDER
                    .comment("Aura loss for machines at larimar tier.")
                    .defineInRange("tiers.larimar.auraLoss", 3, 0, Integer.MAX_VALUE),
            AURACHALCUM_TIER_AURA_LOSS = BUILDER
                    .comment("Aura loss for machines at aurachalcum tier.")
                    .defineInRange("tiers.aurachalcum.auraLoss", 2, 0, Integer.MAX_VALUE),
            COPPER_TIER_CONNECTOR_RATE = BUILDER
                    .comment("How much Aura to transfer per Aura Connector operation at copper tier.")
                    .defineInRange("tiers.copper.connectorRate", 1, 0, Integer.MAX_VALUE),
            LARIMAR_TIER_CONNECTOR_RATE = BUILDER
                    .comment("How much Aura to transfer per Aura Connector operation at larimar tier.")
                    .defineInRange("tiers.larimar.connectorRate", 2, 0, Integer.MAX_VALUE),
            AURACHALCUM_TIER_CONNECTOR_RATE = BUILDER
                    .comment("How much Aura to transfer per Aura Connector operation at aurachalcum tier.")
                    .defineInRange("tiers.aurachalcum.connectorRate", 2, 0, Integer.MAX_VALUE);
    private static final DoubleValue
            COPPER_TIER_MAX_AURA_MULTIPLIER = BUILDER
                    .comment("Maximum Aura multiplier for machines at copper tier.")
                    .defineInRange("tiers.copper.maxAuraMultiplier", 1d, 0, Double.MAX_VALUE),
            LARIMAR_TIER_MAX_AURA_MULTIPLIER = BUILDER
                    .comment("Maximum Aura multiplier for machines at larimar tier.")
                    .defineInRange("tiers.larimar.maxAuraMultiplier", 2d, 0, Double.MAX_VALUE),
            AURACHALCUM_TIER_MAX_AURA_MULTIPLIER = BUILDER
                    .comment("Maximum Aura multiplier for machines at aurachalcum tier.")
                    .defineInRange("tiers.aurachalcum.maxAuraMultiplier", 4d, 0, Double.MAX_VALUE)
    ;

    // Auraglass bottle transfer rates
    private static final IntValue
            SMALL_AURAGLASS_BOTTLE_RATE = BUILDER
                    .comment("How much Aura a Small Auraglass Bottle can transfer per operation.")
                    .defineInRange("auraglassBottleRates.small", 1, 1, Integer.MAX_VALUE),
            MEDIUM_AURAGLASS_BOTTLE_RATE = BUILDER
                    .comment("How much Aura a Medium Auraglass Bottle can transfer per operation.")
                    .defineInRange("auraglassBottleRates.medium", 2, 1, Integer.MAX_VALUE),
            LARGE_AURAGLASS_BOTTLE_RATE = BUILDER
                    .comment("How much Aura a Large Auraglass Bottle can transfer per operation.")
                    .defineInRange("auraglassBottleRates.large", 2, 1, Integer.MAX_VALUE),
            EXTREME_AURAGLASS_BOTTLE_RATE = BUILDER
                    .comment("How much Aura a Extreme Auraglass Bottle can transfer per operation.")
                    .defineInRange("auraglassBottleRates.extreme", 4, 1, Integer.MAX_VALUE);


    // Machine speed
    private static final IntValue
            IGNIS_COLLECTOR_SPEED = BUILDER
                    .comment("How often in ticks Heat Collectors should produce Aura if able.")
                    .defineInRange("machineSpeeds.ignisCollector", 20, 0, Integer.MAX_VALUE),
            AQUA_COLLECTOR_SPEED = BUILDER
                    .comment("How often in ticks Aqua Collectors should produce Aura if able.")
                    .defineInRange("machineSpeeds.aquaCollector", 40, 0, Integer.MAX_VALUE),
            BASIC_SPELL_CIRCLE_SPEED = BUILDER
                    .comment("How often in ticks Basic Spell Circles should attempt to cast their spell.")
                    .defineInRange("machineSpeeds.basicSpellCircle", 80, 0, Integer.MAX_VALUE);


    // Non-spell Aura costs
    private static final IntValue
            ENDERPACK_USAGE_AURA_COST = BUILDER
                    .comment("Enderpack usage Aura cost.")
                    .defineInRange("auraCosts.enderpack", 1, 0, Integer.MAX_VALUE);


    // Misc
    private static final IntValue WANDBOOK_SPELL_CAPACITY = BUILDER
            .comment("Maximum spells a Wandbook can hold. Changing this will not change existing Wandbooks.")
            .defineInRange("misc.wandbookSpellCapacity", 4, 1, Integer.MAX_VALUE);


    // Spec
    static final ModConfigSpec SPEC = BUILDER.build();

    public static Set<ResourceLocation> disabledSpells;
    public static int
            ticksUntilConsideredIdle,
            auraLossTicks,
            basicWandAuraCapacity,
            advancedWandAuraCapacity,
            mysticWandAuraCapacity,
            wandbookAuraCapacity,
            smallAuraglassBottleAuraCapacity,
            mediumAuraglassBottleAuraCapacity,
            largeAuraglassBottleAuraCapacity,
            extremeAuraglassBottleAuraCapacity,
            enderpackAuraCapacity,
            auraConnectorsAuraCapacity,
            auraBasinAuraCapacity,
            auraInfuserAuraCapacity,
            pedestalAuraCapacity,
            collectorsAuraCapacity,
            spellCircleAuraCapacity,
            copperTierAuraLoss,
            copperTierConnectorRate,
            larimarTierAuraLoss,
            larimarTierConnectorRate,
            aurachalcumTierAuraLoss,
            aurachalcumTierConnectorRate,
            smallAuraglassBottleRate,
            mediumAuraglassBottleRate,
            largeAuraglassBottleRate,
            extremeAuraglassBottleRate,
            ignisCollectorSpeed,
            aquaCollectorSpeed,
            basicSpellCircleSpeed,
            enderpackUsageAuraCost,
            wandbookSpellCapacity;
    public static double
            auraConnectorMaxDistance,
            copperTierMaxAuraMultiplier,
            larimarTierMaxAuraMultiplier,
            aurachalcumTierMaxAuraMultiplier;


    @SuppressWarnings("unused")
    @SubscribeEvent
    static void onLoad(final ModConfigEvent ignoredEvent) {
        ticksUntilConsideredIdle = TICKS_UNTIL_CONSIDERED_IDLE.get();
        auraLossTicks = AURA_LOSS_TICKS.get();
        auraConnectorMaxDistance = AURA_CONNECTOR_MAX_DISTANCE.get();
        disabledSpells = DISABLED_SPELLS.get().stream().map(ResourceLocation::new).collect(Collectors.toUnmodifiableSet());

        basicWandAuraCapacity = BASIC_WAND_AURA_CAPACITY.get();
        advancedWandAuraCapacity = ADVANCED_WAND_AURA_CAPACITY.get();
        mysticWandAuraCapacity = MYSTIC_WAND_AURA_CAPACITY.get();
        wandbookAuraCapacity = WANDBOOK_AURA_CAPACITY.get();
        smallAuraglassBottleAuraCapacity = SMALL_AURAGLASS_BOTTLE_AURA_CAPACITY.get();
        mediumAuraglassBottleAuraCapacity = MEDIUM_AURAGLASS_BOTTLE_AURA_CAPACITY.get();
        largeAuraglassBottleAuraCapacity = LARGE_AURAGLASS_BOTTLE_AURA_CAPACITY.get();
        extremeAuraglassBottleAuraCapacity = EXTREME_AURAGLASS_BOTTLE_AURA_CAPACITY.get();
        enderpackAuraCapacity = ENDERPACK_AURA_CAPACITY.get();
        auraConnectorsAuraCapacity = AURA_CONNECTORS_AURA_CAPACITY.get();
        auraBasinAuraCapacity = AURA_BASIN_AURA_CAPACITY.get();
        auraInfuserAuraCapacity = AURA_INFUSER_AURA_CAPACITY.get();
        pedestalAuraCapacity = PEDESTAL_AURA_CAPACITY.get();
        collectorsAuraCapacity = COLLECTORS_AURA_CAPACITY.get();
        spellCircleAuraCapacity = SPELL_CIRCLE_AURA_CAPACITY.get();

        copperTierAuraLoss = COPPER_TIER_AURA_LOSS.get();
        copperTierConnectorRate = COPPER_TIER_CONNECTOR_RATE.get();
        copperTierMaxAuraMultiplier = COPPER_TIER_MAX_AURA_MULTIPLIER.get();
        larimarTierAuraLoss = LARIMAR_TIER_AURA_LOSS.get();
        larimarTierConnectorRate = LARIMAR_TIER_CONNECTOR_RATE.get();
        larimarTierMaxAuraMultiplier = LARIMAR_TIER_MAX_AURA_MULTIPLIER.get();
        aurachalcumTierAuraLoss = AURACHALCUM_TIER_AURA_LOSS.get();
        aurachalcumTierConnectorRate = AURACHALCUM_TIER_CONNECTOR_RATE.get();
        aurachalcumTierMaxAuraMultiplier = AURACHALCUM_TIER_MAX_AURA_MULTIPLIER.get();

        smallAuraglassBottleRate = SMALL_AURAGLASS_BOTTLE_RATE.get();
        mediumAuraglassBottleRate = MEDIUM_AURAGLASS_BOTTLE_RATE.get();
        largeAuraglassBottleRate = LARGE_AURAGLASS_BOTTLE_RATE.get();
        extremeAuraglassBottleRate = EXTREME_AURAGLASS_BOTTLE_RATE.get();

        ignisCollectorSpeed = IGNIS_COLLECTOR_SPEED.get();
        aquaCollectorSpeed = AQUA_COLLECTOR_SPEED.get();
        basicSpellCircleSpeed = BASIC_SPELL_CIRCLE_SPEED.get();

        enderpackUsageAuraCost = ENDERPACK_USAGE_AURA_COST.get();

        wandbookSpellCapacity = WANDBOOK_SPELL_CAPACITY.get();
    }
}
