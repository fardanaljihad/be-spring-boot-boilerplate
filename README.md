# Spring Boot Learning Boilerplate

## 🚀 Purpose

This project serves as a foundational boilerplate for backend development using Java and the Spring Boot framework. Its primary goal is to **accelerate the learning curve for new developers** joining the team by showcasing common patterns, best practices, and integrations with widely used technologies in a structured manner.

It provides a starting point for building robust RESTful APIs with connections to SQL databases (PostgreSQL), NoSQL databases (MongoDB), and caching layers (Redis).

## ✨ Key Features & Technologies

*   **Framework:** Spring Boot 3.x (or specify your version)
*   **Language:** Java 17 (or specify your version)
*   **Build Tool:** Maven (or Gradle)
*   **Web:** Spring Web (for RESTful APIs using embedded Tomcat)
*   **Database (SQL):**
    *   Spring Data JPA
    *   Hibernate
    *   PostgreSQL Driver (configured by default)
*   **Database (NoSQL):**
    *   Spring Data MongoDB
*   **Caching:**
    *   Spring Cache Abstraction
    *   Spring Data Redis
*   **Data Transfer:** DTO (Data Transfer Object) pattern
*   **Validation:** Jakarta Bean Validation (`jakarta.validation-api`)
*   **Code Reduction:** Lombok
*   **Testing:**
    *   JUnit 5
    *   Mockito
    *   Spring Boot Test Utilities (`@SpringBootTest`, `@WebMvcTest`, etc.)
*   **Development Tools:** Spring Boot DevTools (automatic restarts, live reload)
*   **Configuration:** Properties file (`application.properties`) with examples for different environments (though profiles are recommended for real use).
*   **Dependency Management:** Docker Compose for easy local setup of external services (PostgreSQL, MongoDB, Redis).
*   **API:** Basic REST controllers with CRUD examples.
*   **Error Handling:** Global Exception Handling (`@ControllerAdvice`).
*   **Logging:** SLF4j with Logback (default in Spring Boot).

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

