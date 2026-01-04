@echo off
echo Compiling Matrix Calculator...
javac -d bin src/Main.java src/core/*.java src/ui/*.java
echo.
echo Running Matrix Calculator...
java -cp bin Main
pause