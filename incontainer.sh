#!/bin/sh
docker pull ubuntu
docker images
mkdir /opt/ca-test && cd /opt/ca-test
echo -e "FROM ubuntu:18.04 \nLABEL maintainer='Alaguraj <alaguraj.vkm@gmail.com>'\nRUN apt-get update && \\ \n    apt-get -qy full-upgrade && \\ \n    apt-get install -qy curl && \\ \n    apt-get install -qy curl && \\ \n    curl -sSL https://get.docker.com/ | sh" > Dockerfile
docker build -t test-image .
