#!/bin/bash

set -e

./gradlew entities:test networking:test di:test testDebug
