FROM gradle:4.2.1-jdk8-alpine

RUN chmod -x gradlew # This changes ownership of folder
RUN ./gradlew genSources
RUN ./gradlew build --stacktrace