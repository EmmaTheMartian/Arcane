package martian.arcane;

import com.mojang.logging.LogUtils;
import martian.arcane.datagen.ArcaneDatagen;
import martian.arcane.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.BlastFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
        ArcaneBlockEntities.BLOCK_ENTITIES.register(modBus);
        ArcaneRecipeTypes.RECIPE_TYPES.register(modBus);
        ArcaneRecipeTypes.RECIPE_SERIALIZERS.register(modBus);
        ArcaneTabs.TABS.register(modBus);

        ignisGenerationAmounts.put(state -> state.is(Blocks.FIRE), 1);
        ignisGenerationAmounts.put(state -> state.is(Blocks.CAMPFIRE) && state.getValue(CampfireBlock.LIT), 1);
        ignisGenerationAmounts.put(state -> state.is(Blocks.LAVA_CAULDRON), 1);
        ignisGenerationAmounts.put(state -> state.is(Blocks.SOUL_FIRE), 1);
        ignisGenerationAmounts.put(state -> state.is(Blocks.SOUL_CAMPFIRE) && state.getValue(CampfireBlock.LIT), 1);
        ignisGenerationAmounts.put(state -> state.is(Blocks.FURNACE) && state.getValue(FurnaceBlock.LIT), 1);
        ignisGenerationAmounts.put(state -> state.is(Blocks.BLAST_FURNACE) && state.getValue(BlastFurnaceBlock.LIT), 1);
        ignisGenerationAmounts.put(state -> state.is(Blocks.LAVA), 2);

        modBus.addListener(EventPriority.LOWEST, ArcaneDatagen::gatherData);

        forgeBus.register(this);
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
