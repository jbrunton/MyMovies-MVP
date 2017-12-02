#!/usr/bin/env bash

set -e

./gradlew assembleDebugAndroidTest

/var/jenkins_home/gcloud/google-cloud-sdk/bin/gcloud config set project mymovies-7e138
/var/jenkins_home/gcloud/google-cloud-sdk/bin/gcloud auth activate-service-account --key-file ${JENKINS_HOME}/gcloudkey.json mymovies-7e138@appspot.gserviceaccount.com

/var/jenkins_home/gcloud/google-cloud-sdk/bin/gcloud firebase test android run firebase-test-matrices.yml:smoketest \
    --type instrumentation \
      --app ./app/build/outputs/apk/debug/app-debug.apk \
      --test ./app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk