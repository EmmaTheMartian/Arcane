package martian.arcane.common.registry;

import com.mojang.serialization.Codec;
import martian.arcane.ArcaneMod;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.api.MachineTier;
import martian.arcane.api.aura.AuraRecord;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.function.UnaryOperator;

public class ArcaneDataComponents extends ArcaneRegistry {
    public ArcaneDataComponents() { super(REGISTER); }

    private static final DeferredRegister<DataComponentType<?>> REGISTER = DeferredRegister.createDataComponents(ArcaneMod.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<AuraRecord>> AURA = register("aura", builder ->
            builder.persistent(AuraRecord.CODEC).networkSynchronized(AuraRecord.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<@Nullable ResourceLocation>> SPELL = register("spell", builder ->
            builder.persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<@Nullable BlockPos>> TARGET_POS = register("target_pos", builder ->
            builder.persistent(BlockPos.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> ACTIVE = register("active", builder ->
            builder.persistent(Codec.BOOL));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> PUSH_RATE = register("push_rate", builder ->
            builder.persistent(Codec.INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MachineTier>> MACHINE_TIER = register("machine_tier", builder ->
            builder.persistent(MachineTier.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> MODE = register("mode", builder ->
            builder.persistent(Codec.STRING));

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> func) {
        return REGISTER.register(name, () -> func.apply(DataComponentType.builder()).build());
    }
}
