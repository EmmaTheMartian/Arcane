package martian.arcane.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.item.ItemAuraglassBottle;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class ArcaneTabs extends ArcaneRegistry {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArcaneMod.MODID);

    public static final RegistryObject<CreativeModeTab> ARCANE_TAB = TABS.register("arcane_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ArcaneItems.EXTREME_AURAGLASS_BOTTLE.get().getDefaultInstance())
            .title(Component
                    .translatable("itemGroup.arcane.arcane_tab")
                    .withStyle(ChatFormatting.DARK_PURPLE))
            .displayItems((_params, output) -> {
                ArcaneItems.ITEMS.getEntries().forEach(item ->
                        output.accept(item.get().getDefaultInstance()));

                { // Creative Auraglass Bottle (with Integer.MAX_VALUE aura already)
                    ItemStack stack = new ItemStack(ArcaneItems.CREATIVE_AURAGLASS_BOTTLE.get());
                    CompoundTag nbt = stack.getOrCreateTag();
                    ItemAuraglassBottle.initNBT(nbt, Integer.MAX_VALUE);
                    ((ItemAuraglassBottle)stack.getItem()).mapAuraStorage(stack, storage -> {
                        storage.setAura(Integer.MAX_VALUE);
                        return null;
                    });
                    output.accept(stack);
                }
            })
            .build());
}
