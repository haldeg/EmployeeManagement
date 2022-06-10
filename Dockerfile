FROM openjdk:11
EXPOSE 8080
ADD target/employeestate-0.0.1-SNAPSHOT.jar employeestate-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","employeestate-0.0.1-SNAPSHOT.jar"]