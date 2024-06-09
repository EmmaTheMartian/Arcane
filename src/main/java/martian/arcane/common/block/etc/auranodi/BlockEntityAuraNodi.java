package martian.arcane.common.block.etc.auranodi;

import martian.arcane.ArcaneConfig;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.common.registry.ArcaneBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAuraNodi extends AbstractAuraBlockEntity {
    public BlockEntityAuraNodi(BlockPos pos, BlockState state) {
        super(ArcaneConfig.auraNodiAura, 0, true, false, ArcaneBlockEntities.AURA_NODI.get(), pos, state);
        voidMapAuraStorage(aura -> aura.setAura(aura.getMaxAura()));
    }
}
