#!/bin/bash

# Create build directory
mkdir -p build

# Compile all Java files
javac -d build src/usersetup/model/*.java src/usersetup/controller/*.java src/usersetup/view/*.java src/usersetup/*.java

# Copy resources
cp -r src/usersetup/imagepicker build/usersetup/

# Run the application
java -cp build usersetup.UserSetup 