package martian.arcane;

import com.mojang.logging.LogUtils;
import martian.arcane.registry.ArcaneBlockEntities;
import martian.arcane.registry.ArcaneBlocks;
import martian.arcane.registry.ArcaneItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(ArcaneMod.MODID)
public class ArcaneMod
{
    public static final String MODID = "arcane";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> ARCANE_TAB = CREATIVE_MODE_TABS.register("arcane_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ArcaneItems.AURAGLASS_BOTTLE.get().getDefaultInstance())
            .displayItems((itemDisplayParameters, output) -> {
                ArcaneItems.ITEMS.getEntries().forEach(item -> {
                    output.accept(item.get().getDefaultInstance());
                });
            })
            .build());

    public ArcaneMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ArcaneConfig.SPEC);

        ArcaneBlocks.BLOCKS.register(modBus);
        ArcaneBlockEntities.BLOCK_ENTITIES.register(modBus);
        ArcaneItems.ITEMS.register(modBus);
        CREATIVE_MODE_TABS.register(modBus);

        forgeBus.register(this);
    }
}
