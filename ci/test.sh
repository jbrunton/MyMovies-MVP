#!/bin/bash

set -e

./gradlew entities:test networking:test app-usecases:test testDebug
