package dev.wyedusk.duskpollution.datagen;

import dev.wyedusk.duskpollution.DuskPollution;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = DuskPollution.MODID)
public class DPDataProvider implements IModBusEvent {
    @SubscribeEvent
    public static void gatherData(
            GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Clientside Datagen
        generator.addProvider(
                event.includeClient(),
                new DPBlockStateProvider(output, existingFileHelper)
        );
        generator.addProvider(
                event.includeClient(),
                new DPItemModelProvider(output, existingFileHelper)
        );

        // Serverside Datagen
        generator.addProvider(
                event.includeServer(),
                new DPBlockTagsProvider(
                        output,
                        lookupProvider,
                        existingFileHelper
                )
        );
        generator.addProvider(
                event.includeServer(),
                new DPDataMapProvider(
                        output,
                        lookupProvider
                )
        );
    }
}
