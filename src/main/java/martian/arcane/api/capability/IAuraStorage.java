package martian.arcane.api.capability;

public interface IAuraStorage {
    int getAura();

    boolean canExtract();

    int getMaxAura();

    void setAura(int value);

    void setExtractable(boolean value);

    void extractAuraFrom(IAuraStorage other);

    //TODO
//    void onExtractAura(int amountExtracted);
//
//    void onReceiveAura(int amountReceived);
}
