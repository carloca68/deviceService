# Global Devices API

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
- JaCoCo for code coverage

## Prerequisites

- Java 21 or higher
- Maven
- Docker and Docker Compose

## Getting Started

### Clone the Repository

```bash
git clone <repository-url>
cd globalDevicesAPI
```

### Run with Docker Compose

The application is configured to use Docker Compose for local development:

```bash
# Start the PostgreSQL database
docker compose up -d

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
http://localhost:8080/swagger-ui/index.html
```

### Postman Collection

A Postman collection is available in the repository for testing the API endpoints:

1. Import the `postman_collection.json` file into Postman
2. Create an environment in Postman with a variable `baseUrl` set to your server URL (default: `http://localhost:8080`)
3. The collection includes tests for all endpoints and will automatically store created device IDs for use in subsequent requests

## API Endpoints

### Device Management

- `GET /api/device/{id}` - Get a device by ID
- `GET /api/device/brand/{brand}` - Get all devices by brand
- `GET /api/device/state/{state}` - Get all devices by state (AVAILABLE, IN_USE, DISABLED)
- `GET /api/device` - Get all devices
- `POST /api/device` - Create a new device
- `PUT /api/device/{id}` - Update an existing device
- `DELETE /api/device/{id}` - Delete a device

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
- `application-test.properties` - Test environment configuration

## Running Tests

```bash
# Run all tests
./mvnw test

# Run tests with coverage report
./mvnw test jacoco:report
```

The JaCoCo coverage report will be available in `target/site/jacoco/index.html`.

## Testing Approach

The project uses different testing approaches:

1. **Unit Tests**: Testing individual components in isolation
   - `DeviceRestControllerTest` - Tests the REST controller using MockMVC and mocked service

2. **Integration Tests**: Testing with real dependencies
   - Uses TestContainers to spin up a PostgreSQL database for integration tests

## Development

The project uses Spring Boot DevTools for development, which provides features like automatic restart when files change.

### Project Structure

- `src/main/java/com/carlos/app` - Application bootstrap code
  - `DevicesApiApplication.java` - Main application class

- `src/main/java/com/carlos/devices` - Main application code
  - `DeviceRestController.java` - REST controller for device endpoints
  - `domain` - Domain models, services, and repository interfaces
    - `model` - Data models (Device, DeviceState, CreateUpdateDevice)
    - `useCases` - Service implementations
    - `exception` - Custom exceptions
  - `repository` - Repository implementations

- `src/main/resources` - Configuration files
  - `db/changelog` - Liquibase database migration scripts
  - `application*.properties` - Application configuration

- `src/test` - Test code
  - `com/carlos/app` - Application test configuration
  - `com/carlos/devices` - Controller and service tests

## License

This project is licensed under the MIT License - see the LICENSE file for details.
