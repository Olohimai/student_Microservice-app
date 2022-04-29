# Other ways of using Docker
These are helpful commands if running the application without Docker Compose.

## Build application image locally using Gradle
`gradlew bootBuildImage --imageName=tvergilio/student`

## Start DATABASE ONLY using Docker
`docker run --name finance-db --publish 3307:3307 -e MARIADB_ROOT_PASSWORD=my-secret-pw -d mariadb:latest --port 3307`

Connect to the database manually and run migrations manually to create schema, application user and grants.

## Start APPLICATION ONLY using Docker
`docker run --publish 8090:8090 tvergilio/student`

You will need to make changes to `application.properties`

## Publish to Docker Hub
1. Log in
   `docker login --username=tvergilio`
   (replace tvergilio with your Docker Hub username)

3. Get List of Images
   `docker images`

4. Tag Image
   `docker tag tvergilio/student tvergilio/student:1.0`<br/>
   (replace tvergilio with your Docker Hub username; increment the version)

5. Push to Docker Hub
   `docker push tvergilio/student:1.0`

## Run container from image stored in Docker Hub
`docker run --publish 8090:8090 tvergilio/student:1.0`<br/>
(replace tvergilio with your Docker Hub username; check the version)

## To clean old images and containers
`docker system prune -a`<br/>

## To clean volumes
`docker system prune --volumes`