FROM adoptopenjdk/openjdk13:jre-13.0.2_8-ubuntu

# Add executable to the image
ADD build/libs/ .

# Add known levelshots to the image
ADD levelshots/ levelshots

ENTRYPOINT ["java", "-jar", "discord-wolfet.jar"]