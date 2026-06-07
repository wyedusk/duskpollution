package dev.wyedusk.duskpollution.event;

import dev.wyedusk.duskpollution.DPBlocks;
import dev.wyedusk.duskpollution.DPConfig;
import dev.wyedusk.duskpollution.DuskPollution;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = DuskPollution.MODID)
public class PlayerEventListener implements IModBusEvent {
    @SubscribeEvent
    public static void onPlayerTick(
            PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        BlockPos pos = player.blockPosition();
        Level level = player.level();

        if (level.isClientSide) return;
        if ((player.getId() + level.getGameTime()) % 20 != 0) return;

        int pollutants = 0;
        int toxicPollutants = 0;

        ChunkPos centerChunkPos = player.chunkPosition();
        if (level.canSeeSkyFromBelowWater(pos)) {
            // Player is on the surface, do surface pollution check
            for (int chunkX = centerChunkPos.x - 1; chunkX <= centerChunkPos.x + 1; chunkX++) {
                for (int chunkZ = centerChunkPos.z - 1; chunkZ <= centerChunkPos.z + 1; chunkZ++) {
                    LevelChunk chunk = level.getChunk(chunkX, chunkZ);
                    for (int localX = 0; localX < 16; localX++) {
                        for (int localZ = 0; localZ < 16; localZ++) {
                            for (int yOffset = 0; yOffset < 4; yOffset++) {
                                BlockPos queryPos = new BlockPos(
                                        chunkX * 16 + localX,
                                        DPConfig.gasMaximumHeight - yOffset,
                                        chunkZ * 16 + localZ
                                );
                                BlockState state = level.getBlockState(queryPos);
                                if (state.is(DPBlocks.POLLUTANTS)) pollutants++;
                                if (state.is(DPBlocks.POLLUTANTS_TOXIC)) toxicPollutants++;
                            }
                        }
                    }
                }
            }
        }

        // Define pollution effects
        List<Optional<Tuple<Holder<MobEffect>, Tuple<Integer, Integer>>>> pollutantEffects = List.of(
                Optional.empty(),                                                                // T0
                Optional.of(new Tuple<>(MobEffects.WEAKNESS, new Tuple<>(0, 7))),          // T1
                Optional.of(new Tuple<>(MobEffects.MOVEMENT_SLOWDOWN, new Tuple<>(0, 4))), // T2
                Optional.of(new Tuple<>(MobEffects.DIG_SLOWDOWN, new Tuple<>(0, 7))),      // T3
                Optional.of(new Tuple<>(MobEffects.CONFUSION, new Tuple<>(1, 3)))          // T4
        );
        List<Optional<Tuple<Holder<MobEffect>, Tuple<Integer, Integer>>>> toxicPollutantEffects = List.of(
                Optional.empty(),                                                     // T0
                Optional.of(new Tuple<>(MobEffects.HUNGER, new Tuple<>(0, 6))), // T1
                Optional.of(new Tuple<>(MobEffects.POISON, new Tuple<>(0, 4))), // T2
                Optional.of(new Tuple<>(MobEffects.WITHER, new Tuple<>(0, 2)))  // T3
        );

        // Calculate pollution tiers based on effects list
        int pollutantTiers = pollutantEffects.size() - 1;
        int toxicPollutantTiers = toxicPollutantEffects.size() - 1;
        int pollutantTier = (int) Math.floor((pollutants / 600.0d) * pollutantTiers);
        int toxicPollutantTier = (int) Math.floor((toxicPollutants / 200.0d) * toxicPollutantTiers);
        pollutantTier = Math.max(pollutantTier, 0);
        toxicPollutantTier = Math.max(toxicPollutantTier, 0);

        player.sendSystemMessage(Component.literal(String.format("Pollution Tier: %s | Toxicity Tier: %s", pollutantTier, toxicPollutantTier)));

        // Apply effects
        for (int pi = 0; pi <= Math.min(pollutantTiers, pollutantTier); pi++) {
            int add = Math.max(0, (pollutantTier - pi) - 1);
            pollutantEffects.get(pi).ifPresent(effect ->
                    player.addEffect(new MobEffectInstance(
                            effect.getA(),
                            40,
                            Math.min(effect.getB().getA() + add, effect.getB().getB()),
                            true,
                            false,
                            true
                    ))
            );
        }
        for (int tpi = 0; tpi <= Math.min(toxicPollutantTiers, toxicPollutantTier); tpi++) {
            int add = Math.max(0, (toxicPollutantTier - tpi) - 1);
            toxicPollutantEffects.get(tpi).ifPresent(effect ->
                    player.addEffect(new MobEffectInstance(
                            effect.getA(),
                            40,
                            Math.min(effect.getB().getA() + add, effect.getB().getB()),
                            true,
                            false,
                            true
                    ))
            );
        }
    }
}
