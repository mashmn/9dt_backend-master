## 98Point6 Drop Token Java shim ##
This is provided as a possible starting point for a Java implementation. This code, based on [Dropwizard](http://www.dropwizard.io/1.1.0/docs/), requires maven and Java 1.8.

## To Run the application
After cloning the project, traverse to the 'java' folder
```
mvn clean package
```
The docker-compose.yaml file is in the 'java' folder, run the below to run the application once in the 'java' folder:
```
docker network create droptoken-ntw
```
```
docker-compose up
```
## Test service manually ##
```
 curl --header "Content-type: Application/json" -X POST http://localhost:8080/drop_token -d'{ "players":["p1", "p2"], "rows":4, "columns":4}'
```

## Extras ##
### Compile and update the jar in the target folder
`mvn clean package`

### Run Service ##
`java -jar target/9dt-backend-1.0-SNAPSHOT.jar server src/main/resources/local.yml`

### Docker commands
To access the postgres container created above, run the following:
```
docker exec -it droptoken-db-container psql -h localhost -p 5432 -U postgres -d postgres
```
