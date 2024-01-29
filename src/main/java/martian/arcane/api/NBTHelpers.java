package martian.arcane.api;

import martian.arcane.ArcaneMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import java.util.function.BiConsumer;

public final class NBTHelpers {
    public static final String KEY_AURA = ArcaneMod.MODID + ".aura";
    public static final String KEY_MAX_AURA = ArcaneMod.MODID + ".maxAura";
    public static final String KEY_AURA_EXTRACTABLE = ArcaneMod.MODID + ".canExtractAura";
    public static final String KEY_AURA_RECEIVABLE = ArcaneMod.MODID + ".canReceive";
    public static final String KEY_WRENCH_HASP1 = ArcaneMod.MODID + ".wrench.hasp1";
    public static final String KEY_WRENCH_P1 = ArcaneMod.MODID + ".wrench.p1";
    public static final String KEY_EXTRACTOR_TARGET_POS = ArcaneMod.MODID + ".targetPos";
    public static final String KEY_STACK = ArcaneMod.MODID + ".stack";
    public static final String KEY_MODE = ArcaneMod.MODID + ".mode";

    public static void putBlockPos(CompoundTag nbt, String key, BlockPos pos) {
        nbt.putIntArray(key, new int[]{pos.getX(), pos.getY(), pos.getZ()});
    }

    public static BlockPos getBlockPos(CompoundTag nbt, String key) {
        int[] a = nbt.getIntArray(key);
        return new BlockPos(a[0], a[1], a[2]);
    }

    public static void init(CompoundTag nbt, String key, BiConsumer<CompoundTag, String> consumer) {
        if (!nbt.contains(key))
            consumer.accept(nbt, key);
    }
}
