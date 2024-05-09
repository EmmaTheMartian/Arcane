package martian.arcane.api.capability.aura;

import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Supplier;

public class BlockEntityAuraStorage extends NbtAuraStorage {
    private final BlockEntity blockEntity;
    public boolean updateIdleness = true;

    public BlockEntityAuraStorage(Supplier<CompoundTag> nbtSupplier, BlockEntity blockEntity, int maxAura, boolean extractable, boolean acceptable) {
        super(nbtSupplier, maxAura, extractable, acceptable);
        this.blockEntity = blockEntity;
    }

    @Override
    public void setAura(int value) {
        super.setAura(value);
        if (updateIdleness)
            AbstractAuraBlockEntity.setNotIdle(this.blockEntity);
        blockEntity.setChanged();
    }

    @Override
    public void setMaxAura(int value) {
        super.setMaxAura(value);
        blockEntity.setChanged();
    }

    @Override
    public void setExtractable(boolean value) {
        super.setExtractable(value);
        blockEntity.setChanged();
    }

    @Override
    public void setInsertable(boolean value) {
        super.setInsertable(value);
        blockEntity.setChanged();
    }

    @Override
    public int addAura(int value) {
        int overflow = super.addAura(value);
        if (updateIdleness)
            AbstractAuraBlockEntity.setNotIdle(this.blockEntity);
        blockEntity.setChanged();
        return overflow;
    }

    @Override
    public int removeAura(int value) {
        int underflow = super.removeAura(value);
        if (updateIdleness)
            AbstractAuraBlockEntity.setNotIdle(this.blockEntity);
        blockEntity.setChanged();
        return underflow;
    }

    // Updates aura without making the BlockEntity not idle
    public void setAuraNoIdleUpdate(int value) {
        updateIdleness = false;
        super.setAura(value);
        blockEntity.setChanged();
        updateIdleness = true;
    }

    // Updates aura without making the BlockEntity not idle
    public int addAuraNoIdleUpdate(int value) {
        updateIdleness = false;
        int overflow = super.addAura(value);
        blockEntity.setChanged();
        updateIdleness = true;
        return overflow;
    }

    // Updates aura without making the BlockEntity not idle
    public int removeAuraNoIdleUpdate(int value) {
        updateIdleness = false;
        int underflow = super.removeAura(value);
        blockEntity.setChanged();
        updateIdleness = true;
        return underflow;
    }
}
