# Football Manager

Football Manager is an application for managing football teams and players. It allows creating teams, adding players, and transferring players between teams.

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.5.4
- **Frontend**: React
- **Database**: PostgreSQL 17
- **Containerization**: Docker, Docker Compose

## Running with Docker

### Prerequisites

- Docker and Docker Compose installed

### Running the Application

1. Clone the repository:
```bash
git clone <repository-url>
cd football-manager
```


2. From the project root directory, run:
```bash
docker-compose up --build
```


This will start three services:
- PostgreSQL database (port 5432)
- Backend API (port 8080)
- Frontend application (port 3000)

3. Access the application:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080

4. If you want to test Postman, please import the file first and then run the collection, then start manipulating the data on the site.
### Stopping the Application

To stop the application, press `Ctrl+C` in the terminal where docker-compose is running, or run:
```bash
docker-compose down
```


This will stop and remove all containers created by docker-compose.