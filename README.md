# ClientFlow CRM

ClientFlow CRM is a web-based Customer Relationship Management application built with Java and Spring Boot. It provides a centralized system for managing customers, leads, sales pipeline stages, interactions, follow-up tasks, reports, and application settings.

## Features

- User authentication with Spring Security
- Role-based authorization
- Admin and Sales roles
- Customer management
- Lead management
- Sales pipeline management
- Lead stage updates
- Customer and lead archiving
- Customer interactions
- Follow-up task management
- Dashboard with CRM statistics
- Reports
- Application settings
- MySQL database persistence
- Thymeleaf-based web interface
- Docker and Docker Compose support

## Technology Stack

### Backend
- Java 17
- Spring Boot 4.1.1
- Spring MVC
- Spring Data JPA
- Hibernate
- Spring Security
- Maven

### Frontend
- Thymeleaf
- HTML
- CSS
- JavaScript
- Bootstrap Icons

### Database
- MySQL 8.0

### DevOps
- Docker
- Docker Compose
- Git
- GitHub

## Architecture

The application follows a layered Spring Boot architecture:

```text
Browser
   |
   v
Controller
   |
   v
Service
   |
   v
Repository
   |
   v
MySQL
```

The main layers are:

- **Controller** — handles HTTP requests and application navigation
- **Service** — contains business logic
- **Repository** — handles database access using Spring Data JPA
- **Entity** — represents persistent database models
- **DTO** — handles form data between the web layer and application
- **Thymeleaf Templates** — provides the server-side web interface

## Main Modules

### Dashboard
Provides an overview of CRM activity including customer and lead statistics.

### Customers
Allows users to:

- View customers
- Add customers
- Edit customers
- Search customers
- Manage customer status
- Archive customers

Customer archiving is restricted to the Admin role.

### Leads & Sales
Provides lead management with stages such as:

- New
- Contacted
- Qualified
- Proposal
- Won
- Lost

### Sales Pipeline
Provides a visual pipeline for tracking leads through different sales stages.

Both Admin and Sales users can update pipeline stages.

### Interactions
Stores and manages customer/lead interactions.

### Follow-up Tasks
Provides task management for CRM follow-ups.

### Reports
Provides CRM reporting and business information.

### Settings
Provides application-level settings and configuration functionality.

## Authentication and Authorization

ClientFlow CRM uses Spring Security for authentication and authorization.

Supported roles:

- `ADMIN`
- `SALES`

Roles are stored in the database without the `ROLE_` prefix. Spring Security adds the required prefix internally.

Example:

```text
ADMIN
SALES
```

The application uses BCrypt password hashing for stored passwords.

## Database

The application uses MySQL.

Main database tables include:

```text
app_users
customers
leads
interactions
tasks
```

For local development, MySQL can run through Docker Compose.

## Running with Docker

Build the application image:

```bash
docker build -t clientflow-crm .
```

Start the application and MySQL:

```bash
docker compose up -d
```

Check running containers:

```bash
docker compose ps
```

View application logs:

```bash
docker compose logs app --tail=100
```

The application is available at:

```text
http://localhost:8080
```

MySQL is exposed locally on:

```text
localhost:3307
```

The application connects to MySQL through the Docker Compose service name:

```text
jdbc:mysql://mysql:3306/clientflow_crm
```

## Docker Architecture

```text
                    Client Browser
                         |
                         | :8080
                         v
                +-------------------+
                |  Spring Boot App  |
                |  clientflow-app   |
                +---------+---------+
                          |
                          | JDBC
                          v
                +-------------------+
                |     MySQL 8.0     |
                |  clientflow-mysql |
                +-------------------+
                          |
                          v
                clientflow_mysql_data
                     Docker Volume
```

## Project Structure

```text
clientflow-crm/
├── src/
│   ├── main/
│   │   ├── java/com/clientflow/crm/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── static/
│   │       ├── templates/
│   │       └── application.properties
│   └── test/
├── Dockerfile
├── docker-compose.yml
├── .env.example
├── .gitignore
├── pom.xml
├── mvnw
└── README.md
```

## Security

Sensitive configuration should be supplied through environment variables rather than committed directly to GitHub.

The project includes:

```text
.env.example
```

as a template for required environment variables.

Actual `.env` files and sensitive credentials should not be committed to the repository.

## Development

Clone the repository:

```bash
git clone https://github.com/sunny-kumar12/clientflow-crm.git
```

Enter the project:

```bash
cd clientflow-crm
```

Build the Docker image:

```bash
docker build -t clientflow-crm .
```

Start the application:

```bash
docker compose up -d
```

## Future Deployment

The project is containerized and can be deployed to a cloud environment using Docker.

Possible production architecture:

```text
GitHub
   |
   v
Docker Image
   |
   v
Cloud Server
   |
   +---- Spring Boot Container
   |
   +---- MySQL Database
```

## Author

**Sunny Kumar**

B.Tech – Computer Science & Engineering

GitHub: https://github.com/sunny-kumar12
