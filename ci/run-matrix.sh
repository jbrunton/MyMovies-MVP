#!/bin/bash

if [ "$1" != "" ]; then
    TEST_RUN_NAME=$1
else
    echo "Error: unspecified test run name."
    exit
fi

if [ "$2" != "" ]; then
    TEST_MATRIX=$2
else
    echo "Error: unspecified test matrix name."
    exit
fi

TEST_ID=$(dd bs=6 count=1 if=/dev/urandom 2> /dev/null | base64 | tr +/ ab)

RESULTS_DIR="$TEST_RUN_NAME-$TEST_MATRIX-$TEST_ID"
echo "Running $TEST_MATRIX, dir=$RESULTS_DIR, key=$GCLOUD_KEY_LOCATION"

gcloud config set project mymovies-7e138
gcloud auth activate-service-account --key-file $GCLOUD_KEY_LOCATION

echo "Running smoke tests..."
gcloud firebase test android run firebase-test-matrices.yml:$TEST_MATRIX \
    --type instrumentation \
    --results-bucket=ci-jbrunton-com-ui-tests \
    --results-dir=$RESULTS_DIR \
    --app ./app/build/outputs/apk/debug/app-debug.apk \
    --test ./app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk

OUTPUT_DIR="testlab-artifacts-$TEST_MATRIX"
mkdir $OUTPUT_DIR
gsutil rsync -r -m gs://ci-jbrunton-com-ui-tests/$RESULTS_DIR $OUTPUT_DIR

