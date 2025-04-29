# Devices API

A Spring Boot application for managing devices with different states.

## Project Overview

Devices API is a RESTful service that allows you to manage devices. Each device has an ID, name, brand, and state (AVAILABLE, IN_USE, or DISABLED).

## Technologies Used

- **Java**: Core programming language
- **Spring Boot**: Framework for creating stand-alone, production-grade Spring-based applications
- **Spring Data JDBC**: For database operations
- **Spring Web Services**: For creating SOAP web services
- **PostgreSQL**: Database for storing device information
- **Docker Compose**: For containerization and easy deployment
- **Testcontainers**: For integration testing with real database instances
- **Maven**: For dependency management and build automation

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Docker and Docker Compose

## Getting Started

### Clone the Repository

```bash
git clone <repository-url>
cd devicesAPI
```

### Build the Application

```bash
./mvnw clean install
```

### Run with Docker Compose

The application is configured to use Docker Compose for local development:

```bash
docker-compose up -d
```

This will start a PostgreSQL database with the following configuration:
- Database: mydatabase
- Username: myuser
- Password: secret
- Port: 5432

### Run the Application

```bash
./mvnw spring-boot:run
```

## Database Configuration

The application is configured to connect to a PostgreSQL database. The connection details are:

- Database: mydatabase
- Username: myuser
- Password: secret

## API Documentation

The API provides endpoints for managing devices. Each device has the following properties:

- `id`: Unique identifier for the device
- `name`: Name of the device
- `brand`: Brand of the device
- `state`: Current state of the device (AVAILABLE, IN_USE, or DISABLED)

Detailed API documentation will be available once the application is running.

## Testing

The project uses Testcontainers for integration testing with a real PostgreSQL database:

```bash
./mvnw test
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the [MIT License](LICENSE).