package VexVisuals.module;

import VexVisuals.module.JumpCircle;
import VexVisuals.module.ProjectileTrajectory;

public enum ModuleType {
    ARRAY_LIST(Category.HUD, "ArrayList", "Список активных модулей на экране"),
    WATERMARK(Category.HUD, "Watermark", "Водяной знак клиента на HUD"),
    ACTIVE_MODULES(Category.HUD, "ActiveModules", "Показывает включённые модули"),
    KEY_STROKES(Category.HUD, "KeyStrokes", "Визуализация нажатий клавиш WASD и мыши"),
    FPS_DISPLAY(Category.HUD, "FPSDisplay", "Отображение текущего FPS"),
    BPS_DISPLAY(Category.HUD, "BPSDisplay", "Скорость передвижения в блоках в секунду"),
    PING_DISPLAY(Category.HUD, "PingDisplay", "Пинг до сервера на HUD"),
    ARMOR_STATUS(Category.HUD, "ArmorStatus", "Состояние брони и прочность"),
    INVENTORY_VIEWER(Category.HUD, "InventoryViewer", "Мини-превью инвентаря"),
    POTION_STATUS(Category.HUD, "PotionStatus", "Активные зелья и таймеры"),
    COORDINATE_DISPLAY(Category.HUD, "CoordinateDisplay", "Координаты X/Y/Z"),
    SERVER_BRAND(Category.HUD, "ServerBrand", "Бренд сервера (Bukkit/Paper и т.д.)"),
    DIRECTION_HUD(Category.HUD, "DirectionHUD", "Компас направления взгляда"),
    SESSION_INFO(Category.HUD, "SessionInfo", "Время сессии в игре"),
    REAL_TIME_CLOCK(Category.HUD, "RealTimeClock", "Реальное время на HUD"),
    MEMORY_USAGE(Category.HUD, "MemoryUsage", "Использование RAM JVM"),
    TPS_DISPLAY(Category.HUD, "TPSDisplay", "Оценка TPS сервера (если доступно)"),
    DURABILITY_WARNING(Category.HUD, "DurabilityWarning", "Предупреждение о низкой прочности"),
    TARGET_HUD(Category.HUD, "TargetHUD", "Информация о текущей цели"),
    ITEM_COUNT(Category.HUD, "ItemCount", "Счётчик предметов в руке/хотбаре"),

    HIT_PARTICLES(Category.COMBAT_VISUALS, "HitParticles", "Частицы при попадании"),
    ATTACK_PROGRESS(Category.COMBAT_VISUALS, "AttackProgress", "Индикатор перезарядки удара"),
    SHIELD_DAMAGE_VISUAL(Category.COMBAT_VISUALS, "ShieldDamageVisual", "Визуал урона по щиту"),
    WEAPON_SWING_CUSTOMIZER(Category.COMBAT_VISUALS, "WeaponSwingCustomizer", "Настройка анимации взмаха"),
    NO_HURT_CAM(Category.COMBAT_VISUALS, "NoHurtCam", "Ослабление тряски камеры от урона"),
    TOTEM_POP_COUNTER(Category.COMBAT_VISUALS, "TotemPopCounter", "Счётчик срабатываний тотема"),
    VELOCITY_GRAPH(Category.COMBAT_VISUALS, "VelocityGraph", "График отбрасывания (KB)"),
    SWING_SPEED(Category.COMBAT_VISUALS, "SwingSpeed", "Визуальная скорость замаха"),
    CROSSHAIR_CUSTOMIZER(Category.COMBAT_VISUALS, "CrosshairCustomizer", "Кастомный прицел"),
    SHARPNESS_PARTICLES(Category.COMBAT_VISUALS, "SharpnessParticles", "Частицы зачарования остроты"),
    ENCHANT_GLOW_CUSTOMIZER(Category.COMBAT_VISUALS, "EnchantGlowCustomizer", "Цвет свечения зачарований"),
    CUSTOM_HIT_COLOR(Category.COMBAT_VISUALS, "CustomHitColor", "Цвет вспышки при ударе"),
    BLOCK_REACH_INDICATOR(Category.COMBAT_VISUALS, "BlockReachIndicator", "Индикатор дистанции до блока"),
    SHIELD_BREAK_NOTIFY(Category.COMBAT_VISUALS, "ShieldBreakNotify", "Уведомление о сломанном щите"),
    LOW_HP_GLOW(Category.COMBAT_VISUALS, "LowHPGlow", "Подсветка при низком HP"),
    COMBO_COUNTER(Category.COMBAT_VISUALS, "ComboCounter", "Счётчик комбо-ударов"),
    GAPPLE_COOLDOWN(Category.COMBAT_VISUALS, "GappleCooldown", "Кулдаун золотого яблока"),
    PEARL_COOLDOWN(Category.COMBAT_VISUALS, "PearlCooldown", "Кулдаун эндер-жемчуга"),
    CRITICALS_PARTICLES(Category.COMBAT_VISUALS, "CriticalsParticles", "Частицы критического удара"),
    FIREWORK_TRAILS(Category.COMBAT_VISUALS, "FireworkTrails", "Следы от фейерверков"),

