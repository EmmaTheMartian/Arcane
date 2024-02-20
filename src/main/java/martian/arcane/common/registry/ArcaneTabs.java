package martian.arcane.common.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.common.item.ItemAuraWand;
import martian.arcane.common.item.ItemAuraglassBottle;
import martian.arcane.common.item.ItemSpellTablet;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

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
                    .withStyle(ChatFormatting.AQUA))
            .displayItems((_params, output) -> {
                ArcaneSpells.REGISTRY.get().getEntries().forEach(entry -> {
                    ItemStack stack = ArcaneItems.SPELL_TABLET.get().getDefaultInstance();
                    ItemSpellTablet.setSpell(stack, entry.getKey().location());
                    output.accept(stack);
                });

                List<Item> wands = List.of(ArcaneItems.WAND_CHERRY.get(), ArcaneItems.WAND_BLUE_GOLD.get(), ArcaneItems.WAND_AURACHALCUM.get());
                wands.forEach(wand -> ArcaneSpells.REGISTRY.get().getEntries().forEach(entry -> {
                    ItemStack stack = wand.getDefaultInstance();
                    ((ItemAuraWand)stack.getItem()).setSpell(entry.getKey().location(), stack);
                    output.accept(stack);
                }));
            })
            .build());
}
