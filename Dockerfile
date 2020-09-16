FROM openjdk:8-jdk-alpine
COPY build/libs/products-api-*.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
