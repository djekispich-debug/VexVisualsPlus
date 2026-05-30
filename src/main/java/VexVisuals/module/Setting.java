package VexVisuals.module;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Setting<T> {
    private final String name;
    private final String description;
    private final Supplier<T> getter;
    private final Consumer<T> setter;

    protected Setting(String name, String description, Supplier<T> getter, Consumer<T> setter) {
        this.name = name;
        this.description = description;
        this.getter = getter;
        this.setter = setter;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public T get() {
        return getter.get();
    }

    public void set(T value) {
        setter.accept(value);
    }

    public enum SettingType {
        BOOLEAN, NUMBER, MODE, COLOR
    }

    public abstract SettingType getType();
}