    TARGET_INFO(Category.INDICATORS, "TargetInfo", "Детальная панель цели"),
    JUMP_CIRCLE(Category.INDICATORS, "JumpCircle", "3D-круг при прыжке с затуханием"),
    LAND_CIRCLE(Category.INDICATORS, "LandCircle", "Круг при приземлении"),
    VELOCITY_INDICATOR(Category.INDICATORS, "VelocityIndicator", "Вектор скорости игрока"),
    CPS_VIEWER(Category.INDICATORS, "CPSViewer", "Клики в секунду (CPS)"),
    ENTITY_SPEED_GRAPH(Category.INDICATORS, "EntitySpeedGraph", "График скорости сущностей"),
    COOLDOWN_OVERLAY(Category.INDICATORS, "CoolDownOverlay", "Оверлей кулдаунов предметов"),
    INVENTORY_FULL_WARNING(Category.INDICATORS, "InventoryFullWarning", "Предупреждение о полном инвентаре"),
    TOTEM_WARNING(Category.INDICATORS, "TotemWarning", "Предупреждение об отсутствии тотема"),
    PING_INDICATOR(Category.INDICATORS, "PingIndicator", "Цветовой индикатор пинга"),
    POTIONS_GRID(Category.INDICATORS, "PotionsGrid", "Сетка активных зелий"),
    OBSIDIAN_WARNING(Category.INDICATORS, "ObsidianWarning", "Подсказка по обсидиану в PvP"),
    STRENGTH_INDICATOR(Category.INDICATORS, "StrengthIndicator", "Индикатор эффекта силы"),
    ANCHOR_INDICATOR(Category.INDICATORS, "AnchorIndicator", "Индикатор якоря возрождения"),
    CRYSTAL_INDICATOR(Category.INDICATORS, "CrystalIndicator", "Индикатор кристаллов (визуал)"),
    DAMAGE_NUMBER_HUD(Category.INDICATORS, "DamageNumberHUD", "Числа урона на экране"),
    CROSSHAIR_LINES(Category.INDICATORS, "CrosshairLines", "Линии прицела к цели"),
    BETTER_TAB(Category.INDICATORS, "BetterTab", "Улучшенный таб-лист"),
    PING_ON_TAB(Category.INDICATORS, "PingOnTab", "Пинг игроков в табе"),
    ITEM_TOOLTIPS(Category.INDICATORS, "ItemTooltips", "Расширенные подсказки предметов"),

