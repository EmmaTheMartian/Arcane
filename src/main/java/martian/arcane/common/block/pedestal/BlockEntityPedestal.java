package martian.arcane.common.block.pedestal;

import martian.arcane.ArcaneConfig;
import martian.arcane.api.aura.AuraRecord;
import martian.arcane.api.block.entity.AbstractAuraBlockEntityWithSingleItem;
import martian.arcane.api.block.entity.IAuraometerOutput;
import martian.arcane.common.ArcaneContent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BlockEntityPedestal extends AbstractAuraBlockEntityWithSingleItem implements IAuraometerOutput {
    public BlockEntityPedestal(BlockPos pos, BlockState state) {
        super(ArcaneConfig.pedestalAuraCapacity, false, true, ArcaneContent.BE_PEDESTAL.tile().get(), pos, state);
    }

    @Override
    public boolean isUpgradable() {
        return true;
    }

    @Override
    public List<Component> getText(List<Component> text, IAuraometerOutput.Context context) {
        super.getText(text, context);

        if (!getItem().isEmpty()) {
            text.add(Component
                    .translatable("messages.arcane.holding")
                    .append(getItem().getDisplayName()));

            if (getItem().has(ArcaneContent.DC_AURA)) {
                AuraRecord aura = getItem().get(ArcaneContent.DC_AURA);
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
            if (!pedestal.isEmpty() && pedestal.getItem().has(ArcaneContent.DC_AURA)) {
                AuraRecord aura = pedestal.getItem().get(ArcaneContent.DC_AURA);
                if (aura != null && aura.getAura() < aura.getMaxAura()) {
                    var mutable = aura.unfreeze();
                    pedestal.sendAuraTo(mutable, 1);
                    pedestal.getItem().set(ArcaneContent.DC_AURA, mutable.freeze());
                    level.sendBlockUpdated(pos, state, state, 2);
                }
            }
        }
    }
}
