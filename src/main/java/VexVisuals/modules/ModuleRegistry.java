package VexVisuals.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleRegistry {

    // Структура для хранения информации о модуле
    public static class ModuleInfo {
        public final String name;
        public final String description;
        public final Category category;

        public ModuleInfo(String name, String description, Category category) {
            this.name = name;
            this.description = description;
            this.category = category;
        }
    }

    // Легальные категории модулей
    public enum Category {
        HUD_UI("HUD / UI", "Элементы интерфейса и HUD"),
        INDICATORS_GRAPH("Indicators / Graph", "Графики и индикаторы производительности и состояния"),
        COMBAT_VISUALS("Combat Visuals (Legal)", "Легальная информация для боя"),
        COSMETICS("Cosmetics", "Визуальная косметика для игрока"),
        WORLD_STYLE("World Style", "Атмосфера мира и оптимизация графики"),
        SCREEN_CAMERA("Screen / Camera", "Настройки камеры и экрана");

        public final String displayName;
        public final String description;

        Category(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
    }

    // HashMap для хранения всех модулей и их информации
    private static final Map<String, ModuleInfo> MODULES = new HashMap<>();

    // Инициализация всех легальных модулей
    public static void initialize() {
        // HUD / UI (1-15)
        register(new ModuleInfo("ArrayList", "Отображает список активных модулей, отсортированный по длине названия.", Category.HUD_UI));
        register(new ModuleInfo("Watermark", "Отображает название клиента и версию.", Category.HUD_UI));
        register(new ModuleInfo("ActiveModules", "Список активных модулей (альтернатива ArrayList).", Category.HUD_UI));
        register(new ModuleInfo("KeyStrokes", "Отображает нажатые клавиши.", Category.HUD_UI));
        register(new ModuleInfo("FPSDisplay", "Отображает текущий FPS.", Category.HUD_UI));
        register(new ModuleInfo("BPSDisplay", "Отображает текущую скорость перемещения (Blocks Per Second).", Category.HUD_UI));
        register(new ModuleInfo("PingDisplay", "Отображает пинг сервера.", Category.HUD_UI));
        register(new ModuleInfo("ArmorStatus", "Показывает прочность брони.", Category.HUD_UI));
        register(new ModuleInfo("InventoryViewer", "Отображает инвентарь игрока на экране.", Category.HUD_UI));
        register(new ModuleInfo("PotionStatus", "Показывает активные зелья.", Category.HUD_UI));
        register(new ModuleInfo("CoordinateDisplay", "Отображает текущие координаты.", Category.HUD_UI));
        register(new ModuleInfo("ServerBrand", "Отображает бренд сервера.", Category.HUD_UI));
        register(new ModuleInfo("DirectionHUD", "Отображает направление взгляда игрока (компас).", Category.HUD_UI));
        register(new ModuleInfo("SessionInfo", "Отображает статистику текущей игровой сессии.", Category.HUD_UI));
        register(new ModuleInfo("RealTimeClock", "Отображает реальное время.", Category.HUD_UI));

        // Combat Visuals (Legal) (16-30)
        register(new ModuleInfo("HitParticles", "Отображает красивые и плавные частицы при попадании по цели.", Category.COMBAT_VISUALS));
        register(new ModuleInfo("AttackProgress", "Визуальный индикатор прогресса атаки.", Category.COMBAT_VISUALS));
        register(new ModuleInfo("ShieldDamageVisual", "Анимация, показывающая, когда щит получает урон.", Category.COMBAT_VISUALS));
        register(new ModuleInfo("WeaponSwingCustomizer", "Плавная и настраиваемая анимация взмаха оружием.", Category.COMBAT_VISUALS));
        register(new ModuleInfo("NoHurtCam", "Снижает интенсивность тряски камеры при получении урона.", Category.COMBAT_VISUALS));
        register(new ModuleInfo("TotemPopCounter", "Текстовый счетчик срабатываний тотема (в чате или HUD).", Category.COMBAT_VISUALS));
        register(new ModuleInfo("VelocityGraph", "Отображает график получаемой игроком скорости (легальная информация).", Category.COMBAT_VISUALS));
        register(new ModuleInfo("SwingSpeed", "Позволяет настроить скорость выполнения взмахов оружием.", Category.COMBAT_VISUALS));
        register(new ModuleInfo("CrosshairCustomizer", "Кастомизирует прицел (форма, цвет, размер).", Category.COMBAT_VISUALS));
        register(new ModuleInfo("SharpnessParticles", "Добавляет красивые частицы к эффекту зачарования 'Острота'.", Category.COMBAT_VISUALS));
        register(new ModuleInfo("EnchantGlowCustomizer", "Позволяет настроить цвет свечения чар на предметах.", Category.COMBAT_VISUALS));
        register(new ModuleInfo("CustomHitColor", "Изменяет цвет игрока при получении урона (визуальный эффект).", Category.COMBAT_VISUALS));
        register(new ModuleInfo("BlockReachIndicator", "Визуально показывает дистанцию до блока, который можно сломать/поставить.", Category.COMBAT_VISUALS));
        register(new ModuleInfo("ShieldBreakNotify", "Уведомление о поломке щита.", Category.COMBAT_VISUALS));
        register(new ModuleInfo("LowHPGlow", "Придает игроку мягкое свечение при низком HP.", Category.COMBAT_VISUALS));

        // Indicators & Graphs (31-45)
        register(new ModuleInfo("TargetInfo", "Отображает информацию о цели в поле зрения (ник, HP, пинг).", Category.INDICATORS_GRAPH));
        register(new ModuleInfo("JumpCircle", "Отображает 3D-круг на земле при прыжке игрока.", Category.INDICATORS_GRAPH));
        register(new ModuleInfo("LandCircle", "Визуальный эффект при приземлении игрока.", Category.INDICATORS_GRAPH));
        register(new ModuleInfo("VelocityIndicator", "Визуальный индикатор ускорения/замедления игрока.", Category.INDICATORS_GRAPH));
        register(new ModuleInfo("CPSViewer", "Отображает количество кликов в секунду (CPS).", Category.INDICATORS_GRAPH));
        register(new ModuleInfo("EntitySpeedGraph", "График скорости для сущностей (лошади, элитры).", Category.INDICATORS_GRAPH));
        register(new ModuleInfo("CoolDownOverlay", "Визуализирует кулдауны предметов/действий.", Category.INDICATORS_GRAPH));
        register(new ModuleInfo("InventoryFullWarning", "Предупреждение о полном инвентаре.", Category.INDICATORS_GRAPH));
        register(new ModuleInfo("TotemWarning", "Напоминание взять тотем в руку.", Category.INDICATORS_GRAPH));
        register(new ModuleInfo("PingIndicator", "График задержки сети (пинг).", Category.INDICATORS_GRAPH));
        register(new ModuleInfo("ComboCounter", "Счетчик серии успешных ударов.", Category.INDICATORS_GRAPH));
        register(new ModuleInfo("GappleCooldown", "Таймер перезарядки золотого яблока.", Category.INDICATORS_GRAPH));
        register(new ModuleInfo("PearlCooldown", "Таймер перезарядки эндер-жемчуга.", Category.INDICATORS_GRAPH));
        register(new ModuleInfo("PotionsGrid", "Отображает активные зелья в виде сетки.", Category.INDICATORS_GRAPH));
        register(new ModuleInfo("MemoryUsage", "Отображает использование оперативной памяти клиентом.", Category.INDICATORS_GRAPH));

        // Cosmetics (46-60)
        register(new ModuleInfo("ChinaHat", "Светящаяся шляпа-конус на голове игрока.", Category.COSMETICS));
        register(new ModuleInfo("CapeCustomizer", "Позволяет настроить кастомный плащ игрока.", Category.COSMETICS));
        register(new ModuleInfo("CustomWings", "Анимированные крылья за спиной игрока.", Category.COSMETICS));
        register(new ModuleInfo("ParticlesMultiplier", "Увеличивает количество легальных частиц (для красоты).", Category.COSMETICS));
        register(new ModuleInfo("RainbowEnchant", "Делает эффекты зачарований радужными.", Category.COSMETICS));
        register(new ModuleInfo("FireworkTrails", "Оставляет за игроком следы из фейерверков.", Category.COSMETICS));
        register(new ModuleInfo("TrailEffects", "Рисует линии за игроком при беге.", Category.COSMETICS));
        register(new ModuleInfo("CustomAuraEffects", "Настраиваемые эффекты вокруг игрока.", Category.COSMETICS));
        register(new ModuleInfo("WeaponSize", "Изменяет размер оружия в руке игрока.", Category.COSMETICS));
        register(new ModuleInfo("ShieldPosition", "Позволяет настроить положение щита в руке.", Category.COSMETICS));
        register(new ModuleInfo("CustomEmotesVisual", "Визуальное отображение кастомных эмоций.", Category.COSMETICS));
        register(new ModuleInfo("ItemPhysics", "Реалистичное поведение дропнутых предметов.", Category.COSMETICS));
        register(new ModuleInfo("CustomShieldPattern", "Позволяет использовать кастомные узоры для щитов.", Category.COSMETICS));
        register(new ModuleInfo("ArmorHide", "Позволяет скрывать броню (для скриншотов).", Category.COSMETICS));
        register(new ModuleInfo("NameTagFormat", "Применяет красивый шрифт к своему нику.", Category.COSMETICS));

        // World Style (61-75)
        register(new ModuleInfo("FullBright", "Легальное увеличение яркости/гаммы без изменения освещения мира.", Category.WORLD_STYLE));
        register(new ModuleInfo("TimeChanger", "Визуальная смена времени суток (не влияет на сервер).", Category.WORLD_STYLE));
        register(new ModuleInfo("WeatherChanger", "Визуальная смена погоды (не влияет на сервер).", Category.WORLD_STYLE));
        register(new ModuleInfo("AmbienceColor", "Настраивает цвет неба и тумана.", Category.WORLD_STYLE));
        register(new ModuleInfo("SkyboxCustomizer", "Позволяет использовать кастомные текстуры неба.", Category.WORLD_STYLE));
        register(new ModuleInfo("NightVision", "Эффект ночного зрения без зелья.", Category.WORLD_STYLE));
        register(new ModuleInfo("NoWeather", "Отключает отображение дождя/снега для повышения FPS.", Category.WORLD_STYLE));
        register(new ModuleInfo("BlockOverlay", "Красивая подсветка выделенного блока.", Category.WORLD_STYLE));
        register(new ModuleInfo("ChunkBorder", "Отображает границы чанков.", Category.WORLD_STYLE));
        register(new ModuleInfo("CustomStars", "Настройка размера и количества звезд на небе.", Category.WORLD_STYLE));
        register(new ModuleInfo("SunSize", "Изменяет размер солнца.", Category.WORLD_STYLE));
        register(new ModuleInfo("MoonCustomizer", "Позволяет использовать кастомные текстуры луны.", Category.WORLD_STYLE));
        register(new ModuleInfo("FogDensity", "Настройка плотности тумана.", Category.WORLD_STYLE));
        register(new ModuleInfo("ParticleDisabler", "Отключает отображение ресурсоемких частиц.", Category.WORLD_STYLE));
        register(new ModuleInfo("LowGraphicsMode", "Оптимизация листвы и травы для повышения FPS.", Category.WORLD_STYLE));

        // Screen & Camera (76-90)
        register(new ModuleInfo("AspectRatio", "Позволяет настроить соотношение сторон экрана.", Category.SCREEN_CAMERA));
        register(new ModuleInfo("CustomFov", "Расширенный угол обзора (FOV).", Category.SCREEN_CAMERA));
        register(new ModuleInfo("HandProgress", "Плавное отображение анимаций рук.", Category.SCREEN_CAMERA));
        register(new ModuleInfo("ItemModelCustomizer", "Позволяет настроить положение предметов в руках.", Category.SCREEN_CAMERA));
        register(new ModuleInfo("ViewModel", "Смещение предметов по X/Y/Z в виде.", Category.SCREEN_CAMERA));
        register(new ModuleInfo("NoBlindness", "Удаляет эффект слепоты от зелий и других источников.", Category.SCREEN_CAMERA));
        register(new ModuleInfo("NoPumpkin", "Удаляет текстуру тыквы, когда она надета на голову.", Category.SCREEN_CAMERA));
        register(new ModuleInfo("NoFire", "Уменьшает визуальный эффект огня на игроке.", Category.SCREEN_CAMERA));
        register(new ModuleInfo("Zoom", "Плавное приближение камеры (лупа).", Category.SCREEN_CAMERA));
        register(new ModuleInfo("SmoothCamera", "Сглаживает движения камеры.", Category.SCREEN_CAMERA));
        register(new ModuleInfo("MotionBlur", "Добавляет эффект размытия в движении.", Category.SCREEN_CAMERA));
        register(new ModuleInfo("ScreenColorFilter", "Применяет цветокоррекцию к экрану.", Category.SCREEN_CAMERA));
        register(new ModuleInfo("CinematicCamera", "Режим синематика для записи или просмотра.", Category.SCREEN_CAMERA));
        register(new ModuleInfo("DynamicFovDisabler", "Отключает дерганье FOV при спринте.", Category.SCREEN_CAMERA));
        register(new ModuleInfo("HurtCamMultiplier", "Ползунок для регулировки интенсивности тряски камеры от урона.", Category.SCREEN_CAMERA));

        // Chat & Interface Tweaks (91-105)
        register(new ModuleInfo("ChatCustomizer", "Позволяет настроить прозрачность и вид чата.", Category.HUD_UI)); // Перенесено в HUD/UI
        register(new ModuleInfo("ScoreboardHide", "Скрывает табло (scoreboard).", Category.HUD_UI)); // Перенесено в HUD/UI
        register(new ModuleInfo("DeathCoordinates", "Выводит координаты смерти в чат.", Category.HUD_UI)); // Перенесено в HUD/UI
        register(new ModuleInfo("DamageNumberHUD", "Индикатор нанесенного урона на экране (альтернатива Combat Visuals).", Category.INDICATORS_GRAPH)); // Перенесено в Indicators
        register(new ModuleInfo("CrosshairLines", "Рисует линии от прицела для лучшей видимости.", Category.HUD_UI)); // Перенесено в HUD/UI
        register(new ModuleInfo("BetterTab", "Улучшенное меню Tab (список игроков).", Category.HUD_UI)); // Перенесено в HUD/UI
        register(new ModuleInfo("PingOnTab", "Отображает пинг цифрами в меню Tab.", Category.HUD_UI)); // Перенесено в HUD/UI
        register(new ModuleInfo("ItemTooltips", "Расширенные подсказки для предметов.", Category.HUD_UI)); // Перенесено в HUD/UI
        register(new ModuleInfo("CompactChat", "Группировка одинаковых сообщений в чате.", Category.HUD_UI)); // Перенесено в HUD/UI
        register(new ModuleInfo("ChatAnimation", "Плавное появление текста в чате.", Category.HUD_UI)); // Перенесено в HUD/UI
        register(new ModuleInfo("SmoothScrolling", "Плавный скролл в меню и чате.", Category.HUD_UI)); // Перенесено в HUD/UI
        register(new ModuleInfo("CustomLoadingScreen", "Кастомный экран загрузки.", Category.SCREEN_CAMERA)); // Перенесено в Screen/Camera
        register(new ModuleInfo("NotificationSystem", "Легальные всплывающие уведомления мода.", Category.HUD_UI)); // Перенесено в HUD/UI
        register(new ModuleInfo("BlurBehindMenus", "Размытие фона за окнами меню (инвентарь, настройки).", Category.SCREEN_CAMERA)); // Перенесено в Screen/Camera
        register(new ModuleInfo("MenuParticles", "Красивые частицы в главном меню.", Category.COSMETICS)); // Перенесено в Cosmetics
    }

    private static void register(ModuleInfo info) {
        MODULES.put(info.name, info);
    }

    public static Map<String, ModuleInfo> getModules() {
        return MODULES;
    }

    public static ModuleInfo getModuleInfo(String name) {
        return MODULES.get(name);
    }

    public static List<ModuleInfo> getModulesByCategory(Category category) {
        List<ModuleInfo> modulesInCategory = new ArrayList<>();
        for (ModuleInfo info : MODULES.values()) {
            if (info.category == category) {
                modulesInCategory.add(info);
            }
        }
        return modulesInCategory;
    }
}
