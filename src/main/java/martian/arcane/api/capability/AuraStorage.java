package martian.arcane.api.capability;

public class AuraStorage implements IAuraStorage {
    protected int currentAura;
    protected boolean extractable = false;
    protected final int maxAura;

    public AuraStorage(int maxAura, boolean extractable) {
        this.maxAura = maxAura;
        this.extractable = extractable;
    }

    @Override
    public int getAura() {
        return currentAura;
    }

    @Override
    public boolean canExtract() {
        return extractable;
    }

    @Override
    public int getMaxAura() {
        return maxAura;
    }

    @Override
    public void setAura(int value) {
        currentAura = value;
    }

    @Override
    public void setExtractable(boolean value) {
        extractable = value;
    }

    @Override
    public void extractAuraFrom(IAuraStorage other) {
        int auraUntilFull = getMaxAura() - getAura();
        int auraToExtract = Math.min(other.getAura(), auraUntilFull);
        other.setAura(other.getAura() - auraToExtract);
        setAura(getAura() + auraToExtract);
    }
}
