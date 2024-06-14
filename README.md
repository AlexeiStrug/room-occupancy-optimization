# Room Allocation Service

This project is a Spring Boot application that allocates hotel rooms based on guest bids. It contains a service to handle the room allocation logic and a REST API to expose this functionality.

## Requirements

- Java 17
- Spring boot 3.3.0
- Maven 3.6.0 or higher

## Getting Started

### Clone the repository

```bash
git clone https://github.com/AlexeiStrug/room-occupancy-optimization.git
cd room-occupancy-optimization
```

### Build the project

```bash
mvn clean install
```

### Running the Application
To run the application, use the following Maven command:

```bash
mvn spring-boot:run
```

Alternatively, you can run the generated JAR file:

```bash
java -jar target/room-occupancy-optimization-0.0.1-SNAPSHOT.jar
```

### Running Tests

To run the tests, use the following Maven command:

```bash
mvn test
```

## API Endpoints

### Allocate Rooms

- **URL:** `/api/v1/allocate`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Request Body:**
  ```json
  {
      "premiumRooms": 7,
      "economyRooms": 5
  }
  ```
- **Response Body:**
```json
{
    "usedPremiumRooms": 6,
    "usedEconomyRooms": 4,
    "totalPremiumRevenue": 1054,
    "totalEconomyRevenue": 189.99
}
```

## Configuration

### application.properties

The base path for all API endpoints is set in the application.properties file.

```properties
server.servlet.context-path=/api
```

## Project Structure

  - **src/main/java**: Contains the main application code
  - **src/test/java**: Contains the test code
  - **src/main/resources**: Contains the application properties

