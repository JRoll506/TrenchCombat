FROM maven

WORKDIR /usr/src/mymaven
COPY pom.xml .
RUN mvn dependency:resolve verify
COPY . .
RUN mvn verify

FROM spigot:1.8.8

WORKDIR /usr/src/spigot
COPY ./build/ /usr/src/spigot
COPY --from=0 /usr/src/mymaven/target/TrenchCombat-*.jar ./plugins/.

EXPOSE 25565
