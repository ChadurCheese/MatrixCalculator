# Matrix Calculator

A comprehensive Java desktop application for performing matrix operations with a modern interface.

## ✨ Features
- **Matrix Operations**: Addition, subtraction, multiplication, transpose, inverse, determinant, dot product, power, trace
- **Modern UI**: Clean flat design with single-cell selection and smart number formatting
- **User-Friendly**: Interactive tables, resize functionality, random fill, and clear options
- **Error Handling**: Clear error messages with troubleshooting tips

## 🚀 Quick Start
1. **Clone the repository**
   ```bash
   git clone https://github.com/ChadurCheese/MatrixCalculator.git
   cd MatrixCalculator
   ```

2. **Run the application as a desktop app**
   - **Windows**: Double-click `run.bat`, or run `.\run.ps1` in PowerShell
   - **Linux/Mac**: `chmod +x run.sh && ./run.sh`
   - Or directly via Maven:
     ```bash
     mvn javafx:run
     ```

3. **Or preview it in a browser** (via [JPro](https://www.jpro.one/), served at http://localhost:8080)

   JPro's server needs JDK 23+ to run (its bundled JavaFX build requires it), separate from the JDK 21 used for the desktop app. Point `JAVA_HOME` at a JDK 23+ install just for this command:
   ```cmd
   set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.x.x.x-hotspot
   mvn jpro:run
   ```

## 📖 How to Use
1. **Enter matrices** in the A and B tables (use Resize, Random Fill, or Clear buttons)
2. **Select an operation** from the dropdown menu
3. **Click Calculate** to see results in the right panel

## 🛠️ Requirements
- Java 21 or higher for the desktop app (`mvn javafx:run`)
- Java 23 or higher for the browser preview (`mvn jpro:run`) — see note above
- Maven (dependencies, including JavaFX, are resolved automatically)

## 📁 Project Structure
```
MatrixCalculator/
├── src/main/java/       # Source code (Main, core/, ui/)
├── src/main/resources/  # Icon and other assets
├── pom.xml              # Maven build config (JavaFX + JPro)
├── Dockerfile           # Container build for browser deployment
├── run.bat              # Windows launcher (desktop app)
├── run.ps1              # PowerShell launcher (desktop app)
├── run.sh               # Linux/Mac launcher (desktop app)
└── README.md            # This file
```

## 🌐 Deploying the browser demo
This app is built with JavaFX and served over the web via JPro — no separate web frontend needed.
- `mvn jpro:release` produces a deployable zip (`target/*-jpro.zip`) with a `bin/start.sh` launcher.
- `docker build -t matrix-calculator .` builds a container that runs the same release zip and exposes port 8080.

## 📞 Support
- **Contact**: GitHub [@ChadurCheese](https://github.com/ChadurCheese)

---
