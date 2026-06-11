package dev.wyedusk.duskpollution.server;

import dev.wyedusk.duskpollution.DuskPollution;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(value = DuskPollution.MODID)
public class DuskPollutionServer {
    public DuskPollutionServer(
            IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.SERVER, DPServerConfig.SPEC);
    }
}
