package martian.arcane.api.capability;

public interface IAuraStorage {
    int getAura();

    boolean canExtract();

    boolean canInsert();

    int getMaxAura();

    void setAura(int value);

    void setMaxAura(int value);

    void setExtractable(boolean value);

    void setInsertable(boolean value);

    void extractAuraFrom(IAuraStorage other, int maxExtract);

    void sendAuraTo(IAuraStorage other, int maxPush);

    int addAura(int value);

    int removeAura(int value);
}
