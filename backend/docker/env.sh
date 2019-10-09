#!/usr/bin/env bash

if [[ -z "${APP}" ]]; then
  export APP="codenames"
fi

if [[ -z "${BACK_SUFFIX}" ]]; then
  export BACK_SUFFIX="back"
fi

if [[ -z "${BACK_NAME}" ]]; then
  export BACK_NAME="${APP}-${BACK_SUFFIX}"
fi

if [[ -z ${BACK_TAG} ]]; then
  export BACK_TAG="jieiiia/${BACK_NAME}"
fi

if [[ -z "${PORT}" ]]; then
  export PORT=8181
fi
