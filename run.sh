#!/bin/bash
echo "🧮 Matrix Calculator"
echo "===================="

if ! command -v mvn &> /dev/null; then
    echo "Maven was not found on your PATH."
    echo "Install it from https://maven.apache.org/download.cgi and try again."
    exit 1
fi

echo "Launching Matrix Calculator..."
echo ""
mvn -q javafx:run
