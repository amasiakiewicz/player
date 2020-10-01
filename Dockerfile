FROM adoptopenjdk:8-jre-openj9

EXPOSE 8020

ADD build/libs/*.jar player.jar

ENTRYPOINT ["java", "-jar", "player.jar"]
