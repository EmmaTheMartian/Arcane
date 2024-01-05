package martian.arcane.api.block.entity;

import martian.arcane.api.capability.AuraStorageBlockEntityProvider;
import martian.arcane.api.capability.IAuraStorage;
import martian.arcane.registry.ArcaneCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractAuraBlockEntity extends BlockEntity {
    private final AuraStorageBlockEntityProvider provider;

    public AbstractAuraBlockEntity(int maxAura, boolean extractable, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        provider = new AuraStorageBlockEntityProvider(this, maxAura, extractable);
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        LazyOptional<T> providerCapability = provider.getCapability(capability);

        if (providerCapability.isPresent())
            return providerCapability;

        return super.getCapability(capability, side);
    }

    public Optional<IAuraStorage> getAuraStorage() {
        return getCapability(ArcaneCapabilities.AURA_STORAGE).resolve();
    }
}
