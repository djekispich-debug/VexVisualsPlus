package VexVisuals.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ModuleRegistry {
    private static final Map<ModuleType, Module> BY_TYPE = new EnumMap<>(ModuleType.class);
    private static final Map<String, Module> BY_NAME = new HashMap<>();
    private static final List<Module> ALL = new ArrayList<>();

    private ModuleRegistry() {
    }

    public static void init() {
        if (!ALL.isEmpty()) {
            return;
        }
        for (ModuleType type : ModuleType.values()) {
            Module module = new Module(type);
            BY_TYPE.put(type, module);
            BY_NAME.put(type.getName().toLowerCase(), module);
            ALL.add(module);
        }
    }

    public static Module get(ModuleType type) {
        return BY_TYPE.get(type);
    }

    public static Module getByName(String name) {
        return BY_NAME.get(name.toLowerCase());
    }

    public static List<Module> all() {
        return Collections.unmodifiableList(ALL);
    }

    public static List<Module> byCategory(Category category) {
        List<Module> list = new ArrayList<>();
        for (Module module : ALL) {
            if (module.getCategory() == category) {
                list.add(module);
            }
        }
        return list;
    }

    public static String getDescription(ModuleType type) {
        return type.getDescriptionRu();
    }

    public static int count() {
        return ALL.size();
    }
}
