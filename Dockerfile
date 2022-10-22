FROM czb0.czbpub.com:5000/jre:8.090102
MAINTAINER Mephis Pheies <mephistommm@gmail.com>

RUN mkdir -p /app/config
EXPOSE 8080
WORKDIR /app
COPY ./target/se-server-0.1.0-SNAPSHOT.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]
