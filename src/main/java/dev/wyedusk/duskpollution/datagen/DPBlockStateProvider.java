package dev.wyedusk.duskpollution.datagen;

import dev.wyedusk.duskpollution.DPBlocks;
import dev.wyedusk.duskpollution.DuskPollution;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class DPBlockStateProvider extends BlockStateProvider {
    public DPBlockStateProvider(
            PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DuskPollution.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(DPBlocks.CARBON_BLOCK.get(), getModelOfGasBlock(DPBlocks.CARBON_BLOCK));
        simpleBlockWithItem(DPBlocks.SULFUR_BLOCK.get(), getModelOfGasBlock(DPBlocks.SULFUR_BLOCK));
    }

    private BlockModelBuilder getModelOfGasBlock(
            DeferredBlock<Block> block) {
        return models()
                .withExistingParent(block.getId().toString(), "minecraft:block/cube_all")
                .texture("all", blockTexture(block.get()))
                .element()
                    .from(0, 0, 0)
                    .to(16, 16, 16)
                    .face(Direction.DOWN).cullface(Direction.DOWN).texture("#all").end()
                    .face(Direction.UP).cullface(Direction.UP).texture("#all").end()
                    .face(Direction.NORTH).cullface(Direction.NORTH).texture("#all").end()
                    .face(Direction.SOUTH).cullface(Direction.SOUTH).texture("#all").end()
                    .face(Direction.WEST).cullface(Direction.WEST).texture("#all").end()
                    .face(Direction.EAST).cullface(Direction.EAST).texture("#all").end()
                    .end()
                .renderType("minecraft:translucent");
    }
}