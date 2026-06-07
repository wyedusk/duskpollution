package dev.wyedusk.duskpollution.client;

import dev.wyedusk.duskpollution.DPEntities;
import dev.wyedusk.duskpollution.DuskPollution;
import dev.wyedusk.duskpollution.client.renderer.DPGasEntityRenderer;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(value = DuskPollution.MODID, dist = Dist.CLIENT)
public class DuskPollutionClient {
    public DuskPollutionClient(
            IEventBus modEventBus, ModContainer modContainer) {

    }

    @EventBusSubscriber(modid = DuskPollution.MODID, value = Dist.CLIENT)
    public static class DPClientEvents implements IModBusEvent {
        @SubscribeEvent
        public static void registerRenderers(
                EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(DPEntities.GAS_ENTITY.get(), DPGasEntityRenderer::new);
        }
    }
}
