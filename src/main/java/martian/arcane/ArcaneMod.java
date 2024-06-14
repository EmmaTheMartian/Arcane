package martian.arcane;

import com.mojang.logging.LogUtils;
import martian.arcane.api.ArcaneRegistries;
import martian.arcane.client.ArcaneClient;
import martian.arcane.common.ArcaneContent;
import martian.arcane.common.networking.c2s.C2SOpenEnderpackPayload;
import martian.arcane.common.networking.c2s.C2SSetSelectionComponent;
import martian.arcane.common.networking.s2c.S2CSyncAuraAttachment;
import martian.arcane.datagen.ArcaneDatagen;
import martian.arcane.integration.curios.CuriosIntegration;
import martian.arcane.integration.kubejs.KubeJSIntegration;
import martian.arcane.integration.photon.PhotonIntegration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.BlastFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Mod(ArcaneMod.MODID)
public class ArcaneMod {
    public static final String MODID = "arcane";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final Map<Function<BlockState, Boolean>, Integer> ignisGenerationAmounts = new HashMap<>();

    public ArcaneMod(ModContainer mod, IEventBus modBus) {
        mod.registerConfig(ModConfig.Type.SERVER, ArcaneConfig.SPEC);

        ArcaneContent.init(modBus);

        modBus.addListener(FMLCommonSetupEvent.class, event -> {
            CuriosIntegration.INSTANCE.load();
            PhotonIntegration.INSTANCE.load();
            KubeJSIntegration.INSTANCE.load();
        });

        modBus.addListener(RegisterPayloadHandlersEvent.class, event -> {
            final PayloadRegistrar registrar = event.registrar("1");
            registrar.playToServer(C2SOpenEnderpackPayload.TYPE, C2SOpenEnderpackPayload.CODEC, C2SOpenEnderpackPayload::handler);
            registrar.playToServer(C2SSetSelectionComponent.TYPE, C2SSetSelectionComponent.CODEC, C2SSetSelectionComponent::handler);
            registrar.playToClient(S2CSyncAuraAttachment.TYPE, S2CSyncAuraAttachment.CODEC, S2CSyncAuraAttachment::handler);
        });

        modBus.addListener(NewRegistryEvent.class, event -> event.register(ArcaneRegistries.SPELLS));

        modBus.addListener(ArcaneDatagen::gatherData);

        if (FMLEnvironment.dist.isClient())
            ArcaneClient.setup(modBus);

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
        ignisGenerationAmounts.put(state -> state.is(ArcaneContent.SOUL_MAGMA.get()), 3);
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
