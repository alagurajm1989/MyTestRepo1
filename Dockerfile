FROM openjdk:11.0.9.1-jre-slim
# RUN addgroup -S galore && adduser -S galore -G galore
# USER galore:galore
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 9023
ENTRYPOINT ["java","-cp","app:app/lib/*","com.onmobile.apps.galore.sms.SmsApplication"]
