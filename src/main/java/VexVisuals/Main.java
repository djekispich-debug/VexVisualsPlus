package VexVisuals;

import VexVisuals.client.ClientEvents;
import VexVisuals.client.HudOverlay;
import VexVisuals.module.ModuleRegistry;
import VexVisuals.module.ModuleType;
import net.fabricmc.api.ClientModInitializer;

public class Main implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModuleRegistry.init();
        ClientEvents.register();
        HudOverlay.register();
        ModuleRegistry.get(ModuleType.WATERMARK).setEnabled(true);
        ModuleRegistry.get(ModuleType.REAL_TIME_CLOCK).setEnabled(true);
    }
}