    CHINA_HAT(Category.COSMETICS, "ChinaHat", "Косметическая «китайская» шляпа"),
    CAPE_CUSTOMIZER(Category.COSMETICS, "CapeCustomizer", "Настройка плаща"),
    CUSTOM_WINGS(Category.COSMETICS, "CustomWings", "Крылья (косметика)"),
    PARTICLES_MULTIPLIER(Category.COSMETICS, "ParticlesMultiplier", "Множитель частиц (визуал)"),
    RAINBOW_ENCHANT(Category.COSMETICS, "RainbowEnchant", "Радужное свечение зачарований"),
    TRAIL_EFFECTS(Category.COSMETICS, "TrailEffects", "След за игроком"),
    CUSTOM_AURA_EFFECTS(Category.COSMETICS, "CustomAuraEffects", "Кастомная аура"),
    WEAPON_SIZE(Category.COSMETICS, "WeaponSize", "Размер модели оружия в руке"),
    SHIELD_POSITION(Category.COSMETICS, "ShieldPosition", "Позиция щита"),
    CUSTOM_EMOTES_VISUAL(Category.COSMETICS, "CustomEmotesVisual", "Визуальные эмоции"),
    ITEM_PHYSICS(Category.COSMETICS, "ItemPhysics", "Физика дропнутых предметов"),
    CUSTOM_SHIELD_PATTERN(Category.COSMETICS, "CustomShieldPattern", "Узор на щите"),
    ARMOR_HIDE(Category.COSMETICS, "ArmorHide", "Скрытие брони (только визуал)"),
    NAME_TAG_FORMAT(Category.COSMETICS, "NameTagFormat", "Формат никнейма над головой"),
    BODY_SPIN_VISUAL(Category.COSMETICS, "BodySpinVisual", "Вращение модели (косметика)"),
    DEVIL_HORNS(Category.COSMETICS, "DevilHorns", "Рожки (косметика)"),
    ANGEL_HALO(Category.COSMETICS, "AngelHalo", "Нимб (косметика)"),
    GLINT_COLOR(Category.COSMETICS, "GlintColor", "Цвет блика зачарования"),
    CUSTOM_SHIELD_SIZE(Category.COSMETICS, "CustomShieldSize", "Размер щита"),
    HAND_DISABLER(Category.COSMETICS, "HandDisabler", "Скрытие рук (косметика)"),

    FULL_BRIGHT(Category.WORLD_STYLE, "FullBright", "Постоянная яркость (гамма)"),
    TIME_CHANGER(Category.WORLD_STYLE, "TimeChanger", "Локальное время суток"),
    WEATHER_CHANGER(Category.WORLD_STYLE, "WeatherChanger", "Локальная погода"),
    AMBIENCE_COLOR(Category.WORLD_STYLE, "AmbienceColor", "Цветовой оттенок мира"),
    SKYBOX_CUSTOMIZER(Category.WORLD_STYLE, "SkyboxCustomizer", "Настройка неба"),
    NIGHT_VISION(Category.WORLD_STYLE, "NightVision", "Эффект ночного зрения (визуал)"),
    NO_WEATHER(Category.WORLD_STYLE, "NoWeather", "Отключение дождя/снега"),
    BLOCK_OVERLAY(Category.WORLD_STYLE, "BlockOverlay", "Подсветка выбранного блока"),
    CHUNK_BORDER(Category.WORLD_STYLE, "ChunkBorder", "Границы чанков"),
    CUSTOM_STARS(Category.WORLD_STYLE, "CustomStars", "Кастомные звёзды"),
    SUN_SIZE(Category.WORLD_STYLE, "SunSize", "Размер солнца"),
    MOON_CUSTOMIZER(Category.WORLD_STYLE, "MoonCustomizer", "Настройка луны"),
    FOG_DENSITY(Category.WORLD_STYLE, "FogDensity", "Плотность тумана"),
    PARTICLE_DISABLER(Category.WORLD_STYLE, "ParticleDisabler", "Отключение частиц мира"),
    LOW_GRAPHICS_MODE(Category.WORLD_STYLE, "LowGraphicsMode", "Упрощённая графика"),
    NETHER_ROOF_HIDE(Category.WORLD_STYLE, "NetherRoofHide", "Скрытие крыши ада (визуал)"),
    CUSTOM_SKY_GRADIENT(Category.WORLD_STYLE, "CustomSkyGradient", "Градиент неба"),
    CUSTOM_CLOUD_COLOR(Category.WORLD_STYLE, "CustomCloudColor", "Цвет облаков"),
    BETTER_SCREENSHOTS(Category.WORLD_STYLE, "BetterScreenshots", "Улучшенные скриншоты"),
    BARRIER_VIEW(Category.WORLD_STYLE, "BarrierView", "Видимость барьерных блоков"),

