package VexVisuals.module;

import java.util.Arrays;
import java.util.List;

public final class ModeSetting extends Setting<String> {
    private final List<String> modes;

    public ModeSetting(String name, String description, String defaultMode, Module owner, String... modes) {
        super(name, description, () -> owner.getModeStorage(name), v -> owner.setModeStorage(name, v));
        this.modes = List.copyOf(Arrays.asList(modes));
        set(defaultMode);
    }

    public List<String> getModes() {
        return modes;
    }

    public void cycle() {
        int idx = modes.indexOf(get());
        set(modes.get((idx + 1) % modes.size()));
    }

    @Override
    public SettingType getType() {
        return SettingType.MODE;
    }
}
