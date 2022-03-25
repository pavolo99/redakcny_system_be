# STEP 1 - ASSEMBLE APPLICATION AND GENERATE JAR APPLICATION
FROM gradle:7.4.1-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle assemble --no-daemon

# STEP 2 - TAKE BUILT JAR FILE AND SET CONFIGURATION FOR BE IMAGE
FROM openjdk:17-alpine
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/redakcny_system_be-0.1-all.jar redakcny_system_be.jar
ENTRYPOINT ["java", "-jar", "redakcny_system_be.jar"]
