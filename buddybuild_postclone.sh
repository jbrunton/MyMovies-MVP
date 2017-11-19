#!/usr/bin/env bash

# install gcloud sdk
curl -L https://dl.google.com/dl/cloudsdk/channels/rapid/downloads/google-cloud-sdk-180.0.0-darwin-x86_64.tar.gz | tar xz

# add 'gcloud' to path
export PATH="`pwd`/google-cloud-sdk/bin:$PATH"

# authenticate
gcloud config set project "$GCLOUD_PROJECT"
gcloud auth activate-service-account --key-file "gcloudkey.json" "$GCLOUD_USER"
