# Getting Started

# Run the application

You can directly run the application with IDE. Alternatively, you can run the application with the following command:

### Build image

``` Bash
./gradlew jibDockerBuild -Djib.dockerClient.executable=${path_to_docker} 
```

This small application is using [jib](https://github.com/GoogleContainerTools/jib) as the image builder. I will provide
more info if you are interested in using jib.
By default, the image name is `java-playground:latest`. You can change the image name by changing the config in gradle
build file.

### Run container

```bash
docker run -d -p 8080:8080 -t java-playground:latest
```