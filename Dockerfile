FROM amazoncorretto:17
WORKDIR /


ENV LANG=C.UTF-8 JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto
ENV PATH="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:$PATH"

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
