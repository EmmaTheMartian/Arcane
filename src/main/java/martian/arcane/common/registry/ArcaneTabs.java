package martian.arcane.common.registry;

import com.klikli_dev.modonomicon.api.ModonomiconConstants;
import martian.arcane.ArcaneMod;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.common.item.ItemAuraglassBottle;
import martian.arcane.common.item.ItemSpellTablet;
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
            .displayItems((_params, output) -> ArcaneItems.ITEMS.getEntries().forEach(item -> {
                // Guidebook!
                if (item == ArcaneItems.GUIDEBOOK) {
                    ItemStack stack = ArcaneItems.GUIDEBOOK.get().getDefaultInstance();
                    CompoundTag tag = stack.getOrCreateTag();
                    tag.putString(ModonomiconConstants.Nbt.ITEM_BOOK_ID_TAG, "arcane:arcane_guidebook");
                    output.accept(stack);
                    return;
                }

                output.accept(item.get().getDefaultInstance());

                // Creative Auraglass Bottle (with Integer.MAX_VALUE aura already)
                // We want this item added after the empty creative auraglass bottle, which is why this if statement exists
                if (item == ArcaneItems.CREATIVE_AURAGLASS_BOTTLE) {
                    ItemStack stack = new ItemStack(ArcaneItems.CREATIVE_AURAGLASS_BOTTLE.get());
                    CompoundTag nbt = stack.getOrCreateTag();
                    ItemAuraglassBottle.initNBT(nbt, Integer.MAX_VALUE);
                    ((ItemAuraglassBottle)stack.getItem()).mapAuraStorage(stack, storage -> {
                        storage.setAura(Integer.MAX_VALUE);
                        return null;
                    });
                    output.accept(stack);
                }
            }))
            .build());

    public static final RegistryObject<CreativeModeTab> ARCANE_SPELLS_TAB = TABS.register("arcane_spells_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(ARCANE_TAB.getId())
            .icon(() -> ArcaneItems.SPELL_TABLET.get().getDefaultInstance())
            .title(Component
                    .translatable("itemGroup.arcane.arcane_spells_tab")
                    .withStyle(ChatFormatting.DARK_AQUA))
            .displayItems((_params, output) -> {
                ArcaneSpells.REGISTRY.get().getEntries().forEach(entry -> {
                    ItemStack stack = ArcaneItems.SPELL_TABLET.get().getDefaultInstance();
                    ItemSpellTablet.setSpell(stack, entry.getKey().location());
                    output.accept(stack);
                });
            })
            .build());
}
