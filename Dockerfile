FROM amazoncorretto:22-alpine AS build
LABEL author="Schimidt"

ARG APP_BUILD_PATH=/tmp/app
ARG JAR_NAME=app.jar

RUN mkdir $APP_BUILD_PATH

COPY .mvn $APP_BUILD_PATH/.mvn
COPY mvnw $APP_BUILD_PATH/mvnw
COPY pom.xml $APP_BUILD_PATH/pom.xml
COPY src $APP_BUILD_PATH/src

WORKDIR $APP_BUILD_PATH

RUN --mount=type=cache,target=/root/.m2 ./mvnw clean package && \
    apk update && \
    apk add --no-cache binutils && \
    mv target/*.jar $JAR_NAME

RUN  jar -xvf $JAR_NAME

RUN jdeps --ignore-missing-deps -q  \
    --recursive  \
    --multi-release 22  \
    --print-module-deps  \
    --class-path 'BOOT-INF/lib/*'  \
    $JAR_NAME > deps.info

RUN jlink \
    --add-modules $(cat deps.info),jdk.jdwp.agent \
    --strip-debug \
    --no-header-files \
    --no-man-pages \
    --compress=zip-9 \
    --output /jre-slim

FROM alpine:latest

ARG APP_BUILD_PATH=/tmp/app
ARG JAR_NAME=app.jar
ARG APP_VERSION=latest
ENV JAR_NAME=${JAR_NAME}
ENV JAVA_REMOTE_DEBUG='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005'

LABEL maintainer="Denis Schimidt <denao@gmail.com>"
LABEL version=$APP_VERSION
LABEL description="App para estudo de observabilidade"

ENV APP_PATH=/opt/observability
ENV APP_VERSION=$APP_VERSION
ENV JAVA_HOME=/opt/jre-slim
ENV PATH="$PATH:$JAVA_HOME/bin"
COPY --from=build /jre-slim $JAVA_HOME

WORKDIR $APP_PATH
COPY --from=build $APP_BUILD_PATH/src/main/resources/scripts/start.sh start.sh
COPY --from=build $APP_BUILD_PATH/$JAR_NAME $APP_PATH/$JAR_NAME

RUN addgroup -S appgroup && adduser -S app-user -G appgroup
USER app-user

EXPOSE 8080
ENTRYPOINT ["sh","start.sh"]

