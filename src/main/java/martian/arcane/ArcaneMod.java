package martian.arcane;

import com.mojang.logging.LogUtils;
import martian.arcane.common.registry.*;
import martian.arcane.datagen.ArcaneDatagen;
import martian.arcane.integration.curios.CuriosIntegration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.BlastFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Mod(ArcaneMod.MODID)
public class ArcaneMod
{
    public static final String MODID = "arcane";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static Map<Function<BlockState, Boolean>, Integer> ignisGenerationAmounts = new HashMap<>();

    public ArcaneMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ArcaneConfig.SPEC);

        ArcaneBlocks.BLOCKS.register(modBus);
        ArcaneItems.ITEMS.register(modBus);
        ArcaneBlockEntities.REGISTER.register(modBus);
        ArcaneRecipeTypes.RECIPE_TYPES.register(modBus);
        ArcaneRecipeTypes.RECIPE_SERIALIZERS.register(modBus);
        ArcaneTabs.TABS.register(modBus);
        ArcaneSpells.REGISTER.register(modBus);
        ArcaneNetworking.init();

        // TODO: Replace this with something data-driven.
        //  Once I update to NeoForge, this can become a data map. I am unsure of what to use on Fabric though.
        ignisGenerationAmounts.put(state -> state.is(Blocks.FIRE), 1);
        ignisGenerationAmounts.put(state -> state.is(Blocks.CAMPFIRE) && state.getValue(CampfireBlock.LIT), 1);
        ignisGenerationAmounts.put(state -> state.is(Blocks.LAVA_CAULDRON), 1);
        ignisGenerationAmounts.put(state -> state.is(Blocks.MAGMA_BLOCK), 1);
        ignisGenerationAmounts.put(state -> state.is(Blocks.FURNACE) && state.getValue(FurnaceBlock.LIT), 1);
        ignisGenerationAmounts.put(state -> state.is(Blocks.BLAST_FURNACE) && state.getValue(BlastFurnaceBlock.LIT), 2);
        ignisGenerationAmounts.put(state -> state.is(Blocks.SOUL_FIRE), 2);
        ignisGenerationAmounts.put(state -> state.is(Blocks.SOUL_CAMPFIRE) && state.getValue(CampfireBlock.LIT), 2);
        ignisGenerationAmounts.put(state -> state.is(Blocks.LAVA), 2);
        ignisGenerationAmounts.put(state -> state.is(ArcaneBlocks.SOUL_MAGMA.get()), 3);

        modBus.addListener(this::setup);
        modBus.addListener(EventPriority.LOWEST, ArcaneDatagen::gatherData);

        forgeBus.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        if (ModList.get().isLoaded("curios")) {
            CuriosIntegration.init();
        }
    }

    public static int getIgnisGenAmountForState(BlockState state) {
        for (Map.Entry<Function<BlockState, Boolean>, Integer> entry : ignisGenerationAmounts.entrySet()) {
            if (entry.getKey().apply(state))
                return entry.getValue();
        }
        return 0;
    }

    public static ResourceLocation id(String obj) {
        return new ResourceLocation(MODID, obj);
    }
}
