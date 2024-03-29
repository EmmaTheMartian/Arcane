package martian.arcane.common.block.entity.machines;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.api.capability.IAuraStorage;
import martian.arcane.common.registry.ArcaneBlockEntities;
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

    public BlockEntityAquaCollector(int maxAura, int auraLoss, BlockPos pos, BlockState state) {
        super(maxAura, auraLoss, true, false, ArcaneBlockEntities.AQUA_COLLECTOR.get(), pos, state);
    }

    public BlockEntityAquaCollector(BlockPos pos, BlockState state) {
        super(ArcaneStaticConfig.Maximums.COLLECTOR, ArcaneStaticConfig.AuraLoss.COPPER_TIER, true, false, ArcaneBlockEntities.AQUA_COLLECTOR.get(), pos, state);
    }

    public List<Component> getText(List<Component> text, boolean detailed) {
        if (level != null)
            text.add(Component
                    .translatable("messages.arcane.generating")
                    .append(Integer.toString(getAuraToGenerate(level, getBlockPos())))
                    .withStyle(ChatFormatting.RED));

        return super.getText(text, detailed);
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

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        AbstractAuraBlockEntity.tick(level, pos, state, blockEntity);
        if (blockEntity instanceof BlockEntityAquaCollector collector) {
            if (++collector.ticksToNextCollect >= ArcaneStaticConfig.Speed.AQUA_COLLECTOR_SPEED) {
                IAuraStorage aura = collector.getAuraStorage().orElseThrow();
                aura.addAura(getAuraToGenerate(level, pos));
                collector.ticksToNextCollect = 0;
            }
        }
    }
}
