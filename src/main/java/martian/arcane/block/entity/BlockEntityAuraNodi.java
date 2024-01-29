package martian.arcane.block.entity;

import martian.arcane.ArcaneConfig;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.registry.ArcaneBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAuraNodi extends AbstractAuraBlockEntity {
    public BlockEntityAuraNodi(BlockPos pos, BlockState state) {
        super(ArcaneConfig.auraNodiAura, true, false, ArcaneBlockEntities.AURA_NODI.get(), pos, state);
        getAuraStorage().ifPresent(aura -> aura.setAura(aura.getMaxAura()));
    }
}
