#!/usr/bin/env bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
AWS_ACCESS_KEY_ID=test AWS_SECRET_ACCESS_KEY=test $SCRIPT_DIR/gradlew -p $SCRIPT_DIR run