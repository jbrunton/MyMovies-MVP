#!/bin/bash

set -e

./gradlew ktlintCheck detektCheck
./gradlew testDebug
