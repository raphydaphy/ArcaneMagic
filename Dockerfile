FROM openjdk:8-jdk
CMD ["chmod", "+x", "gradlew"]
CMD ["./gradlew", "genSources"]
CMD ["./gradlew", "build"]
ADD build/libs/arcanemagic-1.0.0.jar arcanemagic-1.0.0.jar