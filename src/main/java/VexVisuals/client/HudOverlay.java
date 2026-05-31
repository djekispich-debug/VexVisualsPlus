package VexVisuals.client;

import VexVisuals.gui.ClickGuiScreen;
import VexVisuals.module.Module;
import VexVisuals.module.ModuleRegistry;
import VexVisuals.module.ModuleType;
import VexVisuals.util.ColorManager;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

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

    private static void render(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options.hideGui || mc.player == null) {
            return;
        }
        int y = 4;
        if (isOn(ModuleType.WATERMARK) || isOn(ModuleType.REAL_TIME_CLOCK)) {
            String title = ClickGuiScreen.CLIENT_TITLE;
            String nick = mc.player.getName().getString();
            String time = LocalTime.now().format(CLOCK);
            String line = title + "  |  " + nick + "  |  " + time;
            int w = mc.textRenderer.getWidth(line) + 10;
            context.fill(4, y, 4 + w, y + 14, 0x90000000);
            context.drawText(mc.textRenderer, line, 8, y + 3, ColorManager.chroma(System.currentTimeMillis(), 1), true);
            y += 16;
        }
        if (isOn(ModuleType.ARRAY_LIST)) {
            List<Module> enabled = ModuleRegistry.all().stream()
                    .filter(Module::isEnabled)
                    .sorted(Comparator.comparingInt(m -> -mc.textRenderer.getWidth(m.getName())))
                    .toList();
            int row = y;
            for (Module module : enabled) {
                context.drawText(mc.textRenderer, module.getName(), mc.getWindow().getScaledWidth() - mc.textRenderer.getWidth(module.getName()) - 6,
                        row, ColorManager.chroma(System.currentTimeMillis(), row * 3), true);
                row += 10;
            }
        }
    }

    private static boolean isOn(ModuleType type) {
        Module m = ModuleRegistry.get(type);
        return m != null && m.isEnabled();
    }
}
