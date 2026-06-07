package VexVisuals.module;

public final class NumberSetting extends Setting<Double> {
    private final double min;
    private final double max;
    private final double step;

    public NumberSetting(String name, String description, double defaultValue, double min, double max, double step, Module owner) {
        super(name, description,
              () -> Math.clamp(owner.getNumberStorage(name), min, max),
              v -> owner.setNumberStorage(name, Math.clamp(v, min, max)));
        this.min = min;
        this.max = max;
        this.step = step;
        set(Math.clamp(defaultValue, min, max));
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getStep() {
        return step;
    }

    @Override
    public SettingType getType() {
        return SettingType.NUMBER;
    }
}
