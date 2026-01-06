@echo off
title Matrix Calculator - Modern UI
echo ============================================
echo  Matrix Calculator with FlatLaf
echo ============================================
echo.

echo Step 1: Cleaning old compiled files...
if exist bin rmdir /s /q bin 2>nul
mkdir bin 2>nul

echo Step 2: Compiling with FlatLaf...
javac -cp "src;lib/flatlaf-3.2.1.jar" -d bin ^
    src/com/matrixcalculator/Main.java ^
    src/com/matrixcalculator/core/*.java ^
    src/com/matrixcalculator/ui/*.java

if errorlevel 1 (
    echo.
    echo ERROR: Compilation failed!
    echo Common issues:
    echo 1. Check you added the import in Main.java
    echo 2. Check the JAR file exists: lib/flatlaf-3.2.1.jar
    pause
    exit /b 1
)

echo.
echo Step 3: Running with modern UI...
echo.
java -cp "bin;lib/flatlaf-3.2.1.jar" com.matrixcalculator.Main

echo.
pause