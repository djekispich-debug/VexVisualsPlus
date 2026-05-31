package VexVisuals.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public final class HudOverlay {
    private HudOverlay() {
    }

    public static void register() {
        // Тимчасово вимкнено через проблеми з маппінгами textRenderer.draw
        // TODO: повернути функціонал після адаптації рендерингу тексту
        HudRenderCallback.EVENT.register(HudOverlay::render);
    }

    private static void render(DrawContext context, RenderTickCounter tickCounter) {
        // Порожній метод — HUD поки не працює
        // Але гра запускається, і GUI (ClickGui) відкривається по Shift
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        // Нічого не малюємо
    }
}
