FROM openjdk:17-slim
WORKDIR /app
COPY target/*.jar app.jar
RUN apt-get update && apt-get install -y iputils-ping netcat redis-tools
ENTRYPOINT ["java","-jar","app.jar"]
