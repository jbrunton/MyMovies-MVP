#!/bin/bash

set -e

./gradlew entities:test networking:test usecases:test testDebug
