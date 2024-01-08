package martian.arcane.api.capability;

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
        if (!nbt.contains(NBTHelpers.KEY_AURA_RECEIVABLE))
            nbt.putBoolean(NBTHelpers.KEY_AURA_RECEIVABLE, this.receivable);
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
    public boolean canReceive() {
        return getNbt().getBoolean(NBTHelpers.KEY_AURA_RECEIVABLE);
    }

    @Override
    public void setAura(int value) {
        getNbt().putInt(NBTHelpers.KEY_AURA, value);
    }

    @Override
    public void setExtractable(boolean value) {
        getNbt().putBoolean(NBTHelpers.KEY_AURA_EXTRACTABLE, value);
    }

    @Override
    public void setReceivable(boolean value) {
        getNbt().putBoolean(NBTHelpers.KEY_AURA_RECEIVABLE, value);
    }
}
