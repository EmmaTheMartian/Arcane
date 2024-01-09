package martian.arcane.block;

import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.block.entity.BlockEntityAuraExtractor;
import martian.arcane.registry.ArcaneBlockEntities;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BlockAuraExtractor extends AbstractAuraMachine {
    public BlockAuraExtractor() {
        super(PropertyHelpers.basicAuraMachine().noOcclusion().noCollission(), BlockEntityAuraExtractor::new);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return type == ArcaneBlockEntities.AURA_EXTRACTOR_BE.get() ? BlockEntityAuraExtractor::tick : null;
    }
}
