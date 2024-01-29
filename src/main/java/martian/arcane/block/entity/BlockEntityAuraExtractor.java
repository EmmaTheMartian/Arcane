package martian.arcane.block.entity;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.NBTHelpers;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.api.block.entity.IAuraInserter;
import martian.arcane.api.capability.IAuraStorage;
import martian.arcane.block.BlockAuraExtractor;
import martian.arcane.registry.ArcaneBlockEntities;
import martian.arcane.registry.ArcaneCapabilities;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlockEntityAuraExtractor extends AbstractAuraBlockEntity {
    private LazyOptional<IAuraStorage> cachedTarget;
    public BlockPos targetPos;
    public int extractRate;

    public BlockEntityAuraExtractor(int maxAura, int extractRate, BlockPos pos, BlockState state) {
        super(maxAura, false, false, ArcaneBlockEntities.AURA_EXTRACTOR.get(), pos, state);
        this.extractRate = extractRate;
    }

    public BlockEntityAuraExtractor(BlockPos pos, BlockState state) {
        super(ArcaneStaticConfig.Maximums.AURA_EXTRACTOR, false, false, ArcaneBlockEntities.AURA_EXTRACTOR.get(), pos, state);
        this.extractRate = ArcaneStaticConfig.Rates.AURA_EXTRACTOR_RATE;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (targetPos != null)
            NBTHelpers.putBlockPos(nbt, NBTHelpers.KEY_EXTRACTOR_TARGET_POS, targetPos);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains(NBTHelpers.KEY_EXTRACTOR_TARGET_POS))
            targetPos = NBTHelpers.getBlockPos(nbt, NBTHelpers.KEY_EXTRACTOR_TARGET_POS);
    }

    @Override
    public List<Component> getText(List<Component> text, boolean detailed) {
        boolean linked = targetPos != null;

        if (linked) {
            text.add(Component.translatable("messages.arcane.linked_to")
                    .append(targetPos.toShortString()));
        } else {
            text.add(Component.translatable("messages.arcane.not_linked")
                    .withStyle(ChatFormatting.DARK_RED));
        }

        return super.getText(text, detailed);
    }

    public static void setTarget(@NotNull BlockEntityAuraExtractor extractor, BlockEntityAuraInserter target) {
        extractor.targetPos = target.getBlockPos();
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        if (blockEntity instanceof BlockEntityAuraExtractor extractor) {
            if (extractor.targetPos == null)
                return;

            LazyOptional<IAuraStorage> target;
            if (extractor.cachedTarget != null)
                target = extractor.cachedTarget;
            else {
                BlockEntity e = level.getBlockEntity(extractor.targetPos);
                if (e instanceof IAuraInserter) {
                    target = e.getCapability(ArcaneCapabilities.AURA_STORAGE);
                    if (extractor.cachedTarget == null)
                        extractor.cachedTarget = target;
                }
                return;
            }

            if (!target.isPresent())
                return;

            extractor.mapAuraStorage(storage -> {
                Direction facing = state.getValue(BlockAuraExtractor.FACING);
                BlockPos extractFrom = pos.offset(facing.getStepX(), facing.getStepY(), facing.getStepZ());
                if (!level.getBlockState(extractFrom).hasBlockEntity())
                    return null;

                BlockEntity e = level.getBlockEntity(extractFrom);
                if (e == null)
                    return null;

                LazyOptional<IAuraStorage> cap = e.getCapability(ArcaneCapabilities.AURA_STORAGE);
                if (cap.isPresent() && cap.resolve().isPresent())
                    storage.extractAuraFrom(cap.resolve().get(), extractor.extractRate);

                // Send to the target inserter
                storage.sendAuraTo(target.resolve().orElseThrow(), extractor.extractRate);

                return null;
            });
        }
    }
}
