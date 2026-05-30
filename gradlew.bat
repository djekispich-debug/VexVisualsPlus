@rem VexVisualsPlus — Gradle wrapper (кэш: D:\gradle-home)
@if not defined GRADLE_USER_HOME set "GRADLE_USER_HOME=D:\gradle-home"
if not exist "%GRADLE_USER_HOME%" mkdir "%GRADLE_USER_HOME%" 2>nul
(
echo org.gradle.vfs.watch=false
echo org.gradle.daemon=false
echo org.gradle.jvmargs=-Xmx2G -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8
echo org.gradle.java.home=D:/Programs/Java
) > "%GRADLE_USER_HOME%\gradle.properties"
if exist "%GRADLE_USER_HOME%\native" rmdir /s /q "%GRADLE_USER_HOME%\native" 2>nul

@if "%DEBUG%"=="" @echo off
if "%OS%"=="Windows_NT" setlocal EnableDelayedExpansion
set "DIRNAME=%~dp0"
set "APP_HOME=%DIRNAME%"
for %%i in ("%APP_HOME%") do set "APP_HOME=%%~fi"
set "APP_BASE_NAME=%~n0"
set "WRAPPER_JAR=%APP_HOME%gradle\wrapper\gradle-wrapper.jar"

rem --- Пользовательский путь к JDK (файл java.home в корне проекта) ---
if exist "%APP_HOME%java.home" (
    set /p JAVA_HOME=<"%APP_HOME%java.home"
    set "JAVA_HOME=!JAVA_HOME: =!"
)

rem --- Автопоиск JDK ---
if not defined JAVA_HOME if exist "D:\Programs\Java\bin\java.exe" set "JAVA_HOME=D:\Programs\Java"
if not defined JAVA_HOME if exist "D:\Programs\Java\jdk-21\bin\java.exe" set "JAVA_HOME=D:\Programs\Java\jdk-21"
if not defined JAVA_HOME if exist "C:\Program Files\Java\jdk-21\bin\java.exe" set "JAVA_HOME=C:\Program Files\Java\jdk-21"
if not defined JAVA_HOME if exist "C:\Program Files\Eclipse Adoptium\" (
    for /d %%i in ("C:\Program Files\Eclipse Adoptium\jdk-*") do (
        if exist "%%i\bin\java.exe" set "JAVA_HOME=%%~fi"
    )
)

rem --- Java из PATH ---
if not defined JAVA_HOME (
    for /f "delims=" %%i in ('where java 2^>nul') do (
        for %%J in ("%%~dpi..") do set "JAVA_HOME=%%~fJ"
        goto :java_ok
    )
)

:java_ok
if defined JAVA_HOME (
    set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
) else (
    set "JAVA_EXE=java.exe"
)

if not exist "%JAVA_EXE%" (
    echo.
    echo ERROR: Java not found.
    echo   Create file java.home with your JDK path, for example:
    echo   D:\Programs\Java
    echo   Download JDK 21: https://adoptium.net/
    echo.
    exit /b 1
)

echo Using Java: %JAVA_EXE%
"%JAVA_EXE%" -version 2>&1
if errorlevel 1 exit /b 1

if not exist "%WRAPPER_JAR%" (
    echo [VexVisualsPlus] Downloading gradle-wrapper.jar...
    if not exist "%APP_HOME%gradle\wrapper" mkdir "%APP_HOME%gradle\wrapper"
    certutil -urlcache -split -f "https://raw.githubusercontent.com/gradle/gradle/v8.14.3/gradle/wrapper/gradle-wrapper.jar" "%WRAPPER_JAR%" >nul
    if errorlevel 1 powershell -NoProfile -ExecutionPolicy Bypass -File "%APP_HOME%setup-wrapper.ps1"
)

if not exist "%WRAPPER_JAR%" (
    echo ERROR: gradle-wrapper.jar missing. Check internet or run setup-wrapper.ps1
    exit /b 1
)

set "DEFAULT_JVM_OPTS=-Xmx64m -Xms64m"
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=%APP_BASE_NAME%" -jar "%WRAPPER_JAR%" %*
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% neq 0 exit /b %EXIT_CODE%
if "%OS%"=="Windows_NT" endlocal
exit /b 0
