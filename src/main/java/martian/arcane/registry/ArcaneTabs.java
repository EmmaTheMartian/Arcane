package martian.arcane.registry;

import martian.arcane.ArcaneMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class ArcaneTabs extends ArcaneRegistry {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArcaneMod.MODID);

    public static final RegistryObject<CreativeModeTab> ARCANE_TAB = TABS.register("arcane_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ArcaneItems.AURAGLASS_BOTTLE.get().getDefaultInstance())
            .displayItems((itemDisplayParameters, output) -> {
                ArcaneItems.ITEMS.getEntries().forEach(item -> {
                    output.accept(item.get().getDefaultInstance());
                });
            })
            .build());
}
