#!/bin/sh
docker pull ubuntu
docker images
mkdir /opt/ca-test && cd /opt/ca-test
echo -e "FROM openjdk:11.0.9.1-jre-slim \nARG DEPENDENCY=target/dependency\nCOPY ${DEPENDENCY}/BOOT-INF/lib /app/lib \nCOPY ${DEPENDENCY}/META-INF /app/META-INF \nCOPY ${DEPENDENCY}/BOOT-INF/classes /app\nEXPOSE 9023 \nENTRYPOINT ["java","-cp","app:app/lib/*","com.onmobile.apps.galore.sms.SmsApplication"]" > Dockerfile
docker build -t messa-image .
