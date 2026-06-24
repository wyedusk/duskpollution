package dev.wyedusk.duskpollution.pollution;

import dev.wyedusk.duskpollution.DPBlocks;
import dev.wyedusk.duskpollution.DPDataMaps;
import dev.wyedusk.duskpollution.entity.DPGasEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.*;

public class PollutionManager {
    private static final Map<String, PollutionManager> INSTANCES = new HashMap<>();

    public static PollutionManager get(String dimension) {
        return INSTANCES.computeIfAbsent(dimension, l -> new PollutionManager());
    }
    public static void remove(String dimension) {
        INSTANCES.remove(dimension);
    }

    private final Set<BlockPos> emitters = new HashSet<>();
    private final Map<ChunkPos, Set<BlockPos>> emittersByChunk = new HashMap<>();
    private final Map<BlockPos, List<Integer>> timers = new HashMap<>();

    public void tick(
            ServerLevel level) {
        for (BlockPos emitterPos : emitters) {
            BlockState emitterState = level.getBlockState(emitterPos);
            List<PollutionEmitterData> emitterDatas = emitterState.getBlockHolder().getData(DPDataMaps.POLLUTION_EMITTERS);
            if (emitterDatas == null) continue;
            for (PollutionEmitterData emitterData : emitterDatas) {
                if (matches(emitterState, emitterData.conditions())) {
                    timers.putIfAbsent(emitterPos, List.of(emitterData.pollutionInterval(), emitterData.toxicPollutionInterval()));

                    int pollutionTimer = timers.get(emitterPos).getFirst();
                    int toxicPollTimer = timers.get(emitterPos).getLast();

                    if (emitterData.pollutionRelease() != 0) {
                        pollutionTimer -= 1;
                        if (pollutionTimer <= 0) {
                            pollutionTimer = emitterData.pollutionInterval();
                            level.addFreshEntity(new DPGasEntity(level,
                                    emitterPos.getX() + 0.5, emitterPos.above().getY(), emitterPos.getZ() + 0.5,
                                    DPBlocks.CARBON_BLOCK.get().defaultBlockState()));
                        }
                    }
                    if (emitterData.toxicPollutionRelease() != 0) {
                        toxicPollTimer -= 1;
                        if (toxicPollTimer <= 0) {
                            toxicPollTimer = emitterData.toxicPollutionInterval();
                            level.addFreshEntity(new DPGasEntity(level,
                                    emitterPos.getX(), emitterPos.above().getY(), emitterPos.getZ(),
                                    DPBlocks.SULFUR_BLOCK.get().defaultBlockState()));
                        }
                    }

                    timers.put(emitterPos, List.of(pollutionTimer, toxicPollTimer));

                    break;
                }
            }
        }
    }

    public void addEmitter(
            ChunkPos chunkPos, BlockPos pos) {
        emitters.add(pos);
        emittersByChunk
                .computeIfAbsent(chunkPos, p -> new HashSet<>())
                .add(pos);
    }
    public void removeEmitter(
            BlockPos pos) {
        emitters.remove(pos);
        timers.remove(pos);
    }
    public void removeChunk(
            ChunkPos pos) {
        Set<BlockPos> positions = emittersByChunk.remove(pos);
        System.out.println("Unload chunk: " + pos);
        System.out.println("Known chunks: " + emittersByChunk.keySet());
        if (positions != null) {
            emitters.removeAll(positions);
            positions.forEach(timers::remove);
        }
    }

    private boolean matches(
            BlockState state, Map<String, String> conditions) {
        for (var entry : conditions.entrySet()) {
            Property<?> property = state.getBlock()
                    .getStateDefinition()
                    .getProperty(entry.getKey());
            if (property == null) return false;
            Optional<?> value = property.getValue(entry.getValue());
            if (value.isEmpty()) return false;
            if (!state.getValue(property).equals(value.get())) return false;
        }
        return true;
    }
}
