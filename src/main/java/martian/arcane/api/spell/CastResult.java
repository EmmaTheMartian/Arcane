package martian.arcane.api.spell;

public record CastResult(int auraToConsume, boolean failed) {
    public static CastResult FAILED = new CastResult(0, true);
    public static CastResult PASS = new CastResult(0, false);
}
