FROM anapsix/alpine-java:8
ADD mocktric-*-all.jar app.jar
CMD ["java", "-jar", "app.jar"]