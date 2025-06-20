# Microservices Architecture with Spring Boot and MongoDB

## Project Overview

This project implements a microservices architecture using Spring Boot and MongoDB. The system consists of multiple microservices that work together to provide a complete blog platform functionality. The services are containerized using Docker for easy deployment and scaling.

## Repository Information

- Repository URL: https://github.com/developer-shubham101/microservices-architecture.git

To clone the repository:
```bash
git clone https://github.com/developer-shubham101/microservices-architecture.git
cd microservices-architecture
```

## Additional Resources

For a deep dive into Spring Boot concepts and best practices, check out our comprehensive documentation:
- [Spring Boot Deep Dive Documentation](https://github.com/developer-shubham101/SpringBootSample/blob/main/spring-boot-bootcamp/docs/docs.md)

## Services Overview

The project consists of the following microservices:

1. **Service Discovery (Port: 8761)**
   - Eureka server for service registration and discovery
   - Central registry for all microservices

2. **API Gateway (Port: 8084)**
   - Single entry point for all client requests
   - Routes requests to appropriate microservices
   - Handles cross-cutting concerns

3. **User Service (Port: 7072)**
   - Manages user-related operations
   - Handles user authentication and authorization

4. **Blog Info Service (Port: 7070)**
   - Manages blog post information
   - Handles blog content and metadata

5. **Blog Business Service (Port: 7071)**
   - Handles business logic for blog operations
   - Processes blog-related transactions

6. **Blog Comments Service (Port: 7075)**
   - Manages blog comments
   - Handles comment-related operations

7. **MongoDB (Port: 27017)**
   - Primary database for all services
   - Stores data in a distributed manner

## Requirements

- Java 11+
- Maven
- Docker and Docker Compose
- MongoDB (included in Docker setup)

## How to Run the Project

### Using Docker (Recommended)

The easiest way to run the entire system is using Docker Compose. This will start all services with the correct configuration and networking.

1. Make sure Docker and Docker Compose are installed on your system
2. From the project root directory, run:

```bash
docker-compose up --build
```

Or to run in detached mode:

```bash
docker-compose up -d
```

### Accessing Services

Once all services are running, you can access them at:

- Service Discovery Dashboard: http://localhost:8761
- API Gateway: http://localhost:8084
- Swagger UI (if available): http://localhost:8084/swagger-ui/index.html

## Service Communication

The services communicate with each other through the API Gateway, which routes requests to the appropriate microservice. The Service Discovery (Eureka) server helps in locating services dynamically.

## Database

MongoDB is used as the primary database, running in a Docker container. Each service can have its own database or share the same database with different collections, depending on the implementation.

## Development

### Building Individual Services

To build a specific service:

```bash
cd <service-directory>
./mvnw clean install
```

### Running Services Locally

For local development, you can run services individually:

1. Start MongoDB (if not using Docker)
2. Start the Service Discovery server
3. Start the API Gateway
4. Start other services as needed

Update the `application.properties` in each service to point to the correct MongoDB instance and other service URLs.

## API Documentation

API documentation is available through Swagger UI when accessing the API Gateway. The documentation includes all available endpoints and their usage.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

