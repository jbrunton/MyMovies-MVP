#!/bin/bash

if [ -z "$BRANCH_NAME" ]; then
    echo "Error: unspecified branch name."
    exit
fi

if [ -z "$CHANGE_ID" ]; then
    TEST_RUN_NAME="$BRANCH_NAME"
else
    TEST_RUN_NAME="$CHANGE_ID-$BRANCH_NAME"
fi

./ci/run-matrix.sh $TEST_RUN_NAME smoketest

if [ $BRANCH_NAME = "master" ]; then
    ./ci/run-matrix.sh $TEST_RUN_NAME roadtest
fi
