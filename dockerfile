FROM amazoncorretto:17-alpine
RUN addgroup -S basketFruitGroup && adduser -S user -G basketFruitGroup
USER user
ARG JAR_FILE=target/Demo-CICD-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]