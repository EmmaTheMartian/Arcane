package martian.arcane.api.spell;

public interface ICastingSource {
    enum Type {
        WAND,
        SPELL_CIRCLE
    }

    Type getType();
}
