package martian.arcane;

import martian.arcane.api.datagen.ArcaneBlockModelProvider;
import martian.arcane.api.datagen.ArcaneBlockStateProvider;
import martian.arcane.api.datagen.ArcaneItemModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArcaneMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ArcaneClient {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper efh = event.getExistingFileHelper();
        PackOutput output = gen.getPackOutput();

//        gen.addProvider(event.includeClient(), new ArcaneBlockModelProvider(output, efh));
        gen.addProvider(event.includeClient(), new ArcaneBlockStateProvider(output, efh));
        gen.addProvider(event.includeClient(), new ArcaneItemModelProvider(output, efh));

    }
}
