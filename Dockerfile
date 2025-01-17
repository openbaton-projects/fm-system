FROM openjdk:8-jdk as builder
COPY . /project
WORKDIR /project
RUN ./gradlew build -x test

FROM openjdk:8-jre-alpine
COPY --from=builder /project/build/libs/*.jar /fms.jar
RUN mkdir -p /var/log/openbaton
RUN apk add -u --no-cache python3 python3-dev curl && pip3 install --no-cache-dir openbaton-cli==5.0.0rc1
COPY --from=builder /project/gradle/gradle/scripts/docker/fms.sh /fms.sh
COPY --from=builder /project/src/main/resources/application.properties /etc/openbaton/openbaton-fms.properties
ENTRYPOINT ["/fms.sh"]
EXPOSE 9000
