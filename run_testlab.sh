#!/usr/bin/env bash

set -e

gcloud config set project mymovies-7e138
gcloud auth activate-service-account --key-file ${JENKINS_HOME}/gcloudkey.json mymovies-7e138@appspot.gserviceaccount.com

gcloud firebase test android run firebase-test-matrices.yml:smoketest \
    --type instrumentation \
      --app ./app/build/outputs/apk/debug/app-debug.apk \
      --test ./app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
