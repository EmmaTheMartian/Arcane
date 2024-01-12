package martian.arcane;

import com.mojang.logging.LogUtils;
import martian.arcane.datagen.ArcaneDatagen;
import martian.arcane.registry.ArcaneBlockEntities;
import martian.arcane.registry.ArcaneBlocks;
import martian.arcane.registry.ArcaneItems;
import martian.arcane.registry.ArcaneTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ArcaneMod.MODID)
public class ArcaneMod
{
    public static final String MODID = "arcane";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ArcaneMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ArcaneConfig.SPEC);

        ArcaneBlocks.BLOCKS.register(modBus);
        ArcaneBlockEntities.BLOCK_ENTITIES.register(modBus);
        ArcaneItems.ITEMS.register(modBus);
        ArcaneTabs.TABS.register(modBus);

        modBus.addListener(EventPriority.LOWEST, ArcaneDatagen::gatherData);

        forgeBus.register(this);
    }
}
