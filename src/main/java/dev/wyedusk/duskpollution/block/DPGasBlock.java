package dev.wyedusk.duskpollution.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DPGasBlock extends Block implements EntityBlock {
    public DPGasBlock(
            Properties properties) {
        super(properties);
    }

    // Rendering
    @Override
    public @NotNull VoxelShape getShape(
            @NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos,
            @NotNull CollisionContext context) {
        return Shapes.empty();
    }
    @Override
    public @NotNull VoxelShape getVisualShape(
            @NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos,
            @NotNull CollisionContext context) {
        return Shapes.block();
    }
    @Override
    public boolean skipRendering(
            @NotNull BlockState state, BlockState adjacentState, @NotNull Direction face) {
        return adjacentState.is(this);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(
            @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return null;
    }
}
