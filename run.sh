#!/bin/bash

echo "🧮 Matrix Calculator"
echo "===================="
echo "Running on: $(uname -s)"

# Detect if we're on Windows (Git Bash/MINGW)
if [[ "$OSTYPE" == "msys" ]] || [[ "$OSTYPE" == "cygwin" ]] || [[ "$(uname -s)" == "MINGW"* ]]; then
    echo "Detected: Windows Git Bash"
    SEP=";"  # Windows separator
else
    echo "Detected: Linux/Mac"
    SEP=":"  # Linux/Mac separator
fi

# Clean
rm -rf bin/* 2>/dev/null
mkdir -p bin

# Compile
echo "Compiling..."
javac -cp "lib/flatlaf-3.2.1.jar" -d bin src/*.java src/core/*.java src/ui/*.java

if [ $? -ne 0 ]; then
    echo "❌ Compilation failed!"
    exit 1
fi

echo "✅ Compilation successful!"
echo ""
echo "🚀 Launching Matrix Calculator..."
echo ""

# Run with correct separator
java -cp "bin${SEP}lib/flatlaf-3.2.1.jar" Main