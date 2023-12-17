# Eshop RESTful API
This is an updated version of the Spring Boot application for the legacy Piotr backend application.      

## Running the Application
To run the application, execute the following command:
`./mvnw spring-boot:run`    

To specify particular profile:      
`./mvnw spring-boot:run -Dspring-boot.run.profiles=dev,mon,amq`

The application will start and you can access it at http://localhost:8080.

### Useful Commands
Here are some useful commands that you can use with `mvnw` or `mvn`:

`mvnw clean`: cleans the target directory    
`mvnw compile`: compiles the application      
`mvnw package`: packages the application as a JAR file  
`mvn clean package`: cleans the build directory    
`mvnw test`: runs the unit tests       
`mvnw dependency:resolve`: resolves all dependencies required by the project and downloads them to your local repository        
`./mvnw dependency:tree`: Displays the dependency tree of the project        
`./mvnw spring-boot:run`: Runs the Spring Boot application     
Note that if you're using mvn instead of mvnw, replace ./mvnw with mvn in the above commands      

## Running PostgreSQL and pgAdmin with Docker Compose
To run the PostgreSQL database and pgAdmin in Docker containers, follow these steps:

1. Install Docker on your local machine if you haven't already.
2. Open a terminal window and navigate to the root of the project containing the `docker-compose.yml` file.
3. Run the following command to start the containers:
`docker-compose up -d`       
4. This will start the PostgreSQL container and the pgAdmin container in the background.
5. To access pgAdmin, open your web browser and go to http://localhost:5050.
6. Log in with the email and password specified in the `docker-compose.yml` file.
7. Click on the "Add New Server" button and enter details also specified in the  `docker-compose.yml` file.
8. Click "Save" to save the server details.
9. You should now be able to access the piotr database from pgAdmin. To stop the containers, run the following command in the terminal:
`docker-compose down`

### Useful Docker Compose Commands
Here are some useful commands you can use with Docker Compose:

`docker-compose up -d`: Starts the containers in the background          
`docker-compose down`: Stops the containers     
`docker-compose logs -f`: Shows the logs of the containers (useful for debugging)     
`docker-compose ps`: Lists the running containers     
`docker-compose build`: Builds the Docker images defined in the docker-compose.yml file     

If you want to remove the volumes that were created by the container, run the following command:
`docker volume prune`

# VS Code extensions for Java
`Extension Pack for Java v0.25.15`