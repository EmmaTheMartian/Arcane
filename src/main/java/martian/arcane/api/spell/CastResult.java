package martian.arcane.api.spell;

import javax.annotation.Nullable;

public record CastResult(boolean failed, @Nullable String reason) {
    public static final CastResult FAILED = new CastResult(true, null);
    public static final CastResult SUCCESS = new CastResult(false, null);
    public static final CastResult FAIL_DISABLED = new CastResult(false, "Spell is disabled.");
}
