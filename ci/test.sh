#!/bin/bash

set -e

./gradlew entities:test usecases:test networking:test testDebug
