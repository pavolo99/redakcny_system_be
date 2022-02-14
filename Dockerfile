FROM openjdk:11

COPY build/libs/redakcny_system_be-0.1-all.jar redakcny_system_be.jar

EXPOSE 8080

CMD ["java", "-jar", "redakcny_system_be.jar", "-Dmicronaut.environments=dev"]
