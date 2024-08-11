package martian.arcane.api.spell;

import javax.annotation.Nullable;

public record CastResult(boolean failed, @Nullable IFailureReason reason) {
    public interface IFailureReason {
        String name();
    }

    public enum FailureReason implements IFailureReason {
        SPELL_DISABLED,
        SPELL_OUT_OF_AURA,
        SPELL_WAND_LEVEL_TOO_LOW,
    }

    public static final CastResult
            FAILED = new CastResult(true, null),
            SUCCESS = new CastResult(false, null),
            FAIL_DISABLED = new CastResult(false, FailureReason.SPELL_DISABLED),
            FAIL_OUT_OF_AURA = new CastResult(false, FailureReason.SPELL_OUT_OF_AURA),
            FAIL_WAND_LEVEL_TOO_LOW = new CastResult(false, FailureReason.SPELL_WAND_LEVEL_TOO_LOW);
}
