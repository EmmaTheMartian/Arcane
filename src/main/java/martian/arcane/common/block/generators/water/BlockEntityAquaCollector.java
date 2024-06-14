package martian.arcane.common.block.generators.water;

import martian.arcane.ArcaneConfig;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.api.block.entity.IAuraometerOutput;
import martian.arcane.common.ArcaneContent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BlockEntityAquaCollector extends AbstractAuraBlockEntity {
    private int ticksToNextCollect = 0;

    public BlockEntityAquaCollector(BlockPos pos, BlockState state) {
        super(ArcaneConfig.collectorsAuraCapacity, true, false, ArcaneContent.AQUA_COLLECTOR.tile().get(), pos, state);
    }

    public List<Component> getText(List<Component> text, IAuraometerOutput.Context context) {
        if (level != null)
            text.add(Component
                    .translatable("messages.arcane.generating")
                    .append(Integer.toString(getAuraToGenerate(level, getBlockPos())))
                    .withStyle(ChatFormatting.RED));

        return super.getText(text, context);
    }

    private static final BlockPos[] posOffsets = new BlockPos[]{
            new BlockPos(1, 0, 0),
            new BlockPos(-1, 0, 0),
            new BlockPos(0, 0, 1),
            new BlockPos(0, 0, -1),
            new BlockPos(-1, 0, -1),
            new BlockPos(-1, 0, 1),
            new BlockPos(1, 0, -1),
            new BlockPos(1, 0, 1),
    };

    public static int getAuraToGenerate(Level level, BlockPos pos) {
        int sources = 0;
        BlockState curState;
        for (BlockPos offset : posOffsets) {
            curState = level.getBlockState(pos.offset(offset.getX(), offset.getY(), offset.getZ()));
            if (curState.is(Blocks.WATER)) {
                sources++;
            }
        }
        return Math.floorDiv(sources, 2);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState ignoredState, T blockEntity) {
        if (blockEntity instanceof BlockEntityAquaCollector collector) {
            AbstractAuraBlockEntity.tickForAuraLoss(level, collector);
            if (++collector.ticksToNextCollect >= ArcaneConfig.aquaCollectorSpeed) {
                collector.mapAuraStorage(aura -> aura.addAura(getAuraToGenerate(level, pos)));
                collector.ticksToNextCollect = 0;
            }
        }
    }
}
