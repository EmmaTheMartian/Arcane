package martian.arcane.common.block.generators.heat;

import martian.arcane.ArcaneConfig;
import martian.arcane.ArcaneMod;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.api.block.entity.IAuraometerOutput;
import martian.arcane.common.ArcaneContent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BlockEntityIgnisCollector extends AbstractAuraBlockEntity {
    private int ticksToNextCollect = 0;

    public BlockEntityIgnisCollector(BlockPos pos, BlockState state) {
        super(ArcaneConfig.collectorsAuraCapacity, true, false, ArcaneContent.BE_HEAT_COLLECTOR.tile().get(), pos, state);
    }

    public List<Component> getText(List<Component> text, IAuraometerOutput.Context context) {
        if (level != null)
            text.add(Component
                    .translatable("messages.arcane.generating")
                    .append(Integer.toString(getAuraToGenerate(level, getBlockPos())))
                    .withStyle(ChatFormatting.RED));

        return super.getText(text, context);
    }

    public static int getAuraToGenerate(Level level, BlockPos pos) {
        return ArcaneMod.getIgnisGenAmountForState(level.getBlockState(pos.below()));
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState ignoredState, T blockEntity) {
        if (blockEntity instanceof BlockEntityIgnisCollector collector) {
            if (++collector.ticksToNextCollect >= ArcaneConfig.ignisCollectorSpeed) {
                collector.addAura(getAuraToGenerate(level, pos));
                collector.ticksToNextCollect = 0;
            }
        }
    }
}
