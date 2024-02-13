package martian.arcane.block.entity;

import martian.arcane.api.block.entity.AbstractSingleItemBlockEntity;
import martian.arcane.api.block.entity.IAuraometerOutput;
import martian.arcane.registry.ArcaneBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BlockEntityPedestal extends AbstractSingleItemBlockEntity implements IAuraometerOutput {
    public BlockEntityPedestal(BlockPos pos, BlockState state) {
        super(ArcaneBlockEntities.PEDESTAL.get(), pos, state);
    }

    @Override
    public List<Component> getText(List<Component> text, boolean detailed) {
        if (!getItem().isEmpty()) {
            text.add(Component
                    .translatable("messages.arcane.holding")
                    .append(getItem().getDisplayName()));
        }

        return text;
    }
}
