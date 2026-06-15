package dev.wyedusk.duskpollution.server.event;

import dev.wyedusk.duskpollution.DPDataMaps;
import dev.wyedusk.duskpollution.DuskPollution;
import dev.wyedusk.duskpollution.pollution.PollutionManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = DuskPollution.MODID)
public class LevelEventListener {
    @SubscribeEvent
    public static void onLevelTick(
            LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel level) {
            String pmIdentifier = level.dimension().location().toString();
            PollutionManager.get(pmIdentifier).tick(level);
        }
    }

    @SubscribeEvent
    public static void onLevelUnload(
            LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel level) {
            String pmIdentifier = level.dimension().location().toString();
            PollutionManager.remove(pmIdentifier);
        }
    }

    @SubscribeEvent
    public static void onChunkLoad(
            ChunkEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel level) {
            ChunkAccess chunk = event.getChunk();
            String pmIdentifier = level.dimension().location().toString();
            PollutionManager pollutionManager = PollutionManager.get(pmIdentifier);
            chunk.findBlocks(
                    b -> b.getBlockHolder().getData(DPDataMaps.POLLUTION_EMITTERS) != null,
                    (blockPos, blockState) -> pollutionManager.addEmitter(chunk.getPos(), blockPos));
        }
    }


    @SubscribeEvent
    public static void onChunkUnload(
            ChunkEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel level) {
            ChunkAccess chunk = event.getChunk();
            String pmIdentifier = level.dimension().location().toString();
            PollutionManager pollutionManager = PollutionManager.get(pmIdentifier);
            pollutionManager.removeChunk(chunk.getPos());
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(
            BlockEvent.EntityPlaceEvent event) {
        if (event.getLevel() instanceof ServerLevel level) {
            BlockState block = event.getPlacedBlock();
            BlockPos pos = event.getPos();
            if (block.getBlockHolder().getData(DPDataMaps.POLLUTION_EMITTERS) != null) {
                String pmIdentifier = level.dimension().location().toString();
                PollutionManager pollutionManager = PollutionManager.get(pmIdentifier);
                pollutionManager.addEmitter(new ChunkPos(pos), pos);
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(
            BlockEvent.BreakEvent event) {
        if (event.getLevel() instanceof ServerLevel level) {
            BlockPos pos = event.getPos();
            String pmIdentifier = level.dimension().location().toString();
            PollutionManager pollutionManager = PollutionManager.get(pmIdentifier);
            pollutionManager.removeEmitter(pos);
        }
    }
}
