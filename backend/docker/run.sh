#!/usr/bin/env bash

. ./env.sh

if [[ $(docker ps -a -q -f name=${BACK_NAME}) ]]; then
  docker start ${BACK_NAME}
else
  docker run -d -p ${PORT}:8088 -p 192.168.1.129:5432:5432 --name ${BACK_NAME} ${BACK_TAG}:latest
fi