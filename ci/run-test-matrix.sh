#!/bin/bash

set -e

if [ "$1" != "" ]; then
  TEST_MATRIX=$1
else
  echo "Error: unspecified matrix name."
  exit
fi

./gradlew assembleDebug assembleDebugAndroidTest

modules=(
    "app"
    "features-account"
    "shared-ui"
    "libs-ui"
)

for module in "${modules[@]}"
do
    test_apk="./${module}/build/outputs/apk/androidTest/debug/${module}-debug-androidTest.apk"
    gcloud firebase test android run firebase-test-matrices.yml:$TEST_MATRIX \
        --type instrumentation \
        --app ./app/build/outputs/apk/debug/app-debug.apk \
        --test "${test_apk}"
done