    ASPECT_RATIO(Category.SCREEN_CAMERA_CHAT, "AspectRatio", "Соотношение сторон экрана"),
    CUSTOM_FOV(Category.SCREEN_CAMERA_CHAT, "CustomFov", "Кастомное поле зрения"),
    HAND_PROGRESS(Category.SCREEN_CAMERA_CHAT, "HandProgress", "Прогресс анимации руки"),
    ITEM_MODEL_CUSTOMIZER(Category.SCREEN_CAMERA_CHAT, "ItemModelCustomizer", "Модели предметов в руке"),
    VIEW_MODEL(Category.SCREEN_CAMERA_CHAT, "ViewModel", "Позиция/масштаб viewmodel"),
    NO_BLINDNESS(Category.SCREEN_CAMERA_CHAT, "NoBlindness", "Ослабление слепоты"),
    NO_PUMPKIN(Category.SCREEN_CAMERA_CHAT, "NoPumpkin", "Скрытие оверлея тыквы"),
    NO_FIRE(Category.SCREEN_CAMERA_CHAT, "NoFire", "Ослабление оверлея огня"),
    ZOOM(Category.SCREEN_CAMERA_CHAT, "Zoom", "Плавный зум камеры"),
    SMOOTH_CAMERA(Category.SCREEN_CAMERA_CHAT, "SmoothCamera", "Сглаживание камеры"),
    MOTION_BLUR(Category.SCREEN_CAMERA_CHAT, "MotionBlur", "Размытие движения (пост)"),
    SCREEN_COLOR_FILTER(Category.SCREEN_CAMERA_CHAT, "ScreenColorFilter", "Цветовой фильтр экрана"),
    CINEMATIC_CAMERA(Category.SCREEN_CAMERA_CHAT, "CinematicCamera", "Кинематографическая камера"),
    DYNAMIC_FOV_DISABLER(Category.SCREEN_CAMERA_CHAT, "DynamicFovDisabler", "Отключение динамического FOV"),
    HURT_CAM_MULTIPLIER(Category.SCREEN_CAMERA_CHAT, "HurtCamMultiplier", "Множитель тряски от урона"),
    CHAT_CUSTOMIZER(Category.SCREEN_CAMERA_CHAT, "ChatCustomizer", "Стиль чата"),
    SCOREBOARD_HIDE(Category.SCREEN_CAMERA_CHAT, "ScoreboardHide", "Скрытие скорборда"),
    DEATH_COORDINATES(Category.SCREEN_CAMERA_CHAT, "DeathCoordinates", "Координаты смерти в чат"),
    COMPACT_CHAT(Category.SCREEN_CAMERA_CHAT, "CompactChat", "Компактный чат"),
    CHAT_ANIMATION(Category.SCREEN_CAMERA_CHAT, "ChatAnimation", "Анимация сообщений чата");

    private final Category category;
    private final String name;
    private final String descriptionRu;

    ModuleType(Category category, String name, String descriptionRu) {
        this.category = category;
        this.name = name;
        this.descriptionRu = descriptionRu;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDescriptionRu() {
        return descriptionRu;
    }

    public void applyDefaultSettings(Module module) {
        module.addSetting(new BooleanSetting("Включить HUD", "Показывать элемент на экране", true, module));
        module.addSetting(new NumberSetting("Масштаб", "Размер элемента", 1.0, 0.5, 2.0, 0.05, module));
        module.addSetting(new ModeSetting("Режим", "Стиль отображения", "Обычный", module, "Обычный", "Компакт", "Минимал"));
        module.addSetting(new ColorSetting("Цвет", "Основной цвет", 0xFF6C5CE7, module));

        if (this == JUMP_CIRCLE) {
            module.addSetting(new NumberSetting("Радиус", "Макс. радиус круга", 1.2, 0.5, 3.0, 0.1, module));
            module.addSetting(new NumberSetting("Длительность", "Время жизни (мс)", 600, 200, 1500, 50, module));
        }
        if (this == PEARL_COOLDOWN) {
            module.addSetting(new BooleanSetting("Траектория", "3D-линия броска", true, module));
        }
    }

    public void onEnable(Module module) {
        if (this == JUMP_CIRCLE) {
            JumpCircle.setActive(true);
        }
        if (this == PEARL_COOLDOWN) {
            ProjectileTrajectory.setActive(true);
        }
    }

    public void onDisable(Module module) {
        if (this == JUMP_CIRCLE) {
            JumpCircle.setActive(false);
        }
        if (this == PEARL_COOLDOWN) {
            ProjectileTrajectory.setActive(false);
        }
    }
}
