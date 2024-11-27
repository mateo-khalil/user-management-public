# Pre-requisites

1. Java 17 or higher
2. Maven 3.8 or higher

# Description:

The application will automatically load default data from `src/main/resources/data.sql`.

## Database Configuration

Before running the application, ensure the database instance is active and functioning. By default, the application uses
an H2 in-memory database, but you can configure it for other databases (e.g., MySQL or PostgreSQL) by updating the
`application.properties` file.

### Optional: Using Docker

If you prefer to use Docker for the database, follow these steps:

1. Ensure Docker and Docker Compose are installed on your system.
2. Create a `docker-compose.yml` file. You can use the example configuration below as a starting
   point. [Example Docker Compose Configuration](#example-docker-compose-configuration)
3. Run the following command to start the database:
   ```
   docker-compose up
   ```
4. If the database is not initialized correctly, manually execute the script located at `init-scripts/init.sql`.

### Example Docker Compose Configuration

```
services:

  db:
    image: mysql:8.0
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: password
      MYSQL_DATABASE: asur_db
      MYSQL_USER: user
      MYSQL_PASSWORD: password
    ports:
      - "3306:3306"
    volumes:
      - db_data:/var/lib/mysql

volumes:
  db_data:
```

## Running the Application

Once the database is configured and running, execute the following command to start the application:

```
mvn spring-boot:run
```

### Troubleshooting

- Ensure the database connection settings in `application.properties` match your database setup.
- If using Docker and facing issues, confirm that the containers are running correctly with:
  ```
  docker ps
  ```
- For connection issues, ensure the application is pointing to the correct host and port for the database.

---
This README provides a simple guide for setting up and running the application with or without Docker. Feel free to
customize it for your specific environment.
