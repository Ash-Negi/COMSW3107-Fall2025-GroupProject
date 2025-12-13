#!/bin/bash

# Classpath (Make sure this path matches your actual jar location)
CP="target/classes:/Users/level_up/.m2/repository/com/googlecode/json-simple/json-simple/1.1.1/json-simple-1.1.1.jar"

# 1. Compile (Updated for Maven structure src/main/java)
echo "Compiling..."
javac -cp "$CP" -d target/classes \
    src/main/java/Main.java \
    src/main/java/data/*.java \
    src/main/java/data/loaders/*.java \
    src/main/java/data/models/*.java \
    src/main/java/processor/*.java \
    src/main/java/processor/cache/*.java \
    src/main/java/ui/*.java

# 2. Run
echo "Running..."
# We use 'Main' because your Main.java does not have a 'package' line
java -cp "$CP" Main json src/main/resources/parking.json src/main/resources/properties.csv src/main/resources/population.txt