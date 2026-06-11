package dev.wyedusk.duskpollution.entity;

import dev.wyedusk.duskpollution.DPConfig;
import dev.wyedusk.duskpollution.DPEntities;
import dev.wyedusk.duskpollution.server.DPServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class DPGasEntity extends Entity {
    private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE =
            SynchedEntityData.defineId(DPGasEntity.class, EntityDataSerializers.BLOCK_STATE);

    public DPGasEntity(
            EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData(
            SynchedEntityData.@NotNull Builder builder) {
        builder.define(DATA_BLOCK_STATE, Blocks.AIR.defaultBlockState());
    }

    public DPGasEntity(
            Level level, double x, double y,
            double z, BlockState state) {
        this(DPEntities.GAS_ENTITY.get(), level);
        this.setPos(x, y, z);
        this.setBlockState(state);
    }

    public BlockState getBlockState() {
        return this.entityData.get(DATA_BLOCK_STATE);
    }

    public void setBlockState(BlockState state) {
        this.entityData.set(DATA_BLOCK_STATE, state);
    }

    @Override
    public void tick() {
        super.tick();

        this.setDeltaMovement(new Vec3(0, 0.35, 0));
        this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());

        if (this.level().isClientSide) return;

        BlockPos currentPos = this.blockPosition();
        BlockPos nextPos = currentPos.above();
        boolean reachedYMax = currentPos.getY() >= DPServerConfig.gasMaximumHeight;
        boolean hitCeiling = !this.level().getBlockState(nextPos).isAir();
        if (reachedYMax || hitCeiling) {
            if (this.level().getBlockState(currentPos).isAir()) {
                this.level().setBlock(currentPos, this.getBlockState(), 3);
            }
            this.discard();
        }
    }

    @Override
    protected void readAdditionalSaveData(
            @NotNull CompoundTag compoundTag) {
        if (compoundTag.contains("BlockState", 10)) {
            this.setBlockState(NbtUtils.readBlockState(this.level().holderLookup(net.minecraft.core.registries.Registries.BLOCK), compoundTag.getCompound("BlockState")));
        }
    }

    @Override
    protected void addAdditionalSaveData(
            @NotNull CompoundTag compoundTag) {
        compoundTag.put("BlockState", NbtUtils.writeBlockState(this.getBlockState()));
    }
}
