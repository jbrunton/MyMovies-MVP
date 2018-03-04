#!/bin/bash

set -e

openssl aes-256-cbc -K $encrypted_10a1dc9634f9_key -iv $encrypted_10a1dc9634f9_iv \
  -in "$GCLOUD_KEY_FILE.enc" -out $GCLOUD_KEY_FILE -d
gcloud config set project $GCLOUD_PROJECT
gcloud auth activate-service-account --key-file=$GCLOUD_KEY_FILE
