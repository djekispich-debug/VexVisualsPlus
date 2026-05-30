# Как залить исправление CI на GitHub

Ошибка `plugin.api-version 8.14.3` = на GitHub **старый** `gradle-wrapper.properties` (8.14.3).
Loom 1.14.10 нужен **Gradle 9.2+**.

## Команды (PowerShell)

```powershell
cd C:\Users\Артем\IdeaProjects\VexVisualsPlus

git add .github/workflows/build.yml
git add gradle/wrapper/gradle-wrapper.properties
git add build.gradle gradle.properties settings.gradle

git status
git commit -m "Fix CI: force Gradle 9.4.1 for Loom 1.14.10"
git push
```

## После push

GitHub → **Actions** → **Build VexVisualsPlus** → **Run workflow**

В логе должно быть:
```
Gradle 9.4.1
```

Если снова `8.14.3` — значит push не дошёл или workflow старый.
