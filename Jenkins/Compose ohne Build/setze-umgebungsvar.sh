#!/usr/bin/env bash

export MOVIES_VERSION="$(git log | head -1 | sed s/'commit '//)"
echo $MOVIES_VERSION