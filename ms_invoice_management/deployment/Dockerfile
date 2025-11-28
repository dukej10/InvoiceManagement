FROM eclipse-temurin:21-jdk-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
VOLUME /tmp

COPY ../../applications/app-service/build/libs/InvoiceManagement.jar app.jar

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75 -Djava.security.egd=file:/dev/./urandom"

USER appuser
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]