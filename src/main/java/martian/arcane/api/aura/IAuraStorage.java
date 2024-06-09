package martian.arcane.api.aura;

public interface IAuraStorage {
    int getAura();

    boolean canExtract();

    boolean canInsert();

    int getMaxAura();
}
