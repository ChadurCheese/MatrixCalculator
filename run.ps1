# Matrix Calculator Launcher for PowerShell
Write-Host "Matrix Calculator" -ForegroundColor Green
Write-Host "===================="

if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
    Write-Host "Maven was not found on your PATH." -ForegroundColor Red
    Write-Host "Install it from https://maven.apache.org/download.cgi and try again."
    pause
    exit 1
}

Write-Host "Launching Matrix Calculator..." -ForegroundColor Green
mvn -q javafx:run
