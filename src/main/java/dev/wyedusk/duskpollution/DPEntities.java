package dev.wyedusk.duskpollution;

import dev.wyedusk.duskpollution.entity.DPGasEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DPEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, "duskpollution");

    public static final DeferredHolder<EntityType<?>, EntityType<DPGasEntity>> GAS_ENTITY =
            ENTITIES.register("gas_entity", () ->
                    EntityType.Builder.<DPGasEntity>of(DPGasEntity::new, MobCategory.MISC)
                            .sized(0.98F, 0.98F)
                            .clientTrackingRange(10)
                            .updateInterval(20)
                            .build("gas_entity")
            );

    public static void register(
            IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
