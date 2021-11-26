FROM anapsix/alpine-java
VOLUME /tmp
ADD sstudent-0.0.1.jar app.jar
EXPOSE 9095
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
