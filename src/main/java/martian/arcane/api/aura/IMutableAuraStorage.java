package martian.arcane.api.aura;

public interface IMutableAuraStorage extends IAuraStorage {
    int getAura();

    boolean canExtract();

    boolean canInsert();

    int getMaxAura();

    void setAura(int value);

    void setMaxAura(int value);

    void setExtractable(boolean value);

    void setInsertable(boolean value);

    void extractAuraFrom(IMutableAuraStorage other, int maxExtract);

    void sendAuraTo(IMutableAuraStorage other, int maxPush);

    int addAura(int value);

    int removeAura(int value);
}
