package VexVisuals.module;

public final class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name, String description, boolean defaultValue, Module owner) {
        super(name, description, () -> owner.getBooleanStorage(name), v -> owner.setBooleanStorage(name, v));
        set(defaultValue);
    }

    @Override
    public SettingType getType() {
        return SettingType.BOOLEAN;
    }
}
