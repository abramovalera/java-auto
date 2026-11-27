# Сервер курса: JDK 21 нужен и чтобы запустить сам сервер, и чтобы компилировать решения ученика.
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Библиотеки для модулей про автотесты: JUnit 5 (запуск тестов ученика) и AssertJ (красивые проверки).
# Качаются с Maven Central один раз при сборке образа.
ADD https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar /app/lib/junit-platform-console-standalone.jar
ADD https://repo1.maven.org/maven2/org/assertj/assertj-core/3.27.3/assertj-core-3.27.3.jar /app/lib/assertj-core.jar
ADD https://repo1.maven.org/maven2/net/bytebuddy/byte-buddy/1.15.11/byte-buddy-1.15.11.jar /app/lib/byte-buddy.jar

COPY server/ /app/server/
RUN javac -encoding UTF-8 -d /app/classes /app/server/*.java

ENV LANG=C.UTF-8 \
    SITE_DIR=/app/site \
    TASKS_DIR=/app/tasks \
    STARTER_DIR=/app/starter \
    LIB_DIR=/app/lib \
    WORKSPACE_DIR=/workspace \
    PORT=7070
EXPOSE 7070
CMD ["java", "-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8", "-cp", "/app/classes", "CourseServer"]
