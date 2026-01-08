@echo off
echo Compiling Matrix Calculator with Modern UI...
echo.

if exist bin rmdir /s /q bin >nul
mkdir bin 2>nul

javac -cp "src;lib/flatlaf-3.2.1.jar" -encoding UTF8 -d bin ^
    src/Main.java ^
    src/core/*.java ^
    src/ui/*.java

echo.
echo Running Matrix Calculator...
echo.
java -cp "bin;lib/flatlaf-3.2.1.jar" Main

pause