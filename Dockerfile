# STEP 1 - ASSEMBLE APPLICATION AND GENERATE JAR APPLICATION
FROM gradle:latest AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle assemble --no-daemon

# STEP 2 - TAKE BUILT JAR FILE AND SET CONFIGURATION FOR BE IMAGE
FROM openjdk:11-jre-slim
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/redakcny_system_be-0.1-all.jar redakcny_system_be.jar
CMD ["java", "-jar", "redakcny_system_be.jar"]
