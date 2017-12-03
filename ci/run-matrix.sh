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

echo "TEST_RUN_NAME: $TEST_RUN_NAME"
echo "TEST_MATRIX: $TEST_MATRIX"

RESULTS_DIR="$TEST_RUN_NAME-$TEST_MATRIX-$TEST_ID"
echo "RESULTS_DIR: $RESULTS_DIR"