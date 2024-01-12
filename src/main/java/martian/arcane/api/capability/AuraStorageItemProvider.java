package martian.arcane.api.capability;

import martian.arcane.registry.ArcaneCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AuraStorageItemProvider implements ICapabilityProvider {
    public final IAuraStorage storage;
    private final LazyOptional<IAuraStorage> auraStorageHolder;

    public AuraStorageItemProvider(ItemStack stack, int maxAura, boolean extractable, boolean acceptable) {
        storage = new NbtAuraStorage(stack::getOrCreateTag, maxAura, extractable, acceptable);
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
