
### Using Spring Boot Docker Layers

1.  Config ``` spring-boot-maven-plugin ```

    ```  
        <plugins>
            ... ...
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <layers>
                        <enabled>true</enabled>
                        <includeLayerTools>true</includeLayerTools>
                    </layers>
                </configuration>
            </plugin>
            ... ...
        </plugins>
    ```

2.  Add ``` docker-maven-plugin ```

    ```  
        <plugins>
            ... ...
            <plugin>
                <groupId>io.fabric8</groupId>
                <artifactId>docker-maven-plugin</artifactId>
                <version>0.34.1</version>
                <configuration>
                    <verbose>true</verbose>
                    <images>
                        <image>
                            <name>
                                ${docker.image.prefix}/${docker.image.name}
                            </name>
                            <alias>${project.artifactId}</alias>
                            <build>
                                <!--copies artifact to docker build dir in target-->
                                <assembly>
                                    <descriptorRef>artifact</descriptorRef>
                                </assembly>
                                <dockerFile>Dockerfile</dockerFile>
                                <tags>
                                    <tag>latest</tag>
                                    <tag>${project.version}</tag>
                                </tags>
                            </build>
                        </image>
                    </images>
                </configuration>
            </plugin>
            ... ...
        </plugins>
    ```

3.  Add ``` Dockerfile ``` 

    - Add new ``` Dockerfile ``` to ``` src/main/docker ``` folder
    - This Dockerfile uses [use multi-stage builds](https://docs.docker.com/develop/develop-images/multistage-build/)
    
      ``` 
        FROM openjdk:16-slim as builder
        WORKDIR application
        ADD maven/${project.build.finalName}.jar ${project.build.finalName}.jar
        RUN java -Djarmode=layertools -jar ${project.build.finalName}.jar extract
        
        FROM openjdk:16-slim
        LABEL PROJECT_NAME=${project.artifactId} \
              PROJECT=${project.id}
        
        EXPOSE 8083
        
        WORKDIR application
        COPY --from=builder application/dependencies/ ./
        COPY --from=builder application/spring-boot-loader/ ./
        COPY --from=builder application/snapshot-dependencies/ ./
        COPY --from=builder application/application/ ./
        ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "org.springframework.boot.loader.JarLauncher"]
      ```

4.  Build Image

    - ``` $ mvn clean package docker:build ```
    - ``` $ docker images -a ```
    - ``` $ docker history sfgbeerworks/mssc-beer-inventory-service ```


### Resources

- [Docker Image: openjdk:16-slim](https://hub.docker.com/layers/openjdk/library/openjdk/16-slim/images/sha256-ac208c5060f1866d76c415009f81ed5c70e68ebef5f6d9f8e355c3365a192667?context=explore)
- [Why You Should be Using Spring Boot Docker Layers](https://springframework.guru/why-you-should-be-using-spring-boot-docker-layers/)
- [Docker Cheat Sheet for Spring Developers](https://springframework.guru/docker-cheat-sheet-for-spring-devlopers/)
- [fabric8io/docker-maven-plugin](https://github.com/fabric8io/docker-maven-plugin)
- [fabric8io/docker-maven-plugin Doc](http://dmp.fabric8.io/)
- [Use multi-stage builds](https://docs.docker.com/develop/develop-images/multistage-build/)
