package martian.arcane.api.capability;

public class AuraStorage implements IAuraStorage {
    protected int maxAura;
    protected int currentAura;
    /* If this storage can have aura extracted from it */
    protected boolean extractable;
    /* If this storage can have aura sent to it */
    protected boolean receivable;

    public AuraStorage(int maxAura, boolean extractable, boolean receivable) {
        this.maxAura = maxAura;
        this.extractable = extractable;
        this.receivable = receivable;
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
    public boolean canReceive() {
        return receivable;
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
    public void setMaxAura(int value) {
        maxAura = value;
    }

    @Override
    public void setExtractable(boolean value) {
        extractable = value;
    }

    @Override
    public void setReceivable(boolean value) {
        receivable = value;
    }

    @Override
    public void extractAuraFrom(IAuraStorage other, int maxExtract) {
        if (!other.canExtract() || getAura() >= getMaxAura()) {
            return;
        }

        int auraUntilFull = getMaxAura() - getAura();
        int auraToExtract = Math.min(other.getAura(), auraUntilFull);
        if (maxExtract > -1) {
            auraToExtract = Math.min(maxExtract, auraToExtract);
        }

        other.setAura(other.getAura() - auraToExtract);
        setAura(getAura() + auraToExtract);
    }

    @Override
    public void sendAuraTo(IAuraStorage other, int maxPush) {
        if (!other.canReceive() || other.getAura() >= other.getMaxAura()) {
            return;
        }

        int auraUntilFull = other.getMaxAura() - other.getAura();
        int auraToPush = Math.min(getAura(), auraUntilFull);
        if (maxPush > -1) {
            auraToPush = Math.min(maxPush, auraToPush);
        }

        setAura(getAura() - auraToPush);
        other.setAura(other.getAura() + auraToPush);
    }

    @Override
    public int addAura(int value) {
        currentAura += value;
        if (currentAura > getMaxAura()) {
            int overflow = currentAura - getMaxAura();
            currentAura = getMaxAura();
            return overflow;
        }
        return 0;
    }

    @Override
    public int removeAura(int value) {
        currentAura -= value;
        if (currentAura < 0) {
            int underflow = Math.abs(currentAura);
            currentAura = 0;
            return underflow;
        }
        return 0;
    }
}
