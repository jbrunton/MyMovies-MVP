#!/bin/bash

if [ "$1" != "" ]; then
  TEST_MATRIX=$1
else
  echo "Error: unspecified matrix name."
  exit
fi

./gradlew assembleDebug assembleDebugAndroidTest

gcloud firebase test android run firebase-test-matrices.yml:$TEST_MATRIX \
    --type instrumentation \
      --app ./app/build/outputs/apk/debug/app-debug.apk \
      --test ./app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
