package martian.arcane;

//TODO: Change this to be configurable in the TOML config file or serverdefaults
public final class ArcaneStaticConfig {
    public static final class Maximums {
        public static final int BASIC_WAND = 16;
        public static final int ADVANCED_WAND = 32;
        public static final int MYSTIC_WAND = 64;

        public static final int SMALL_AURAGLASS_BOTTLE = 16;
        public static final int MEDIUM_AURAGLASS_BOTTLE = 32;
        public static final int LARGE_AURAGLASS_BOTTLE = 64;
        public static final int EXTREME_AURAGLASS_BOTTLE = 128;

        public static final int AURA_EXTRACTOR = 32;
        public static final int AURA_INSERTER = 32;
        public static final int IMPROVED_AURA_EXTRACTOR = 16;
        public static final int IMPROVED_AURA_INSERTER = 16;
        public static final int AURA_BASIN = 512;

        public static final int COLLECTOR_MAX_AURA = 8;

        public static final int SPELL_CIRCLE_BASIC = 8;
        public static final int SPELL_CIRCLE_ADVANCED = 16;
        public static final int SPELL_CIRCLE_MYSTIC = 32;

        public static final int ENDERPACK = 8;
    }

    // Rates are per tick unless otherwise specified
    public static final class Rates {
        public static final int AURA_EXTRACTOR_RATE = 1;
        public static final int AURA_INSERTER_RATE = 1;
        public static final int IMPROVED_AURA_EXTRACTOR_RATE = 4;
        public static final int IMPROVED_AURA_INSERTER_RATE = 4;

        public static final int SMALL_AURAGLASS_BOTTLE = 1;
        public static final int MEDIUM_AURAGLASS_BOTTLE = 2;
        public static final int LARGE_AURAGLASS_BOTTLE = 2;
        public static final int EXTREME_AURAGLASS_BOTTLE = 4;
    }

    // Machine speed is measured in ticks
    public static final class Speed {
        public static final int IGNIS_COLLECTOR_SPEED = 20;  // 1s
        public static final int AQUA_COLLECTOR_SPEED = 40;   // 2s

        public static final int SPELL_CIRCLE_BASIC = 80;     // 4s
        public static final int SPELL_CIRCLE_ADVANCED = 60;  // 3s
        public static final int SPELL_CIRCLE_MYSTIC = 40;    // 2s
    }

    public static final class Consumption {
        public static final int ENDERPACK = 1;
    }
}
