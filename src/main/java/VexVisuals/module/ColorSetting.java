package VexVisuals.module;

public final class ColorSetting extends Setting<Integer> {
    public ColorSetting(String name, String description, int defaultArgb, Module owner) {
        super(name, description, () -> owner.getColorStorage(name), v -> owner.setColorStorage(name, v));
        set(defaultArgb);
    }

    @Override
    public SettingType getType() {
        return SettingType.COLOR;
    }
}
