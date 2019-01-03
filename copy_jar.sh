#!/bin/bash

set -e

./gradlew clean && ./gradlew jar

cp ./build/libs/chess-tama-engine-java.jar ../chess-tama/libs/ 

echo "Done"
