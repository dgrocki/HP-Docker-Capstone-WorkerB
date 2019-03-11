FROM openjdk:8-jre-alpine
WORKDIR /

ADD ./build/libs/PDFFormat-all.jar ./
CMD ["java",  "-jar", "./PDFFormat-all.jar"]
