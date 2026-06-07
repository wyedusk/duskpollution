package dev.wyedusk.duskpollution.blockentity;

import dev.wyedusk.duskpollution.DPBlockEntities;
import dev.wyedusk.duskpollution.DPConfig;
import dev.wyedusk.duskpollution.entity.DPGasEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SulfurBlockEntity extends BlockEntity {
    private int life = 0;
    private int skyMoves = 0;
    private final int tickOffset;

    public SulfurBlockEntity(
            BlockPos pos, BlockState state) {
        super(DPBlockEntities.SULFUR_BLOCK_ENTITY.get(), pos, state);
        this.tickOffset = RandomSource.create().nextInt(-20, 20);
    }

    public void setSkyMoves(int skyMoves) {
        this.skyMoves = skyMoves;
    }

    public static void tick(
            Level level, BlockPos pos, BlockState state,
            SulfurBlockEntity blockEntity) {
        if (level.isClientSide) return;

        BlockPos nextPos = pos.above();
        boolean reachedYMax = pos.getY() >= DPConfig.gasMaximumHeight;
        boolean hitCeiling = !level.getBlockState(nextPos).isAir();

        blockEntity.life++;
        if (blockEntity.life >= (60 + blockEntity.tickOffset)) {
            if (!(reachedYMax || hitCeiling)) {
                DPGasEntity gasEntity = new DPGasEntity(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, state);
                level.addFreshEntity(gasEntity);
                level.removeBlock(pos, false);
            } else {
                BlockPos escapePos = findHorizontalEscape(level, pos);
                if (escapePos != null) {
                    if (reachedYMax) blockEntity.skyMoves++;
                    if (DPConfig.gasCanNaturallyDissipate) {
                        if (blockEntity.skyMoves >= DPConfig.movementsBeforeGasCanDissipate) {
                            float baseChance = DPConfig.baseDissipationChance;
                            int additionalMoves = blockEntity.skyMoves - DPConfig.movementsBeforeGasCanDissipate;
                            float currentChance = baseChance + (additionalMoves * DPConfig.dissipationChanceIncrease);
                            if (level.getRandom().nextFloat() < currentChance) {
                                level.removeBlock(pos, false);
                                return;
                            }
                        }
                    }
                    if (level.getBlockState(escapePos).isAir()) {
                        level.setBlock(escapePos, state, 3);
                        BlockEntity newBe = level.getBlockEntity(escapePos);
                        if (newBe instanceof SulfurBlockEntity newSulfurBe) {
                            newSulfurBe.setSkyMoves(blockEntity.skyMoves);
                        }
                        level.removeBlock(pos, false);
                    }
                } else {
                    blockEntity.life = 0;
                }
            }
        }
    }

    @Nullable
    private static BlockPos findHorizontalEscape(Level level, BlockPos centerPos) {
        int maxRadius = 3;
        for (int radius = 1; radius <= maxRadius; radius++) {
            List<BlockPos> positions = new ArrayList<>();
            for (int xOffset = -radius; xOffset <= radius; xOffset++) {
                for (int zOffset = -radius; zOffset <= radius; zOffset++) {
                    if (Math.abs(xOffset) == radius || Math.abs(zOffset) == radius) {
                        positions.add(centerPos.offset(xOffset, 0, zOffset));
                    }
                }
            }
            Collections.shuffle(positions);
            for (BlockPos checkPos : positions) {
                if (level.getBlockState(checkPos).isAir()) {
                    return checkPos;
                }
            }
        }
        return null;
    }
}
