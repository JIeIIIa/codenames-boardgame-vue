#!/usr/bin/env bash

. ./env.sh

if [[ $(docker ps -a -q -f name=${FRONT_NAME}) ]]; then
  docker start ${FRONT_NAME}
else
  docker run -d -p ${PORT}:80 --name ${FRONT_NAME} ${FRONT_TAG}:latest
fi