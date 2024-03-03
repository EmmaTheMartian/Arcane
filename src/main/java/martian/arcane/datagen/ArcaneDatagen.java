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
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class ArcaneDatagen {
    public static void gatherData(GatherDataEvent event) {
        ArcaneMod.LOGGER.info("Invoking ArcaneDatagen#gatherData");
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper efh = event.getExistingFileHelper();
        PackOutput output = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        final AbstractModonomiconLanguageProvider langEnUS = new ArcaneLanguageEnUsProvider(output);

        if (event.includeServer()) {
            ArcaneMod.LOGGER.info("gatherDataEvent: including server");
            gen.addProvider(true, new ArcaneRecipeProvider(output));
            BlockTagsProvider blockTags = gen.addProvider(true, new ArcaneBlockTagProvider(output, lookupProvider, efh));
            gen.addProvider(true, new ArcaneItemTagProvider(output, lookupProvider, blockTags.contentsGetter(), efh));
            gen.addProvider(true, new ArcaneLootTableProvider(output));
            gen.addProvider(true, new ArcaneBookProvider(output, langEnUS));
        }

        if (event.includeClient()) {
            ArcaneMod.LOGGER.info("gatherDataEvent: including client");
            gen.addProvider(true, new ArcaneBlockStateProvider(output, efh));
            gen.addProvider(true, new ArcaneItemModelProvider(output, efh));
            // Lang
            gen.addProvider(true, langEnUS);
        }
    }
}
