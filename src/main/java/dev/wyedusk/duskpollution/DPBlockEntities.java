package dev.wyedusk.duskpollution;

import dev.wyedusk.duskpollution.blockentity.CarbonBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DPBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, "duskpollution");

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CarbonBlockEntity>> CARBON_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("carbon_block_entity", () ->
                    BlockEntityType.Builder.of(CarbonBlockEntity::new, DPBlocks.CARBON_BLOCK.get()).build(null)
            );

    public static void register(
            IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}