#!/usr/bin/env bash

set -e

gcloud config set project "$GCLOUD_PROJECT"
gcloud auth activate-service-account --key-file ${BUDDYBUILD_SECURE_FILES}/gcloudkey.json "$GCLOUD_USER"

./gradlew --no-daemon ":app:assembleDebugAndroidTest" ":app:assembleDebug"

echo "Starting smoke test..."
gcloud firebase test android run firebase-test-matrices.yml:smoketest \
    --type instrumentation \
      --app $BUDDYBUILD_WORKSPACE/app/build/outputs/apk/debug/app-debug.apk \
      --test $BUDDYBUILD_WORKSPACE/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk

if [ "$BUDDYBUILD_BRANCH" = "master" ]
then
    echo "Starting road test..."
    gcloud firebase test android run firebase-test-matrices.yml:roadtest \
        --type instrumentation \
          --app $BUDDYBUILD_WORKSPACE/app/build/outputs/apk/debug/app-debug.apk \
          --test $BUDDYBUILD_WORKSPACE/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
else
    echo "Not on master, skipping road test."
fi
