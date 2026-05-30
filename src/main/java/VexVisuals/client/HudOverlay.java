package VexVisuals.client;

import VexVisuals.gui.ClickGuiScreen;
import VexVisuals.module.Module;
import VexVisuals.module.ModuleRegistry;
import VexVisuals.module.ModuleType;
import VexVisuals.util.ColorManager;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public final class HudOverlay {
    private static final DateTimeFormatter CLOCK = DateTimeFormatter.ofPattern("HH:mm:ss");

    private HudOverlay() {
    }

    public static void register() {
        HudRenderCallback.EVENT.register(HudOverlay::render);
    }

    private static void render(GuiGraphics g, net.minecraft.client.DeltaTracker tickCounter) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.player == null) {
            return;
        }
        int y = 4;
        if (isOn(ModuleType.WATERMARK) || isOn(ModuleType.REAL_TIME_CLOCK)) {
            String title = ClickGuiScreen.CLIENT_TITLE;
            String nick = mc.player.getName().getString();
            String time = LocalTime.now().format(CLOCK);
            String line = title + "  |  " + nick + "  |  " + time;
            int w = mc.font.width(line) + 10;
            g.fill(4, y, 4 + w, y + 14, 0x90000000);
            g.drawString(mc.font, line, 8, y + 3, ColorManager.chroma(System.currentTimeMillis(), 1));
            y += 16;
        }
        if (isOn(ModuleType.ARRAY_LIST)) {
            List<Module> enabled = ModuleRegistry.all().stream()
                    .filter(Module::isEnabled)
                    .sorted(Comparator.comparingInt(m -> -mc.font.width(m.getName())))
                    .toList();
            int row = y;
            for (Module module : enabled) {
                g.drawString(mc.font, module.getName(), mc.getWindow().getGuiScaledWidth() - mc.font.width(module.getName()) - 6,
                        row, ColorManager.chroma(System.currentTimeMillis(), row * 3));
                row += 10;
            }
        }
    }

    private static boolean isOn(ModuleType type) {
        Module m = ModuleRegistry.get(type);
        return m != null && m.isEnabled();
    }
}
