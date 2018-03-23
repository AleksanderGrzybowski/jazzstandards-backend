FROM openjdk:8-jdk

COPY . /app
WORKDIR /app
RUN ./gradlew clean bootRepackage


FROM openjdk:8-jre

COPY --from=0 /app/build/libs/app-0.0.1-SNAPSHOT.jar /
WORKDIR /
ENV SPRING_PROFILES_ACTIVE production

CMD java -Xmx40m -jar app-0.0.1-SNAPSHOT.jar

