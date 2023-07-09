# Todo Application 

The purpose of this todo application is to serve as an example for the various use cases covered by the book.



### Running the Application on Your Local Machine



You can now log in with the following users: `duck`, `tom`, `alice`, `will`. They all have the same password `stratospheric`.

### Application Profiles

- `dev` running the application locally for development. You don't need any AWS account or running AWS services for this. All infrastructure components are started within `docker-compose.yml`.
- `aws` running the application inside AWS. This requires the whole infrastructure setup inside your AWS account.

### Running the Tests

Run `./gradlew build` from the command line.




## Architecture

### Model

#### Class structure
![alt text][class-diagram]

#### Entity-relationship
![alt text][entity-relationship-diagram]



[class-diagram]:https://github.com/stratospheric-dev/stratospheric/raw/main/application/docs/Todo%20App%20-%20Class%20Diagram.png "class diagram"
[entity-relationship-diagram]:https://github.com/stratospheric-dev/stratospheric/raw/main/application/docs/Todo%20App%20-%20ER%20diagram.png "entity-relationship diagram"
[database-schema-diagram]:https://github.com/stratospheric-dev/stratospheric/raw/main/application/docs/Todo%20App%20-%20ER%20diagram%20from%20database%20schema.png "database schema diagram"

## Built with

* [Spring Boot](https://projects.spring.io/spring-boot/) and the following starters: Spring Web MVC, Spring Data JPA, Spring Cloud AWS, Spring WebFlux, Spring WebSocket, Thymeleaf, Spring Mail, Spring Validation, Spring Security, Actuator, OAuth2 Client
* [Gradle](https://gradle.org/)


