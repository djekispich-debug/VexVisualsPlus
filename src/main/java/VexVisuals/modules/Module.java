package VexVisuals.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;

import java.util.List;

public abstract class Module {

    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    protected static final TextRenderer textRenderer = mc.textRenderer;

    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled = false;

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    // Вызывается при активации модуля
    public void onEnable() {}

    // Вызывается при деактивации модуля
    public void onDisable() {}

    // Вызывается при рендеринге мира (3D)
    public void onWorldRender(float partialTicks) {}

    // Вызывается при рендеринге HUD (2D)
    public void onHudRender() {}

    // Вызывается при обработке игровых событий (например, получение урона)
    public void onEvent(Object event) {}

    // Получение настроек модуля (для GUI)
    public abstract List<Setting> getSettings();

    public static class Setting {
        public enum Type { BOOLEAN, INTEGER, DOUBLE, COLOR, MODE }
        public String name;
        public Type type;
        public Object value; // Boolean, Integer, Double, Color, String[] (for modes)

        public Setting(String name, Type type, Object defaultValue) {
            this.name = name;
            this.type = type;
            this.value = defaultValue;
        }
    }
}
