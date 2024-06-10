package martian.arcane.api.spell;

public record CastResult(boolean failed) {
    public static CastResult FAILED = new CastResult(true);
    public static CastResult SUCCESS = new CastResult(false);
}
