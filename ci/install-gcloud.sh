#!/bin/bash

set -e

# travis is configured to cache the installation
if [ ! -d $HOME/google-cloud-sdk ]; then
  curl https://dl.google.com/dl/cloudsdk/channels/rapid/downloads/google-cloud-sdk-182.0.0-linux-x86_64.tar.gz -o gcloud.tar.gz
  tar xzf gcloud.tar.gz -C $HOME
  $GCLOUD_SDK_DIR/install.sh --quiet --usage-reporting false
fi

source $GCLOUD_SDK_DIR/path.bash.inc
