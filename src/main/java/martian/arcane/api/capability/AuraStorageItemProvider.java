package martian.arcane.api.capability;

import martian.arcane.api.NbtKeys;
import martian.arcane.registry.ArcaneCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AuraStorageItemProvider implements ICapabilityProvider {
    private final ItemStack stack;
    public final AuraStorage storage;

    private final LazyOptional<IAuraStorage> auraStorageHolder;

    public AuraStorageItemProvider(ItemStack pStack, int maxAura, boolean extractable) {
        stack = pStack;

        storage = new AuraStorage(maxAura, extractable) {
            private @NotNull CompoundTag getNbt() {
                CompoundTag nbt = stack.getOrCreateTag();
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
