package dev.wyedusk.duskpollution.datagen;

import dev.wyedusk.duskpollution.DPBlocks;
import dev.wyedusk.duskpollution.DuskPollution;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DPBlockTagsProvider extends BlockTagsProvider {
    public DPBlockTagsProvider(
            PackOutput output, CompletableFuture<HolderLookup.Provider> provider,
            ExistingFileHelper existingFileHelper) {
        super(output, provider, DuskPollution.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(
            HolderLookup.@NotNull Provider provider) {
        tag(DPBlocks.POLLUTANTS)
                .add(DPBlocks.CARBON_BLOCK.get());
    }
}
