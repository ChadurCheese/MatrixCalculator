# Simple Matrix Calculator Launcher for PowerShell
Write-Host "Matrix Calculator - Starting..." -ForegroundColor Green

# Clean
if (Test-Path "bin") {
    Remove-Item -Path "bin" -Recurse -Force
}
New-Item -ItemType Directory -Path "bin" -Force | Out-Null

# Compile
Write-Host "Compiling..." -ForegroundColor Yellow
javac -cp "lib/flatlaf-3.2.1.jar" -d bin src/*.java src/core/*.java src/ui/*.java

if ($LASTEXITCODE -eq 0) {
    Write-Host "Launching..." -ForegroundColor Green
    java -cp "bin;lib/flatlaf-3.2.1.jar" Main
} else {
    Write-Host "Compilation failed!" -ForegroundColor Red
    pause
}