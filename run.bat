@echo off
echo Compiling Matrix Calculator with Modern UI...
echo.

if exist bin rmdir /s /q bin >nul
mkdir bin 2>nul

javac -cp "src;lib/flatlaf-3.2.1.jar" -encoding UTF8 -d bin ^
    src/com/matrixcalculator/Main.java ^
    src/com/matrixcalculator/core/*.java ^
    src/com/matrixcalculator/ui/*.java

echo.
echo Running Matrix Calculator...
echo.
java -cp "bin;lib/flatlaf-3.2.1.jar" com.matrixcalculator.Main

pause