1.  **Java Development Kit (JDK):** Version 17 or later (LTS recommended). [Download OpenJDK](https://adoptium.net/)
2.  **Build Tool:** Apache Maven 3.6+ or Gradle 7+. [Install Maven](https://maven.apache.org/install.html) | [Install Gradle](https://gradle.org/install/)
3.  **IDE:** An IDE that supports Java and Maven/Gradle (e.g., IntelliJ IDEA, Eclipse, VS Code with Java extensions).
4.  **Docker & Docker Compose:** Required for easily running local database and cache instances. [Install Docker](https://docs.docker.com/get-docker/)

## ⚙️ Setup & Installation

1.  **Clone the repository:**
    ```bash
    git clone https://gitlab.cloudias79.com/internal-apps/core/boilerplate/internal-boilerplate/be-spring-boot-boilerplate.git
    cd be-springboot-boilerplate
    ```
2.  **Import the project:** Open the project directory in your preferred IDE as a Maven (or Gradle) project. The IDE should automatically download dependencies.

## 🔧 Configuration

Application configuration is primarily managed in `src/main/resources/application.properties`.

*   **Default Settings:** The default configuration connects to services running on `localhost` with default ports and predefined credentials (suitable for local development with the provided Docker Compose setup).
    *   PostgreSQL: `jdbc:postgresql://localhost:5432/boilerplate_db` (user: `boilerplate_user`, pass: `boilerplate_secret`)
    *   MongoDB: `mongodb://localhost:27017/boilerplate_mongo_db` (no auth by default)
    *   Redis: `localhost:6379` (no auth by default)
*   **Database Schema:** `spring.jpa.hibernate.ddl-auto` is set to `update` by default. This **automatically creates/updates** database tables based on JPA entities upon application startup.
    *   ⚠️ **Warning:** This is convenient for development but **unsafe for production**. Use database migration tools like Flyway or Liquibase and set `ddl-auto` to `validate` or `none` in production.
*   **Environment Variables (Recommended):** For sensitive information (like production database passwords) or environment-specific settings, **use environment variables or Spring Profiles (`application-<profile>.properties`)**. Spring Boot automatically picks up environment variables that match property names (e.g., `SPRING_DATASOURCE_PASSWORD` overrides `spring.datasource.password`).
    ```bash
    # Example: Setting password via environment variable
    export SPRING_DATASOURCE_PASSWORD="your_secure_password"
    java -jar target/learning-boilerplate-*.jar
    ```

## 🐳 Running Dependencies (Docker Compose)

To easily run the required external services (PostgreSQL, MongoDB, Redis) locally, use the provided Docker Compose configuration:

1.  **Start Services:**
    ```bash
    docker-compose up -d
    ```
    *   `-d` runs the containers in detached mode (in the background).
    *   This will start PostgreSQL on port 5432, MongoDB on 27017, and Redis on 6379, matching the default `application.properties`.
    *   Data will be persisted in Docker volumes (`postgres_data`, `mongo_data`, `redis_data`).

2.  **Stop Services:**
    ```bash
    docker-compose down
    ```
    *   This stops and removes the containers. Volumes are preserved unless you use `docker-compose down -v`.

3.  **View Logs:**
    ```bash
    docker-compose logs -f             # Follow logs for all services
    docker-compose logs -f postgres    # Follow logs for postgres only
    ```

## ▶️ Running the Application

1.  **Make sure dependencies are running** (use `docker-compose up -d`).
2.  Choose one of the following methods:

    *   **Via IDE:** Locate the `LearningBoilerplateApplication.java` class in `src/main/java/...` and run it directly from your IDE (usually via right-click -> Run).
    *   **Via Maven:** Open a terminal in the project root directory and run:
        ```bash
        mvn spring-boot:run
        ```
    *   **Via Executable JAR:**
        First, build the JAR file:
        ```bash
        mvn clean package
        ```
        Then, run the JAR:
        ```bash
        java -jar target/learning-boilerplate-*.jar
        ```

The application will start, typically on `http://localhost:8080`. Check the console logs for the exact port and startup messages.

## ✅ Running Tests

To run the unit and integration tests included in the project:

```bash
mvn test
```

This command executes all tests found in the `src/test/java` directory. Test results will be shown in the console, and reports are usually generated in the `target/surefire-reports` directory.

## 📡 API Endpoints Examples

Once the application is running, you can interact with the API endpoints using tools like `curl`, Postman, or Insomnia.

*   **Health Check (Basic Ping):**
    ```bash
    curl http://localhost:8080/ping
    # Expected Response: pong
    ```

*   **Actuator Health (Detailed):** (Requires Actuator dependency and configuration)
    ```bash
    curl http://localhost:8080/actuator/health
    # Expected Response: {"status":"UP", "components":{...}} (Details depend on config)
    ```

*   **Get All Users:**
    ```bash
    curl http://localhost:8080/api/v1/users
    # Expected Response: JSON array of user DTOs, e.g., [] or [{...},{...}]
    ```

*   **Create a User:**
    ```bash
    curl -X POST http://localhost:8080/api/v1/users \
       -H "Content-Type: application/json" \
       -d '{
             "username": "johndoe",
             "email": "john.doe@example.com",
             "password": "secretpassword"
           }'
    # Expected Response: 201 Created status with JSON of the created user DTO
    ```

*   **Get User by ID:**
    ```bash
    curl http://localhost:8080/api/v1/users/1
    # Expected Response: JSON of the user DTO with ID 1, or 404 Not Found
    ```

*   **(See `UserController.java` and `ProductController.java` for more endpoints)**

## 📁 Project Structure

The project follows a standard layered architecture:

```
src/
├── main/
│   ├── java/
│   │   └── com/yourcompany/learningboilerplate/
│   │       ├── LearningBoilerplateApplication.java # Main entry point
│   │       ├── config/         # Spring configuration classes (e.g., Security, Cache)
│   │       ├── controller/     # REST API controllers (handling HTTP requests)
│   │       ├── dto/            # Data Transfer Objects (for API request/response)
│   │       ├── exception/      # Custom exceptions and global handler
│   │       ├── model/          # Data models
│   │       │   ├── document/   # MongoDB documents (@Document)
│   │       │   └── entity/     # JPA entities (@Entity)
│   │       ├── repository/     # Data access layer (Spring Data repositories)
│   │       │   ├── jpa/        # JPA Repositories
│   │       │   └── mongo/      # MongoDB Repositories
│   │       ├── service/        # Business logic layer
│   │       └── util/           # Utility classes
│   └── resources/
│       ├── application.properties # Main configuration file
│       ├── static/           # Static web assets (optional)
│       └── templates/        # Server-side templates (optional)
└── test/                     # Test sources (mirroring main structure)
    └── java/
        └── com/yourcompany/learningboilerplate/
            ├── controller/
            ├── service/
            └── ...
docker-compose.yml            # Docker configuration for dependencies
pom.xml                       # Maven project configuration
README.md                     # This file
```

## ✨ Best Practices Highlighted

This boilerplate attempts to demonstrate several best practices:

*   **Layered Architecture:** Separation of concerns (Controller, Service, Repository).
*   **Dependency Injection:** Using constructor injection (`@RequiredArgsConstructor`) for testability and immutability.
*   **DTO Pattern:** Separating API data contracts from internal domain models.
*   **Global Exception Handling:** Consistent error responses using `@ControllerAdvice`.
*   **Repository Pattern:** Abstracting data access using Spring Data JPA/MongoDB.
*   **Configuration Management:** Using `application.properties` (extendable with profiles).
*   **Use of Lombok:** Reducing boilerplate code (getters, setters, constructors).
*   **Unit Testing:** Demonstrating tests for the service layer using Mockito.
*   **Basic Caching:** Example using Spring Cache abstraction with Redis.
*   **Input Validation:** Using Jakarta Bean Validation annotations in DTOs.
*   **Logging:** Meaningful logging using SLF4j.

## 🌱 Further Learning & Enhancements

This boilerplate is a starting point. Consider exploring and adding:

*   **Spring Security:** Implement authentication (e.g., JWT) and authorization.
*   **API Documentation:** Integrate Springdoc OpenAPI (Swagger UI).
*   **Database Migrations:** Add Flyway or Liquibase for schema management.
*   **Mapping:** Use MapStruct for efficient DTO-Entity mapping.
*   **Spring Profiles:** Define `application-dev.properties`, `application-prod.properties`, etc.
*   **Integration Testing:** Add tests using `@SpringBootTest` and Testcontainers.
*   **Containerization:** Create a `Dockerfile` for the application itself.
*   **CI/CD:** Set up a pipeline (GitHub Actions, GitLab CI, Jenkins).
*   **Monitoring:** Expose more Actuator endpoints and integrate with Prometheus/Grafana.
*   **Async Processing:** Use `@Async` for background tasks.
*   **Message Queues:** Integrate Kafka or RabbitMQ if needed.

## 🤝 Contributing

Contributions are welcome! Please follow standard practices like creating issues for discussion and submitting pull requests for changes. (Add more specific guidelines if needed).

## 📄 License

This project is licensed under the [MIT License](LICENSE) (or choose another license like Apache 2.0 and add the corresponding LICENSE file).
```

**Next Steps:**

1.  **Replace Placeholders:** Change `<your-repository-url>` to the actual URL of your Git repository. Update the Spring Boot and Java versions if they differ from the examples. Change `com.yourcompany.learningboilerplate` if you used a different package structure.
2.  **Add License File:** If you mention a license (like MIT), create a `LICENSE` file in the root directory containing the text of that license. You can find standard license texts easily online (e.g., [choosealicense.com](https://choosealicense.com/)).
3.  **Commit:** Add `README.md` (and `LICENSE` if added) to your Git repository and commit the changes.