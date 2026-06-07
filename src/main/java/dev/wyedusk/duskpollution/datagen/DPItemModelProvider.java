package dev.wyedusk.duskpollution.datagen;

import dev.wyedusk.duskpollution.DuskPollution;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class DPItemModelProvider extends ItemModelProvider {
    public DPItemModelProvider(
            PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DuskPollution.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
    }
}