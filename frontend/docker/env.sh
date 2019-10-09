#!/usr/bin/env bash

if [[ -z "${APP}" ]]; then
  export APP="codenames"
fi

if [[ -z "${FRONT_SUFFIX}" ]]; then
  export FRONT_SUFFIX="front-vuejs"
fi

if [[ -z "${FRONT_NAME}" ]]; then
  export FRONT_NAME="codenames-${FRONT_SUFFIX}"
fi

if [[ -z ${FRONT_TAG} ]]; then
  export FRONT_TAG="jieiiia/${FRONT_NAME}"
fi

if [[ -z "${PORT}" ]]; then
  export PORT=3333
fi
