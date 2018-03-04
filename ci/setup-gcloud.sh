#!/bin/bash

set -e

# Install gcloud
curl https://dl.google.com/dl/cloudsdk/channels/rapid/downloads/google-cloud-sdk-182.0.0-linux-x86_64.tar.gz -o gcloud.tar.gz
tar xzf gcloud.tar.gz -C $HOME
$GCLOUD_SDK_DIR/install.sh --quiet --usage-reporting false
source $GCLOUD_SDK_DIR/path.bash.inc

# Decrypt the key file
openssl aes-256-cbc -K $encrypted_10a1dc9634f9_key -iv $encrypted_10a1dc9634f9_iv \
  -in "$GCLOUD_KEY_FILE.enc" -out $GCLOUD_KEY_FILE -d

gcloud config set project $GCLOUD_PROJECT
gcloud auth activate-service-account --key-file=$GCLOUD_KEY_FILE
