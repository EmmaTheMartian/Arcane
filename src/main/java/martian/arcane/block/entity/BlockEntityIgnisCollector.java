package martian.arcane.block.entity;

import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneStaticConfig;
import martian.arcane.ArcaneTags;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.api.capability.AuraStorage;
import martian.arcane.api.capability.IAuraStorage;
import martian.arcane.registry.ArcaneBlockEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BlockEntityIgnisCollector extends AbstractAuraBlockEntity {
    private int ticksToNextCollect = 0;

    public BlockEntityIgnisCollector(int maxAura, BlockPos pos, BlockState state) {
        super(maxAura, true, false, ArcaneBlockEntities.IGNIS_COLLECTOR.get(), pos, state);
    }

    public BlockEntityIgnisCollector(BlockPos pos, BlockState state) {
        super(ArcaneStaticConfig.Maximums.COLLECTOR_MAX_AURA, true, false, ArcaneBlockEntities.IGNIS_COLLECTOR.get(), pos, state);
    }

    public List<Component> getText(List<Component> text, boolean detailed) {
        if (level != null)
            text.add(Component
                    .literal("Generating: ")
                    .append(Integer.toString(getAuraToGenerate(level, getBlockPos())))
                    .withStyle(ChatFormatting.RED));

        return super.getText(text, detailed);
    }

    public static int getAuraToGenerate(Level level, BlockPos pos) {
        BlockState blockBelow = level.getBlockState(pos.below());
        if (blockBelow.is(ArcaneTags.IGNIS_HEAT_SOURCES)) {
            return ArcaneMod.getIgnisGenAmountForState(blockBelow);
        }
        return 0;
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState ignoredState, T blockEntity) {
        if (blockEntity instanceof BlockEntityIgnisCollector collector) {
            if (++collector.ticksToNextCollect >= ArcaneStaticConfig.Speed.IGNIS_COLLECTOR_SPEED) {
                IAuraStorage aura = collector.getAuraStorage().orElseThrow();
                aura.addAura(getAuraToGenerate(level, pos));
                collector.ticksToNextCollect = 0;
            }
        }
    }
}
