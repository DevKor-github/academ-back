FROM gradle:8.8.0-jdk21 as build

COPY ./settings.gradle /proj/settings.gradle
COPY ./build.gradle /proj/build.gradle
COPY ./src /proj/src
COPY ./gradle /proj/gradle
COPY ./build /proj/build

WORKDIR /proj

RUN gradle assemble

# 만들어진 jar 파일 (하나) 을 복사
RUN file=$(ls /proj/build/libs/*.jar) && cp "$file" /app.jar

ENV PROFILE=production

ENTRYPOINT ["java","-jar","/app.jar"]