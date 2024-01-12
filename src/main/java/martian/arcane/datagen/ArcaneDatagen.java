package martian.arcane.datagen;

import martian.arcane.ArcaneMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

public class ArcaneDatagen {
    public static void gatherData(GatherDataEvent event) {
        ArcaneMod.LOGGER.info("Invoking ArcaneDatagen#gatherData");
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper efh = event.getExistingFileHelper();
        PackOutput output = gen.getPackOutput();

        if (event.includeClient()) {
            ArcaneMod.LOGGER.info("gatherDataEvent: including client");
            gen.addProvider(true, new ArcaneBlockStateProvider(output, efh));
            gen.addProvider(true, new ArcaneItemModelProvider(output, efh));
        }

        if (event.includeServer()) {
            ArcaneMod.LOGGER.info("gatherDataEvent: including server");
            gen.addProvider(true, new ArcaneRecipeProvider(output));
        }
    }
}
