FROM anapsix/alpine-java

ENV CONSUL_LOCATION consul
ADD ./target/apache-0.0.1-SNAPSHOT.jar  /apache.jar

ENTRYPOINT ["java", "-jar", "/apache.jar"]
