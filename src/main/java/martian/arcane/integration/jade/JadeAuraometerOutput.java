package martian.arcane.integration.jade;

import martian.arcane.ArcaneMod;
import martian.arcane.api.block.entity.IAuraometerOutput;
import martian.arcane.api.item.IAuraometer;
import martian.arcane.common.ArcaneContent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public enum JadeAuraometerOutput implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    static final ResourceLocation uid = ArcaneMod.id("jade_auraometer_provider");
    static final Predicate<ItemStack> hasAuraometer = it -> it.getItem() instanceof IAuraometer;

    @Override
    public void appendTooltip(ITooltip tip, BlockAccessor accessor, IPluginConfig config) {
        if (
            accessor.getBlockEntity() instanceof IAuraometerOutput it &&
            accessor.getPlayer().getInventory().contains(hasAuraometer)
        ) {
            CompoundTag data = accessor.getServerData();

            List<Component> text = new ArrayList<>();
            it.getText(text, accessor.getPlayer().isCrouching());
            tip.addAll(text);

            if (accessor.getPlayer().isCrouching() && accessor.getBlockEntity().hasData(ArcaneContent.DA_AURA)) {
                tip.add(Component
                        .translatable("messages.arcane.can_extract")
                        .append(Boolean.toString(data.getBoolean("CanExtract"))));

                tip.add(Component
                        .translatable("messages.arcane.can_insert")
                        .append(Boolean.toString(data.getBoolean("CanInsert"))));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return uid;
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        if (accessor.getBlockEntity().hasData(ArcaneContent.DA_AURA)) {
            var aura = accessor.getBlockEntity().getData(ArcaneContent.DA_AURA);
            tag.putBoolean("CanExtract", aura.canExtract());
            tag.putBoolean("CanInsert", aura.canInsert());
        }
    }
}
