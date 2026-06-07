package VexVisuals.module;

public final class ColorSetting extends Setting<Integer> {
    public ColorSetting(String name, String description, int defaultColor, Module owner) {
        super(name, description,
              () -> owner.getColorStorage(name),
              v -> owner.setColorStorage(name, v));
        set(defaultColor);
    }

    @Override
    public SettingType getType() {
        return SettingType.COLOR;
    }

    public static int argbToInt(int argb) {
        return argb;
    }

    public static int[] intToArgb(int color) {
        return new int[]{
            (color >> 24) & 0xFF,  // A
            (color >> 16) & 0xFF,  // R
            (color >> 8) & 0xFF,   // G
            color & 0xFF            // B
        };
    }
}
