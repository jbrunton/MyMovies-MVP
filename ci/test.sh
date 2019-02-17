#!/bin/bash

set -e

./gradlew entities:test app-usecases:test networking:test testDebug
