package martian.arcane.api;

import martian.arcane.ArcaneMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;

public final class NBTHelpers {
    public static final String KEY_AURA = ArcaneMod.MODID + ".aura";
    public static final String KEY_AURA_PROGRESS = ArcaneMod.MODID + ".auraProgress";
    public static final String KEY_MAX_AURA = ArcaneMod.MODID + ".maxAura";
    public static final String KEY_AURA_EXTRACTABLE = ArcaneMod.MODID + ".canExtractAura";
    public static final String KEY_AURA_INSERTABLE = ArcaneMod.MODID + ".canInsertAura";
    public static final String KEY_CONFIGURATOR_P1 = ArcaneMod.MODID + ".configurator.p1";
    public static final String KEY_EXTRACTOR_TARGET_POS = ArcaneMod.MODID + ".targetPos";
    public static final String KEY_STACK = ArcaneMod.MODID + ".stack";
    public static final String KEY_MODE = ArcaneMod.MODID + ".mode";
    public static final String KEY_SPELL = ArcaneMod.MODID + ".spell";
    public static final String KEY_ACTIVE = ArcaneMod.MODID + ".active";
    public static final String KEY_PUSH_RATE = ArcaneMod.MODID + ".pushRate";
    public static final String KEY_LEVEL = ArcaneMod.MODID + ".level";
    public static final String KEY_TICKS_TO_NEXT = ArcaneMod.MODID + ".ticksToNext";
    public static final String KEY_CAST_RATE = ArcaneMod.MODID + ".castRate";
    public static final String KEY_HAS_SIGNAL = ArcaneMod.MODID + ".hasSignal";

    public static void putBlockPos(CompoundTag nbt, String key, BlockPos pos) {
        nbt.putIntArray(key, new int[]{pos.getX(), pos.getY(), pos.getZ()});
    }

    public static BlockPos getBlockPos(CompoundTag nbt, String key) {
        int[] a = nbt.getIntArray(key);
        return new BlockPos(a[0], a[1], a[2]);
    }

    public static void putItemStack(CompoundTag nbt, String key, ItemStack stack) {
        CompoundTag stackTag = new CompoundTag();
        stack.save(stackTag);
        nbt.put(key, stackTag);
    }

    public static ItemStack getItemStack(CompoundTag nbt, String key) {
        return ItemStack.of(nbt.getCompound(key));
    }

    public static void init(CompoundTag nbt, String key, BiConsumer<CompoundTag, String> consumer) {
        if (!nbt.contains(key))
            consumer.accept(nbt, key);
    }
}
