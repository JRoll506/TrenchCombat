FROM maven

COPY . /usr/src/mymaven
WORKDIR /usr/src/mymaven

RUN mvn verify

FROM spigot

COPY ./build/ /usr/src/spigot
COPY --from=0 /usr/src/mymaven/target/TrenchCombat-*.jar ./plugins/.
WORKDIR /usr/src/spigot

EXPOSE 25565
