### Build container
FROM openjdk:11 AS build

RUN apt update && apt upgrade -y
RUN apt install -y maven ant

WORKDIR /tmp/build

COPY . /tmp/build/sormas
RUN cd sormas/sormas-base \
 && mvn clean package -DskipTests \
 && ant collect-serverlibs

### Runtime container
FROM payara/server-full:5.2021.1-jdk11

USER root
RUN apt update && apt upgrade -y
RUN apt install -y curl dnsutils
USER payara

# copy serverlibs
COPY --from=build /tmp/build/sormas/deploy/serverlibs/*.jar $PAYARA_DIR/glassfish/lib/

# copy deployments
COPY --from=build /tmp/build/sormas/sormas-ear/target/sormas-ear.ear /tmp/deploy/sormas-ear.ear
COPY --from=build /tmp/build/sormas/sormas-rest/target/sormas-rest.war /tmp/deploy/sormas-rest.war
COPY --from=build /tmp/build/sormas/sormas-ui/target/sormas-ui.war /tmp/deploy/sormas-ui.war

# asadmin config and properties
COPY --chown=payara:payara ./docker-config/sormas.properties /opt/config/sormas.properties
COPY --chown=payara:payara ./docker-config/sormas.post-boot.asadmin $CONFIG_DIR/post-boot-commands.asadmin
COPY --chown=payara:payara ./docker-config/sormas.pre-boot.asadmin $CONFIG_DIR/pre-boot-commands.asadmin
