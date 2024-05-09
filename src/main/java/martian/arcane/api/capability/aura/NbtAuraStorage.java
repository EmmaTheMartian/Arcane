package martian.arcane.api.capability.aura;

import martian.arcane.api.NBTHelpers;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class NbtAuraStorage extends AuraStorage {
    private final Supplier<CompoundTag> nbtSupplier;

    public NbtAuraStorage(Supplier<CompoundTag> nbtSupplier, int maxAura, boolean extractable, boolean acceptable) {
        super(maxAura, extractable, acceptable);
        this.nbtSupplier = nbtSupplier;
    }

    private @NotNull CompoundTag getNbt() {
        CompoundTag nbt = nbtSupplier.get();
        if (!nbt.contains(NBTHelpers.KEY_AURA))
            nbt.putInt(NBTHelpers.KEY_AURA, 0);
        if (!nbt.contains(NBTHelpers.KEY_AURA_EXTRACTABLE))
            nbt.putBoolean(NBTHelpers.KEY_AURA_EXTRACTABLE, this.extractable);
        if (!nbt.contains(NBTHelpers.KEY_AURA_INSERTABLE))
            nbt.putBoolean(NBTHelpers.KEY_AURA_INSERTABLE, this.insertable);
        return nbt;
    }

    @Override
    public int getAura() {
        return getNbt().getInt(NBTHelpers.KEY_AURA);
    }

    @Override
    public boolean canExtract() {
        return getNbt().getBoolean(NBTHelpers.KEY_AURA_EXTRACTABLE);
    }

    @Override
    public boolean canInsert() {
        return getNbt().getBoolean(NBTHelpers.KEY_AURA_INSERTABLE);
    }

    @Override
    public void setAura(int value) {
        getNbt().putInt(NBTHelpers.KEY_AURA, value);
    }

    @Override
    public void setMaxAura(int value) {
        getNbt().putInt(NBTHelpers.KEY_MAX_AURA, value);
    }

    @Override
    public void setExtractable(boolean value) {
        getNbt().putBoolean(NBTHelpers.KEY_AURA_EXTRACTABLE, value);
    }

    @Override
    public void setInsertable(boolean value) {
        getNbt().putBoolean(NBTHelpers.KEY_AURA_INSERTABLE, value);
    }

    @Override
    public int addAura(int value) {
        setAura(getAura() + value);
        int aura = getAura();
        if (aura > getMaxAura()) {
            int overflow = aura - getMaxAura();
            setAura(getMaxAura());
            return overflow;
        }
        return 0;
    }

    @Override
    public int removeAura(int value) {
        setAura(getAura() - value);
        int aura = getAura();
        if (aura < 0) {
            int underflow = Math.abs(aura);
            setAura(0);
            return underflow;
        }
        return 0;
    }
}
