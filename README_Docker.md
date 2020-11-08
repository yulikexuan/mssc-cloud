
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