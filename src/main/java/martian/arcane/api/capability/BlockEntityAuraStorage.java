package martian.arcane.api.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Supplier;

public class BlockEntityAuraStorage extends NbtAuraStorage {
    private final BlockEntity blockEntity;

    public BlockEntityAuraStorage(Supplier<CompoundTag> nbtSupplier, BlockEntity blockEntity, int maxAura, boolean extractable, boolean acceptable) {
        super(nbtSupplier, maxAura, extractable, acceptable);
        this.blockEntity = blockEntity;
    }

    @Override
    public void setAura(int value) {
        super.setAura(value);
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
        blockEntity.setChanged();
        return overflow;
    }

    @Override
    public int removeAura(int value) {
        int underflow = super.removeAura(value);
        blockEntity.setChanged();
        return underflow;
    }
}
