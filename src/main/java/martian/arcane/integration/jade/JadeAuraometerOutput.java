package martian.arcane.integration.jade;

import martian.arcane.ArcaneMod;
import martian.arcane.api.block.entity.IAuraometerOutput;
import martian.arcane.api.capability.IAuraStorage;
import martian.arcane.common.registry.ArcaneCapabilities;
import martian.arcane.common.registry.ArcaneItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.LazyOptional;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.Set;

public enum JadeAuraometerOutput implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    static final ResourceLocation UID = ArcaneMod.id("jade_auraometer_provider");
    static final Set<Item> MATCHING_ITEMS = Set.of(ArcaneItems.AURAOMETER.get());

    @Override
    public void appendTooltip(ITooltip tip, BlockAccessor accessor, IPluginConfig config) {
        if (
            accessor.getBlockEntity() instanceof IAuraometerOutput &&
            accessor.getPlayer().getInventory().hasAnyOf(MATCHING_ITEMS)
        ) {
            CompoundTag data = accessor.getServerData();
            tip.add(Component
                    .translatable("messages.arcane.aura")
                    .append(Integer.toString(data.getInt("Aura")))
                    .append("/")
                    .append(Integer.toString(data.getInt("MaxAura")))
                    .withStyle(ChatFormatting.LIGHT_PURPLE));

            tip.add(Component
                    .translatable("messages.arcane.can_extract")
                    .append(Boolean.toString(data.getBoolean("CanExtract"))));

            tip.add(Component
                    .translatable("messages.arcane.can_insert")
                    .append(Boolean.toString(data.getBoolean("CanInsert"))));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        LazyOptional<IAuraStorage> cap = accessor.getBlockEntity().getCapability(ArcaneCapabilities.AURA_STORAGE);
        cap.map(aura -> {
            tag.putInt("Aura", aura.getAura());
            tag.putInt("MaxAura", aura.getMaxAura());
            tag.putBoolean("CanExtract", aura.canExtract());
            tag.putBoolean("CanInsert", aura.canInsert());
            return 0;
        });
    }
}
