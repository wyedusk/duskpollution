package dev.wyedusk.duskpollution;

import dev.wyedusk.duskpollution.pollution.PollutionEmitterData;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import java.util.List;

@EventBusSubscriber(modid = DuskPollution.MODID)
public class DPDataMaps implements IModBusEvent {
    public static final DataMapType<Block, List<PollutionEmitterData>> POLLUTION_EMITTERS = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(DuskPollution.MODID, "pollution_emitters"),
            Registries.BLOCK, PollutionEmitterData.CODEC.listOf()).build();

    @SubscribeEvent
    public static void registerDataMapTypes(
            RegisterDataMapTypesEvent event) {
        event.register(POLLUTION_EMITTERS);
    }
}
