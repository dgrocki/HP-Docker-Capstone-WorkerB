FROM openjdk:8-jre-alpine
WORKDIR /

ADD ./build/libs/workerB-all.jar ./
CMD ["java",  "-jar", "./workerB-all.jar"]
