# Devices API

A Spring Boot REST API for managing devices. This application provides endpoints for tracking and managing devices with their states (available, in use, or disabled).

## Technologies Used

- Java 21
- Spring Boot 3.4.5
- Spring Data JDBC
- PostgreSQL
- Liquibase for database migrations
- Docker and Docker Compose
- Swagger/OpenAPI for API documentation
- TestContainers for integration testing

## Prerequisites

- Java 21 or higher
- Maven
- Docker and Docker Compose

## Getting Started

### Clone the Repository

```bash
git clone <repository-url>
cd devicesAPI
```

### Run with Docker Compose

The application is configured to use Docker Compose for local development:

```bash
# Start the PostgreSQL database
docker-compose up -d

# Build and run the application
./mvnw spring-boot:run
```

### Build and Run Manually

```bash
# Build the application
./mvnw clean package

# Run the application
java -jar target/devicesAPI-0.0.1-SNAPSHOT.jar
```

## API Documentation

The API documentation is available via Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

## API Endpoints

- `GET /api/device/{id}` - Get a device by ID

## Database Schema

The application uses a PostgreSQL database with the following schema:

### Device Table

| Column        | Type      | Description                                |
|---------------|-----------|--------------------------------------------|
| id            | SERIAL    | Primary key                                |
| name          | VARCHAR   | Device name                                |
| brand         | VARCHAR   | Device brand                               |
| state         | INTEGER   | Device state (0=AVAILABLE, 1=IN_USE, 2=DISABLED) |
| creation_time | TIMESTAMP | When the device was created                |

## Configuration

The application can be configured using the following properties files:

- `application.properties` - Default configuration
- `application-dev.properties` - Development environment configuration
- `application-prod.properties` - Production environment configuration

## Running Tests

```bash
# Run all tests
./mvnw test
```

## Development

The project uses Spring Boot DevTools for development, which provides features like automatic restart when files change.

### Project Structure

- `src/main/java/com/carlos/devices` - Main application code
  - `controller` - REST controllers
  - `domain` - Domain models and repository interfaces
  - `repository` - Repository implementations

- `src/main/resources` - Configuration files
  - `db/changelog` - Liquibase database migration scripts

- `src/test` - Test code

## License

[Add license information here]