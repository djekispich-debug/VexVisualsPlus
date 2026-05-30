@echo off
cd /d "%~dp0"
set "GRADLE_USER_HOME=D:\gradle-home"
if exist "D:\gradle-home\native" rmdir /s /q "D:\gradle-home\native"
call gradlew.bat --no-daemon %*
exit /b %ERRORLEVEL%
