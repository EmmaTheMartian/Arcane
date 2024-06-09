package martian.arcane.common.block.pedestal;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.aura.AuraRecord;
import martian.arcane.api.block.entity.AbstractAuraBlockEntityWithSingleItem;
import martian.arcane.api.block.entity.AbstractSingleItemBlockEntity;
import martian.arcane.api.block.entity.IAuraometerOutput;
import martian.arcane.common.registry.ArcaneBlockEntities;
import martian.arcane.common.registry.ArcaneDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BlockEntityPedestal extends AbstractAuraBlockEntityWithSingleItem implements IAuraometerOutput {
    public BlockEntityPedestal(BlockPos pos, BlockState state) {
        super(ArcaneStaticConfig.AuraMaximums.PEDESTAL, ArcaneStaticConfig.AuraLoss.COPPER_TIER, false, true, ArcaneBlockEntities.PEDESTAL.get(), pos, state);
    }

    public BlockEntityPedestal(int maxAura, int auraLoss, BlockPos pos, BlockState state) {
        super(maxAura, auraLoss, false, true, ArcaneBlockEntities.PEDESTAL.get(), pos, state);
    }

    @Override
    public List<Component> getText(List<Component> text, boolean detailed) {
        if (!getItem().isEmpty()) {
            text.add(Component
                    .translatable("messages.arcane.holding")
                    .append(getItem().getDisplayName()));

            if (getItem().has(ArcaneDataComponents.AURA)) {
                AuraRecord aura = getItem().get(ArcaneDataComponents.AURA);
                assert aura != null;
                text.add(Component
                        .translatable("messages.arcane.item_aura")
                        .append(Integer.toString(aura.getAura()))
                        .append("/")
                        .append(Integer.toString(aura.getMaxAura()))
                        .withStyle(ChatFormatting.LIGHT_PURPLE));
            }
        }

        return text;
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T entity) {
        if (level.isClientSide)
            return;

        if (entity instanceof BlockEntityPedestal pedestal) {
            if (!pedestal.isEmpty() && pedestal.getItem().has(ArcaneDataComponents.AURA)) {
                AuraRecord aura = pedestal.getItem().get(ArcaneDataComponents.AURA);
                if (aura != null && aura.getAura() < aura.getMaxAura()) {
                    var mutable = aura.unfreeze();
                    pedestal.sendAuraTo(mutable, 1);
                    pedestal.getItem().set(ArcaneDataComponents.AURA, mutable.freeze());
                    level.sendBlockUpdated(pos, state, state, 2);
                }
            }
        }
    }
}
