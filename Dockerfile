FROM openjdk:11
COPY target/front-office-0.0.1-SNAPSHOT.jar /app/app.jar
WORKDIR /app
CMD ["java","-jar", "app.jar"]