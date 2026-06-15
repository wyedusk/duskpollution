package dev.wyedusk.duskpollution.datagen;

import dev.wyedusk.duskpollution.DPDataMaps;
import dev.wyedusk.duskpollution.pollution.PollutionEmitterData;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DPDataMapProvider extends DataMapProvider {
    public DPDataMapProvider(
            PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    protected void gather(HolderLookup.@NotNull Provider provider) {
        // Campfires
        addBasicPollutionEmitter(Blocks.CAMPFIRE, List.of(
                new PollutionEmitterData(
                        Map.of("lit", "true"),
                        1,
                        1,
                        80,
                        3000)
        ));
        addBasicPollutionEmitter(Blocks.SOUL_CAMPFIRE, List.of(
                new PollutionEmitterData(
                        Map.of("lit", "true"),
                        1,
                        1,
                        80,
                        3000)
        ));
        // Furnaces & Furnace Variants
        addBasicPollutionEmitter(Blocks.FURNACE, List.of(
                 new PollutionEmitterData(
                         Map.of("lit", "true"),
                         1,
                         1,
                         300,
                         9000)
        ));
        addBasicPollutionEmitter(Blocks.SMOKER, List.of(
                new PollutionEmitterData(
                        Map.of("lit", "true"),
                        1,
                        0,
                        250,
                        0)
        ));
        addBasicPollutionEmitter(Blocks.BLAST_FURNACE, List.of(
                new PollutionEmitterData(
                        Map.of("lit", "true"),
                        1,
                        1,
                        275,
                        900)
        ));
    }

    private void addBasicPollutionEmitter(
            Block block, List<PollutionEmitterData> data) {
        builder(DPDataMaps.POLLUTION_EMITTERS)
                .add(block.defaultBlockState().getBlockHolder(), data, false);
    }
}
