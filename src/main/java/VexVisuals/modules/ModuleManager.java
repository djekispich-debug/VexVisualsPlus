package VexVisuals.modules;

import VexVisuals.modules.impl.render.*; // Импорт реализованных модулей
import VexVisuals.utils.ColorManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModuleManager {

    private final Map<String, Module> modules = new HashMap<>();
    private final List<Module> enabledModules = new ArrayList<>();

    public void initialize() {
        // Регистрация всех легальных модулей
        // HUD / UI
        registerModule(new HudArrayList("ArrayList", "Отображает список активных модулей, отсортированный по длине названия.", ModuleRegistry.Category.HUD_UI));
        // registerModule(new Watermark("Watermark", "Отображает название клиента и версию.", ModuleRegistry.Category.HUD_UI));
        // registerModule(new KeyStrokes("KeyStrokes", "Отображает нажатые клавиши.", ModuleRegistry.Category.HUD_UI));
        // registerModule(new FPSDisplay("FPSDisplay", "Отображает текущий FPS.", ModuleRegistry.Category.HUD_UI));
        // registerModule(new BPSDisplay("BPSDisplay", "Отображает текущую скорость перемещения (Blocks Per Second).", ModuleRegistry.Category.HUD_UI));
        // registerModule(new PingDisplay("PingDisplay", "Отображает пинг сервера.", ModuleRegistry.Category.HUD_UI));
        // registerModule(new ArmorStatus("ArmorStatus", "Показывает прочность брони.", ModuleRegistry.Category.HUD_UI));
        // registerModule(new InventoryViewer("InventoryViewer", "Отображает инвентарь игрока на экране.", ModuleRegistry.Category.HUD_UI));
        // registerModule(new PotionStatus("PotionStatus", "Показывает активные зелья.", ModuleRegistry.Category.HUD_UI));
        // registerModule(new CoordinateDisplay("CoordinateDisplay", "Отображает текущие координаты.", ModuleRegistry.Category.HUD_UI));
        // registerModule(new ServerBrand("ServerBrand", "Отображает бренд сервера.", ModuleRegistry.Category.HUD_UI));
        // registerModule(new DirectionHUD("DirectionHUD", "Отображает направление взгляда игрока (компас).", ModuleRegistry.Category.HUD_UI));
        // registerModule(new SessionInfo("SessionInfo", "Отображает статистику текущей игровой сессии.", ModuleRegistry.Category.HUD_UI));
        // registerModule(new RealTimeClock("RealTimeClock", "Отображает реальное время.", ModuleRegistry.Category.HUD_UI));
        // registerModule(new ChatCustomizer("ChatCustomizer", "Позволяет настроить прозрачность и вид чата.", ModuleRegistry.Category.HUD_UI));
        // registerModule(new ScoreboardHide("ScoreboardHide", "Скрывает табло (scoreboard).", ModuleRegistry.Category.HUD_UI));
        // registerModule(new DeathCoordinates("DeathCoordinates", "Выводит координаты смерти в чат.", ModuleRegistry.Category.HUD_UI));
        // registerModule(new CrosshairLines("CrosshairLines", "Рисует линии от прицела для лучшей видимости.", ModuleRegistry.Category.HUD_UI));
        // registerModule(new BetterTab("BetterTab", "Улучшенное меню Tab (список игроков).", ModuleRegistry.Category.HUD_UI));
        // registerModule(new PingOnTab("PingOnTab", "Отображает пинг цифрами в меню Tab.", ModuleRegistry.Category.HUD_UI));
        // registerModule(new ItemTooltips("ItemTooltips", "Расширенные подсказки для предметов.", ModuleRegistry.Category.HUD_UI));
        // registerModule(new CompactChat("CompactChat", "Группировка одинаковых сообщений в чате.", ModuleRegistry.Category.HUD_UI));
        // registerModule(new ChatAnimation("ChatAnimation", "Плавное появление текста в чате.", ModuleRegistry.Category.HUD_UI));
        // registerModule(new SmoothScrolling("SmoothScrolling", "Плавный скролл в меню и чате.", ModuleRegistry.Category.HUD_UI));
        // registerModule(new NotificationSystem("NotificationSystem", "Легальные всплывающие уведомления мода.", ModuleRegistry.Category.HUD_UI));

        // Combat Visuals (Legal)
        // registerModule(new HitParticles("HitParticles", "Отображает красивые и плавные частицы при попадании по цели.", ModuleRegistry.Category.COMBAT_VISUALS));
        // registerModule(new AttackProgress("AttackProgress", "Визуальный индикатор прогресса атаки.", ModuleRegistry.Category.COMBAT_VISUALS));
        // registerModule(new ShieldDamageVisual("ShieldDamageVisual", "Анимация, показывающая, когда щит получает урон.", ModuleRegistry.Category.COMBAT_VISUALS));
        // registerModule(new WeaponSwingCustomizer("WeaponSwingCustomizer", "Плавная и настраиваемая анимация взмаха оружием.", ModuleRegistry.Category.COMBAT_VISUALS));
        // registerModule(new NoHurtCam("NoHurtCam", "Снижает интенсивность тряски камеры при получении урона.", ModuleRegistry.Category.COMBAT_VISUALS));
        // registerModule(new TotemPopCounter("TotemPopCounter", "Текстовый счетчик срабатываний тотема (в чате или HUD).", ModuleRegistry.Category.COMBAT_VISUALS));
        // registerModule(new VelocityGraph("VelocityGraph", "Отображает график получаемой игроком скорости (легальная информация).", ModuleRegistry.Category.COMBAT_VISUALS));
        // registerModule(new SwingSpeed("SwingSpeed", "Позволяет настроить скорость выполнения взмахов оружием.", ModuleRegistry.Category.COMBAT_VISUALS));
        // registerModule(new CrosshairCustomizer("CrosshairCustomizer", "Кастомизирует прицел (форма, цвет, размер).", ModuleRegistry.Category.COMBAT_VISUALS));
        // registerModule(new SharpnessParticles("SharpnessParticles", "Добавляет красивые частицы к эффекту зачарования 'Острота'.", ModuleRegistry.Category.COMBAT_VISUALS));
        // registerModule(new EnchantGlowCustomizer("EnchantGlowCustomizer", "Позволяет настроить цвет свечения чар на предметах.", ModuleRegistry.Category.COMBAT_VISUALS));
        // registerModule(new CustomHitColor("CustomHitColor", "Изменяет цвет игрока при получении урона (визуальный эффект).", ModuleRegistry.Category.COMBAT_VISUALS));
        // registerModule(new BlockReachIndicator("BlockReachIndicator", "Визуально показывает дистанцию до блока, который можно сломать/поставить.", ModuleRegistry.Category.COMBAT_VISUALS));
        // registerModule(new ShieldBreakNotify("ShieldBreakNotify", "Уведомление о поломке щита.", ModuleRegistry.Category.COMBAT_VISUALS));
        // registerModule(new LowHPGlow("LowHPGlow", "Придает игроку мягкое свечение при низком HP.", ModuleRegistry.Category.COMBAT_VISUALS));

        // Indicators & Graphs
        registerModule(new TargetInfo("TargetInfo", "Отображает информацию о цели в поле зрения (ник, HP, пинг).", ModuleRegistry.Category.INDICATORS_GRAPH));
        registerModule(new JumpCircle("JumpCircle", "Отображает 3D-круг на земле при прыжке игрока.", ModuleRegistry.Category.INDICATORS_GRAPH));
        // registerModule(new LandCircle("LandCircle", "Визуальный эффект при приземлении игрока.", ModuleRegistry.Category.INDICATORS_GRAPH));
        // registerModule(new VelocityIndicator("VelocityIndicator", "Визуальный индикатор ускорения/замедления игрока.", ModuleRegistry.Category.INDICATORS_GRAPH));
        // registerModule(new CPSViewer("CPSViewer", "Отображает количество кликов в секунду (CPS).", ModuleRegistry.Category.INDICATORS_GRAPH));
        // registerModule(new EntitySpeedGraph("EntitySpeedGraph", "График скорости для сущностей (лошади, элитры).", ModuleRegistry.Category.INDICATORS_GRAPH));
        // registerModule(new CoolDownOverlay("CoolDownOverlay", "Визуализирует кулдауны предметов/действий.", ModuleRegistry.Category.INDICATORS_GRAPH));
        // registerModule(new InventoryFullWarning("InventoryFullWarning", "Предупреждение о полном инвентаре.", ModuleRegistry.Category.INDICATORS_GRAPH));
        // registerModule(new TotemWarning("TotemWarning", "Напоминание взять тотем в руку.", ModuleRegistry.Category.INDICATORS_GRAPH));
        // registerModule(new PingIndicator("PingIndicator", "График задержки сети (пинг).", ModuleRegistry.Category.INDICATORS_GRAPH));
        // registerModule(new ComboCounter("ComboCounter", "Счетчик серии успешных ударов.", ModuleRegistry.Category.INDICATORS_GRAPH));
        // registerModule(new GappleCooldown("GappleCooldown", "Таймер перезарядки золотого яблока.", ModuleRegistry.Category.INDICATORS_GRAPH));
        // registerModule(new PearlCooldown("PearlCooldown", "Таймер перезарядки эндер-жемчуга.", ModuleRegistry.Category.INDICATORS_GRAPH));
        // registerModule(new PotionsGrid("PotionsGrid", "Отображает активные зелья в виде сетки.", ModuleRegistry.Category.INDICATORS_GRAPH));
        // registerModule(new MemoryUsage("MemoryUsage", "Отображает использование оперативной памяти клиентом.", ModuleRegistry.Category.INDICATORS_GRAPH));
        // registerModule(new DamageNumberHUD("DamageNumberHUD", "Индикатор нанесенного урона на экране (альтернатива Combat Visuals).", ModuleRegistry.Category.INDICATORS_GRAPH));

        // Cosmetics
        // registerModule(new ChinaHat("ChinaHat", "Светящаяся шляпа-конус на голове игрока.", ModuleRegistry.Category.COSMETICS));
        // registerModule(new CapeCustomizer("CapeCustomizer", "Позволяет настроить кастомный плащ игрока.", ModuleRegistry.Category.COSMETICS));
        // registerModule(new CustomWings("CustomWings", "Анимированные крылья за спиной игрока.", ModuleRegistry.Category.COSMETICS));
        // registerModule(new ParticlesMultiplier("ParticlesMultiplier", "Увеличивает количество легальных частиц (для красоты).", ModuleRegistry.Category.COSMETICS));
        // registerModule(new RainbowEnchant("RainbowEnchant", "Делает эффекты зачарований радужными.", ModuleRegistry.Category.COSMETICS));
        // registerModule(new FireworkTrails("FireworkTrails", "Оставляет за игроком следы из фейерверков.", ModuleRegistry.Category.COSMETICS));
        // registerModule(new TrailEffects("TrailEffects", "Рисует линии за игроком при беге.", ModuleRegistry.Category.COSMETICS));
        // registerModule(new CustomAuraEffects("CustomAuraEffects", "Настраиваемые эффекты вокруг игрока.", ModuleRegistry.Category.COSMETICS));
        // registerModule(new WeaponSize("WeaponSize", "Изменяет размер оружия в руке игрока.", ModuleRegistry.Category.COSMETICS));
        // registerModule(new ShieldPosition("ShieldPosition", "Позволяет настроить положение щита в руке.", ModuleRegistry.Category.COSMETICS));
        // registerModule(new CustomEmotesVisual("CustomEmotesVisual", "Визуальное отображение кастомных эмоций.", ModuleRegistry.Category.COSMETICS));
        // registerModule(new ItemPhysics("ItemPhysics", "Реалистичное поведение дропнутых предметов.", ModuleRegistry.Category.COSMETICS));
        // registerModule(new CustomShieldPattern("CustomShieldPattern", "Позволяет использовать кастомные узоры для щитов.", ModuleRegistry.Category.COSMETICS));
        // registerModule(new ArmorHide("ArmorHide", "Позволяет скрывать броню (для скриншотов).", ModuleRegistry.Category.COSMETICS));
        // registerModule(new NameTagFormat("NameTagFormat", "Применяет красивый шрифт к своему нику.", ModuleRegistry.Category.COSMETICS));
        // registerModule(new MenuParticles("MenuParticles", "Красивые частицы в главном меню.", ModuleRegistry.Category.COSMETICS));

        // World Style
        // registerModule(new FullBright("FullBright", "Легальное увеличение яркости/гаммы без изменения освещения мира.", ModuleRegistry.Category.WORLD_STYLE));
        // registerModule(new TimeChanger("TimeChanger", "Визуальная смена времени суток (не влияет на сервер).", ModuleRegistry.Category.WORLD_STYLE));
        // registerModule(new WeatherChanger("WeatherChanger", "Визуальная смена погоды (не влияет на сервер).", ModuleRegistry.Category.WORLD_STYLE));
        // registerModule(new AmbienceColor("AmbienceColor", "Настраивает цвет неба и тумана.", ModuleRegistry.Category.WORLD_STYLE));
        // registerModule(new SkyboxCustomizer("SkyboxCustomizer", "Позволяет использовать кастомные текстуры неба.", ModuleRegistry.Category.WORLD_STYLE));
        // registerModule(new NightVision("NightVision", "Эффект ночного зрения без зелья.", ModuleRegistry.Category.WORLD_STYLE));
        // registerModule(new NoWeather("NoWeather", "Отключает отображение дождя/снега для повышения FPS.", ModuleRegistry.Category.WORLD_STYLE));
        // registerModule(new BlockOverlay("BlockOverlay", "Красивая подсветка выделенного блока.", ModuleRegistry.Category.WORLD_STYLE));
        // registerModule(new ChunkBorder("ChunkBorder", "Отображает границы чанков.", ModuleRegistry.Category.WORLD_STYLE));
        // registerModule(new CustomStars("CustomStars", "Настройка размера и количества звезд на небе.", ModuleRegistry.Category.WORLD_STYLE));
        // registerModule(new SunSize("SunSize", "Изменяет размер солнца.", ModuleRegistry.Category.WORLD_STYLE));
        // registerModule(new MoonCustomizer("MoonCustomizer", "Позволяет использовать кастомные текстуры луны.", ModuleRegistry.Category.WORLD_STYLE));
        // registerModule(new FogDensity("FogDensity", "Настройка плотности тумана.", ModuleRegistry.Category.WORLD_STYLE));
        // registerModule(new ParticleDisabler("ParticleDisabler", "Отключает отображение ресурсоемких частиц.", ModuleRegistry.Category.WORLD_STYLE));
        // registerModule(new LowGraphicsMode("LowGraphicsMode", "Оптимизация листвы и травы для повышения FPS.", ModuleRegistry.Category.WORLD_STYLE));

        // Screen & Camera
        // registerModule(new AspectRatio("AspectRatio", "Позволяет настроить соотношение сторон экрана.", ModuleRegistry.Category.SCREEN_CAMERA));
        // registerModule(new CustomFov("CustomFov", "Расширенный угол обзора (FOV).", ModuleRegistry.Category.SCREEN_CAMERA));
        // registerModule(new HandProgress("HandProgress", "Плавное отображение анимаций рук.", ModuleRegistry.Category.SCREEN_CAMERA));
        // registerModule(new ItemModelCustomizer("ItemModelCustomizer", "Позволяет настроить положение предметов в руках.", ModuleRegistry.Category.SCREEN_CAMERA));
        // registerModule(new ViewModel("ViewModel", "Смещение предметов по X/Y/Z в виде.", ModuleRegistry.Category.SCREEN_CAMERA));
        // registerModule(new NoBlindness("NoBlindness", "Удаляет эффект слепоты от зелий и других источников.", ModuleRegistry.Category.SCREEN_CAMERA));
        // registerModule(new NoPumpkin("NoPumpkin", "Удаляет текстуру тыквы, когда она надета на голову.", ModuleRegistry.Category.SCREEN_CAMERA));
        // registerModule(new NoFire("NoFire", "Уменьшает визуальный эффект огня на игроке.", ModuleRegistry.Category.SCREEN_CAMERA));
        // registerModule(new Zoom("Zoom", "Плавное приближение камеры (лупа).", ModuleRegistry.Category.SCREEN_CAMERA));
        // registerModule(new SmoothCamera("SmoothCamera", "Сглаживает движения камеры.", ModuleRegistry.Category.SCREEN_CAMERA));
        // registerModule(new MotionBlur("MotionBlur", "Добавляет эффект размытия в движении.", ModuleRegistry.Category.SCREEN_CAMERA));
        // registerModule(new ScreenColorFilter("ScreenColorFilter", "Применяет цветокоррекцию к экрану.", ModuleRegistry.Category.SCREEN_CAMERA));
        // registerModule(new CinematicCamera("CinematicCamera", "Режим синематика для записи или просмотра.", ModuleRegistry.Category.SCREEN_CAMERA));
        // registerModule(new DynamicFovDisabler("DynamicFovDisabler", "Отключает дерганье FOV при спринте.", ModuleRegistry.Category.SCREEN_CAMERA));
        // registerModule(new HurtCamMultiplier("HurtCamMultiplier", "Ползунок для регулировки интенсивности тряски камеры от урона.", ModuleRegistry.Category.SCREEN_CAMERA));
        // registerModule(new CustomLoadingScreen("CustomLoadingScreen", "Кастомный экран загрузки.", ModuleRegistry.Category.SCREEN_CAMERA));
        // registerModule(new BlurBehindMenus("BlurBehindMenus", "Размытие фона за окнами меню (инвентарь, настройки).", ModuleRegistry.Category.SCREEN_CAMERA));


        // Важно: Загрузка настроек из файла конфигурации (если есть)
        // loadSettings();
    }

    private void registerModule(Module module) {
        modules.put(module.getName(), module);
    }

    public Module getModule(String name) {
        return modules.get(name);
    }

    public List<Module> getAllModules() {
        return new ArrayList<>(modules.values());
    }

    public List<Module> getModulesByCategory(ModuleRegistry.Category category) {
        return modules.values().stream()
                .filter(module -> module.getCategory() == category)
                .collect(Collectors.toList());
    }

    public List<Module> getEnabledModules() {
        // Сортируем по длине названия для ArrayList
        enabledModules.sort((m1, m2) -> Integer.compare(textRenderer.getWidth(m2.getName()), textRenderer.getWidth(m1.getName())));
        return enabledModules;
    }

    public void toggleModule(String moduleName) {
        Module module = getModule(moduleName);
        if (module != null) {
            module.toggle();
            if (module.isEnabled()) {
                enabledModules.add(module);
            } else {
                enabledModules.remove(module);
            }
            // Пересортируем после изменения статуса
            enabledModules.sort((m1, m2) -> Integer.compare(textRenderer.getWidth(m2.getName()), textRenderer.getWidth(m1.getName())));
        }
    }

    public void setModuleEnabled(String moduleName, boolean enabled) {
        Module module = getModule(moduleName);
        if (module != null) {
            module.setEnabled(enabled);
            if (enabled) {
                if (!enabledModules.contains(module)) {
                    enabledModules.add(module);
                }
            } else {
                enabledModules.remove(module);
            }
            // Пересортируем после изменения статуса
            enabledModules.sort((m1, m2) -> Integer.compare(textRenderer.getWidth(m2.getName()), textRenderer.getWidth(m1.getName())));
        }
    }

    public void onWorldRender(float partialTicks) {
        for (Module module : enabledModules) {
            module.onWorldRender(partialTicks);
        }
    }

    public void onHudRender() {
        for (Module module : enabledModules) {
            module.onHudRender();
        }
    }

    public void onEvent(Object event) {
        for (Module module : enabledModules) {
            module.onEvent(event);
        }
    }

    // TODO: Реализовать сохранение и загрузку настроек
    public void saveSettings() {
        // Логика сохранения настроек модулей в файл конфигурации
    }

    public void loadSettings() {
        // Логика загрузки настроек модулей из файла конфигурации
    }
}
