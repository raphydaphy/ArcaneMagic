FROM gradle:4.2.1-jdk8-alpine
WORKDIR /app
COPY --from=0 /app/myProject /app

USER root                # This changes default user to root
RUN chown -R gradle /app # This changes ownership of folder
USER gradle              # This changes the user back to the default user "gradle"

RUN ./gradlew build --stacktrace