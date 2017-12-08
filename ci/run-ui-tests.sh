#!/bin/bash

set -e

#gcloud config set project $GCLOUD_PROJECT
#gcloud auth activate-service-account --key-file $GCLOUD_KEY_LOCATION

echo 'Running UI smoke tests...'
./ci/run-test-matrix.sh smoketest

if [ "$BRANCH_NAME" = "master" ]; then
    echo 'On master, running UI road tests...'
    sh './ci/run-test-matrix.sh roadtest'
else
    echo 'Not on master, skipping UI road tests.'
fi
