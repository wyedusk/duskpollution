package dev.wyedusk.duskpollution.block;

import dev.wyedusk.duskpollution.DPBlockEntities;
import dev.wyedusk.duskpollution.blockentity.CarbonBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CarbonBlock extends DPGasBlock {
    public CarbonBlock(
            Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(
            @NotNull BlockState state, Level level, @NotNull BlockPos pos,
            @NotNull Entity entity) {
        if (!level.isClientSide() && entity instanceof LivingEntity livingEntity) {
            double eyeY = livingEntity.getEyeY();
            if (eyeY >= pos.getY() && eyeY <= pos.getY() + 1.0) {
                livingEntity.addEffect(new MobEffectInstance(
                        MobEffects.BLINDNESS, 40, 0, false, true));
            }
        }
    }

    @Nullable @Override
    public BlockEntity newBlockEntity(
            @NotNull BlockPos pos, @NotNull BlockState state) {
        return new CarbonBlockEntity(pos, state);
    }
    @Nullable @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        if (level.isClientSide) return null;

        if (type == DPBlockEntities.CARBON_BLOCK_ENTITY.get()) {
            return (lvl, pos, blockState, be) -> CarbonBlockEntity.tick(lvl, pos, blockState, (CarbonBlockEntity) be);
        }
        return null;
    }
}
