FROM eclipse-temurin:25-jdk AS build
RUN apt-get update && apt-get install -y --no-install-recommends maven unzip \
    && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B jpro:release
RUN cd target && unzip -q *-jpro.zip -d /release && \
    mv /release/*/* /release/ 2>/dev/null; \
    chmod +x /release/bin/start.sh

FROM eclipse-temurin:25-jre
RUN apt-get update && apt-get install -y --no-install-recommends \
    libgtk-3-0 libxtst6 libxrender1 libxi6 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=build /release/ .

EXPOSE 8080
CMD ["bash", "bin/start.sh"]
