FROM anapsix/alpine-java


ADD ./target/apache-0.0.1-SNAPSHOT.jar  /apache.jar

ENTRYPOINT ["java", "-jar", "/apache.jar"]
