#!/bin/bash

set -e

./gradlew assembleDebug
./gradlew ktlintCheck detektCheck
./gradlew testDebug
