# VexVisualsPlus

Fabric-мод для **Minecraft 1.21.11** (Java 21).

## Windows 7 — важно

**Minecraft 1.21.11** и **Fabric Loom 1.14** официально рассчитаны на **Windows 10/11**.  
На **Windows 7** локально часто ломается Gradle (`gradle-fileevents.dll`) и не запускается сама игра.

| Что нужно | Windows 7 | Windows 10/11 |
|-----------|-------------|-----------------|
| Играть в MC 1.21.11 | Нет | Да |
| Собрать мод через Gradle 8 + Loom | Практически нет | Да |
| Писать код в IntelliJ | Да | Да |

### Варианты на Windows 7

1. **Сборка в облаке (бесплатно)** — залей проект на GitHub, запусти Actions → скачай JAR:
   - Репозиторий → **Actions** → **Build VexVisualsPlus** → **Run workflow**
   - В конце скачай артефакт **VexVisualsPlus**

2. **Другой ПК / виртуалка** с Windows 10 и JDK 21 — собери там, скопируй `build/libs/*.jar`.

3. **Обновление ОС** до Windows 10 — единственный нормальный способ и играть, и собирать на одном ПК.

Код и Gradle-настройки в проекте оставлены под **Win10+**; на Win7 не пытайся ставить Gradle 9.x.

---

## Сборка (Windows 10/11, PowerShell)

```powershell
cd C:\Users\Артем\IdeaProjects\VexVisualsPlus
.\BUILD.cmd build
```

Путь к Java: **`java.home`** → `D:\Programs\Java`  
Кэш Gradle: **`D:\gradle-home`**

В PowerShell всегда: **`.\gradlew.bat`**, не `gradlew.bat`.

## IntelliJ

- **Gradle user home:** `D:\gradle-home`
- **Gradle JVM:** JDK 21

## В игре

- **Right Shift** — ClickGUI (название, ник, время)
- **PearlCooldown** — 3D-траектория
- **JumpCircle** — кольца при прыжке
