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
    public void setReceivable(boolean value) {
        super.setReceivable(value);
        blockEntity.setChanged();
    }
}