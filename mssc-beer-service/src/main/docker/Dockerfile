# With multi-stage builds, Using multiple FROM statements in Dockerfile
# Each FROM instruction can use a different base, and each of them begins a new
# stage of the build
# You can selectively copy artifacts from one stage to another, leaving behind
# everything you don’t want in the final image
FROM openjdk:16-slim as builder
# The WORKDIR instruction sets the working directory for any RUN, CMD,
# ENTRYPOINT, COPY and ADD instructions that follow it in the Dockerfile
# If the WORKDIR doesn’t exist, it will be created even if it’s not used in any
# subsequent Dockerfile instruction
# The WORKDIR instruction can be used multiple times in a Dockerfile
# If a relative path is provided, it will be relative to the path of the previous
WORKDIR application
ADD maven/${project.build.finalName}.jar ${project.build.finalName}.jar
RUN java -Djarmode=layertools -jar ${project.build.finalName}.jar extract

FROM openjdk:16-slim
LABEL PROJECT_NAME=${project.artifactId} \
      PROJECT=${project.id}

EXPOSE 8081

WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "org.springframework.boot.loader.JarLauncher"]