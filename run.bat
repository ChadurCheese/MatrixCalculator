@echo off
echo Matrix Calculator
echo ====================
echo.

where mvn >nul 2>nul
if errorlevel 1 (
    echo Maven was not found on your PATH.
    echo Install it from https://maven.apache.org/download.cgi and try again.
    pause
    exit /b 1
)

echo Launching Matrix Calculator...
echo.
call mvn -q javafx:run

pause
