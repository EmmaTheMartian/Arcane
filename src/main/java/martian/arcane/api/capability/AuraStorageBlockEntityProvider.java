package martian.arcane.api.capability;

import martian.arcane.api.NbtKeys;
import martian.arcane.registry.ArcaneCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AuraStorageBlockEntityProvider implements ICapabilityProvider {
    private final BlockEntity tile;
    public final AuraStorage storage;

    private final LazyOptional<IAuraStorage> auraStorageHolder;

    public AuraStorageBlockEntityProvider(BlockEntity pTile, int maxAura, boolean extractable) {
        tile = pTile;

        storage = new AuraStorage(maxAura, extractable) {
            private @NotNull CompoundTag getNbt() {
                CompoundTag nbt = tile.getPersistentData();
                if (!nbt.contains(NbtKeys.KEY_AURA))
                    nbt.putInt(NbtKeys.KEY_AURA, 0);
                if (!nbt.contains(NbtKeys.KEY_AURA_EXTRACTABLE))
                    nbt.putBoolean(NbtKeys.KEY_AURA_EXTRACTABLE, storage.extractable);
                return nbt;
            }

            @Override
            public int getAura() {
                return getNbt().getInt(NbtKeys.KEY_AURA);
            }

            @Override
            public boolean canExtract() {
                return getNbt().getBoolean(NbtKeys.KEY_AURA_EXTRACTABLE);
            }

            @Override
            public void setAura(int value) {
                getNbt().putInt(NbtKeys.KEY_AURA, value);
            }

            @Override
            public void setExtractable(boolean value) {
                getNbt().putBoolean(NbtKeys.KEY_AURA_EXTRACTABLE, value);
            }
        };

        auraStorageHolder = LazyOptional.of(() -> storage);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        final LazyOptional<T> capAuraStorage = ArcaneCapabilities.AURA_STORAGE.orEmpty(capability, auraStorageHolder);

        if (capAuraStorage.isPresent())
            return capAuraStorage;

        return LazyOptional.empty();
    }
}
