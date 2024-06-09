package martian.arcane.datagen;

import com.klikli_dev.modonomicon.api.datagen.AbstractModonomiconLanguageProvider;
import martian.arcane.ArcaneMod;
import martian.arcane.datagen.client.ArcaneBlockStateProvider;
import martian.arcane.datagen.client.ArcaneItemModelProvider;
import martian.arcane.datagen.client.lang.ArcaneLanguageEnUsProvider;
import martian.arcane.datagen.server.ArcaneBlockTagProvider;
import martian.arcane.datagen.server.ArcaneItemTagProvider;
import martian.arcane.datagen.server.ArcaneLootTableProvider;
import martian.arcane.datagen.server.ArcaneRecipeProvider;
import martian.arcane.datagen.server.book.ArcaneBookProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class ArcaneDatagen {
    public static void gatherData(GatherDataEvent event) {
        ArcaneMod.LOGGER.info("Invoking ArcaneDatagen#gatherData");
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper efh = event.getExistingFileHelper();
        PackOutput packOutput = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        final AbstractModonomiconLanguageProvider langEnUS = new ArcaneLanguageEnUsProvider(packOutput);

        if (event.includeServer()) {
            ArcaneMod.LOGGER.info("gatherDataEvent: including server");

            gen.addProvider(true, new ArcaneRecipeProvider(packOutput, lookupProvider));
            BlockTagsProvider blockTags = gen.addProvider(true, new ArcaneBlockTagProvider(packOutput, lookupProvider, efh));
            gen.addProvider(true, new ArcaneItemTagProvider(packOutput, lookupProvider, blockTags.contentsGetter(), efh));
            gen.addProvider(true, new ArcaneLootTableProvider(packOutput, lookupProvider));
            gen.addProvider(true, new ArcaneBookProvider(packOutput, lookupProvider, langEnUS));
        }

        if (event.includeClient()) {
            ArcaneMod.LOGGER.info("gatherDataEvent: including client");

            gen.addProvider(true, new ArcaneBlockStateProvider(packOutput, efh));
            gen.addProvider(true, new ArcaneItemModelProvider(packOutput, efh));
            // Lang
            gen.addProvider(true, langEnUS);
        }
    }
}
