package martian.arcane.api.capability;

public interface IAuraStorage {
    int getAura();

    boolean canExtract();

    boolean canReceive();

    int getMaxAura();

    void setAura(int value);

    void setMaxAura(int value);

    void setExtractable(boolean value);

    void setReceivable(boolean value);

    void extractAuraFrom(IAuraStorage other, int maxExtract);

    void sendAuraTo(IAuraStorage other, int maxPush);
}
