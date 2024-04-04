#!/usr/bin/env bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

if [ ! -d "localstack" ]; then
  echo "localstack not installed -> DOWNLOADING it"
  wget https://github.com/localstack/localstack-cli/releases/download/v3.3.0/localstack-cli-3.3.0-linux-amd64.tar.gz
  tar zxvf localstack-cli-3.3.0-linux-amd64.tar.gz
  rm localstack-cli-3.3.0-linux-amd64.tar.gz
fi

$SCRIPT_DIR/localstack/localstack start &
$SCRIPT_DIR/localstack/localstack wait && $SCRIPT_DIR/tournament-api/start.sh