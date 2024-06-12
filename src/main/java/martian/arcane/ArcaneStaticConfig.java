package martian.arcane;

//TODO: Change this to be configurable in the TOML config file or serverdefaults
public final class ArcaneStaticConfig {
    public static final class AuraMaximums {
        public static final int BASIC_WAND = 16;
        public static final int ADVANCED_WAND = 32;
        public static final int MYSTIC_WAND = 64;

        public static final int SMALL_AURAGLASS_BOTTLE = 16;
        public static final int MEDIUM_AURAGLASS_BOTTLE = 32;
        public static final int LARGE_AURAGLASS_BOTTLE = 64;
        public static final int EXTREME_AURAGLASS_BOTTLE = 128;

        public static final int AURA_CONNECTORS = 16;
        public static final int AURA_BASIN = 256;
        public static final int AURA_INFUSER = 32;
        public static final int PEDESTAL = 32;

        public static final int COLLECTOR = 8;

        public static final int SPELL_CIRCLE_BASIC = 8;

        public static final int ENDERPACK = 8;
    }

    public static final class AuraLoss {
        public static final int COPPER_TIER = 4;
        public static final int LARIMAR_TIER = 3;
        public static final int AURACHALCUM_TIER = 2;
    }

    // Rates are per tick unless otherwise specified
    public static final class Rates {
        public static final int COPPER_AURA_CONNECTOR_RATE = 1;
        public static final int LARIMAR_AURA_CONNECTOR_RATE = 2;
        public static final int AURACHALCUM_AURA_CONNECTOR_RATE = 4;

        public static final int SMALL_AURAGLASS_BOTTLE = 1;
        public static final int MEDIUM_AURAGLASS_BOTTLE = 2;
        public static final int LARGE_AURAGLASS_BOTTLE = 2;
        public static final int EXTREME_AURAGLASS_BOTTLE = 4;

        // How often aura loss should occur. If this is set to `1` then machines will be drained as soon as they become
        // idle. Arguably it is hilarious to see an entire Aura network die as soon it does
        public static final int AURA_LOSS_TICKS = 10;  // 0.5s
    }

    // Machine speed is measured in ticks
    public static final class Speed {
        public static final int IGNIS_COLLECTOR_SPEED = 20;  // 1s
        public static final int AQUA_COLLECTOR_SPEED = 40;   // 2s

        public static final int SPELL_CIRCLE_BASIC = 80;     // 4s
    }

    public static final class Consumption {
        public static final int ENDERPACK = 1;
    }

    public static final class SpellMinLevels {
        public static final int BREAKING = 1;
        public static final int BUILDING = 1;
        public static final int CLEANSING = 2;
        public static final int CRAFTING = 1;
        public static final int DASHING = 1;
        public static final int HAMMERING = 2;
        public static final int PRESERVATION = 2;
        public static final int PURIFYING = 2;
        public static final int ACTIVATOR = 2;
    }

    public static final class SpellCosts {
        public static final int BREAKING = 1;  // Per block
        public static final int BUILDING = 1;  // Per block
        public static final int CLEANSING = 2;
        public static final int CRAFTING = 1;
        public static final int DASHING = 2;
        public static final int HAMMERING = 4;
        public static final int PRESERVATION = 4;
        public static final int PURIFYING = 4;
        public static final int ACTIVATOR = 16;
    }

    public static final double AURA_EXTRACTOR_MAX_DISTANCE = 10.0d;  // In blocks
    public static final int TICKS_UNTIL_CONSIDERED_IDLE = 80;  // 4s
}
