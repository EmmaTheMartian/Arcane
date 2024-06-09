package martian.arcane.common.registry;

import com.klikli_dev.modonomicon.registry.DataComponentRegistry;
import martian.arcane.ArcaneMod;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.common.item.ItemSpellTablet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

import static martian.arcane.common.registry.ArcaneItems.*;

@SuppressWarnings("unused")
public class ArcaneTabs extends ArcaneRegistry {
    public ArcaneTabs() { super(TABS); }

    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArcaneMod.MODID);

    public static final Supplier<CreativeModeTab> ARCANE_TAB = TABS.register("arcane_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.arcane.arcane_tab"))
            .icon(() -> new ItemStack(ArcaneItems.EXTREME_AURAGLASS_BOTTLE.get()))
            .displayItems((params, output) -> {
                // Guidebook!
                ItemStack guidebookStack = new ItemStack(GUIDEBOOK.get());
                guidebookStack.set(DataComponentRegistry.BOOK_ID.get(), ArcaneMod.id("arcane_guidebook"));
                output.accept(guidebookStack);

                // Items
                output.acceptAll(ArcaneItems.ITEMS.getEntries().stream().map(it -> it.get().getDefaultInstance()).toList());

                // Other items
                //FIXME: Temporarily removed from tab.
                // See https://github.com/neoforged/NeoForge/issues/1040 and https://github.com/neoforged/NeoForge/pull/1043
                // I suspect that because the data component has data *inside* of it changing, the hash is not changed.
                // A mixin could fix this, but I would rather not use one for such a menial difference.
//                ItemStack creativeAuraglassBottle = new ItemStack(CREATIVE_AURAGLASS_BOTTLE.get());
//                Objects.requireNonNull(creativeAuraglassBottle.get(ArcaneDataComponents.AURA)).setAura(Integer.MAX_VALUE);
//                output.accept(creativeAuraglassBottle);
            })
            .build());

    public static final Supplier<CreativeModeTab> ARCANE_SPELLS_TAB = TABS.register("arcane_spells_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.arcane.arcane_spells_tab"))
            .icon(() -> new ItemStack(ArcaneItems.SPELL_TABLET.get()))
            .withTabsBefore(ArcaneMod.id("arcane_tab"))
            .displayItems((params, output) -> {
                ArcaneRegistries.SPELLS.entrySet().forEach(entry -> {
                    ItemStack stack = ArcaneItems.SPELL_TABLET.get().getDefaultInstance();
                    ItemSpellTablet.setSpell(entry.getKey().location(), stack);
                    output.accept(stack);
                });
            })
            .build());

    public static void buildTabContents(BuildCreativeModeTabContentsEvent e) {
        if (e.getTab() == ARCANE_TAB.get()) {
        } else if (e.getTab() == ARCANE_SPELLS_TAB.get()) {
        }
    }
}
