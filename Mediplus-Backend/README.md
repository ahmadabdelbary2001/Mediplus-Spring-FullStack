## Mediplus Backend

### Tech Stack (Backend)

- **Java 17+**  
- **Spring Boot 3.x**  
- **Spring Data JPA (Hibernate)**  
- **Spring Security**  
- **Lombok**  
- **H2 (in-memory) / MySQL / PostgreSQL (configurable)**  
- **JUnit 5 / Mockito** for testing  

### Backend Project Structure

```
medi-plus-backend/
├── src/
│   ├── main/
│   │   ├── java/org/mediplus/
│   │   │   ├── admin/                # Admin entity, controller, service, repository
│   │   │   ├── appointment/          # Appointment entity, controller, service, repository
│   │   │   ├── doctor/               # Doctor entity, controller, service, repository
│   │   │   ├── notification/         # Notification entity, controller, service, repository
│   │   │   ├── patient/              # Patient entity, controller, service, repository
│   │   │   ├── user/                 # User (abstract), UserService, UserController, repository, security
│   │   │   ├── config/               # Security, CORS, PasswordEncoder, custom UserDetailsService
│   │   │   ├── DataInitializer.java  # Demo data loader
│   │   │   └── MediPlusApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/
│       └── java/org/mediplus/        # Unit and integration tests
```


### 🚀 Getting Started (Backend)

1. **Database Setup**  
   - By default, H2 in-memory is used.  
   - For MySQL/postgreSQL, update `application.properties` with the appropriate JDBC URL, credentials, and Hibernate dialect.

2. **Environment Configuration**  
   - Create a `.env` (or set environment variables) for:
     ```
     SPRING_DATASOURCE_USERNAME=...
     SPRING_DATASOURCE_PASSWORD=...
     SPRING_DATASOURCE_URL=...
     JWT_SECRET_KEY=...           # (if JWT is used in the future)
     ACTUATOR_USER=...            # (if using Spring Actuator)
     ACTUATOR_PASSWORD=...
     ```

3. **Build & Run**  
   ```bash
   mvn clean install
   mvn spring-boot:run
