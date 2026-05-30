package VexVisuals.module;

import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Module {
    private final ModuleType type;
    private boolean enabled;
    private int keyBind = GLFW.GLFW_KEY_UNKNOWN;
    private final List<Setting<?>> settings = new ArrayList<>();
    private final Map<String, Boolean> booleans = new HashMap<>();
    private final Map<String, Double> numbers = new HashMap<>();
    private final Map<String, String> modes = new HashMap<>();
    private final Map<String, Integer> colors = new HashMap<>();

    public Module(ModuleType type) {
        this.type = type;
        type.applyDefaultSettings(this);
    }

    public ModuleType getType() {
        return type;
    }

    public String getName() {
        return type.getName();
    }

    public String getDescription() {
        return type.getDescriptionRu();
    }

    public Category getCategory() {
        return type.getCategory();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            type.onEnable(this);
        } else {
            type.onDisable(this);
        }
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public int getKeyBind() {
        return keyBind;
    }

    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    public void addSetting(Setting<?> setting) {
        settings.add(setting);
    }

    boolean getBooleanStorage(String key) {
        return booleans.getOrDefault(key, false);
    }

    void setBooleanStorage(String key, boolean value) {
        booleans.put(key, value);
    }

    double getNumberStorage(String key) {
        return numbers.getOrDefault(key, 0.0);
    }

    void setNumberStorage(String key, double value) {
        numbers.put(key, value);
    }

    String getModeStorage(String key) {
        return modes.getOrDefault(key, "");
    }

    void setModeStorage(String key, String value) {
        modes.put(key, value);
    }

    int getColorStorage(String key) {
        return colors.getOrDefault(key, 0xFFFFFFFF);
    }

    void setColorStorage(String key, int value) {
        colors.put(key, value);
    }
}
