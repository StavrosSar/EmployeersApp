# Employee Manager

Spring Boot + Angular + MySQL app for employee CRUD.

## Testing (JUnit 5 + Mockito)

The backend test suite uses:

- **JUnit 5** for test lifecycle and assertions
- **Mockito** for mocking dependencies (for example, mocking `EmployeeRepo` or `EmployeeService`)

Current backend test classes include:

- `employeemanager/src/test/java/tech/getarrays/employeemanager/service/EmployeeServiceTest.java`
- `employeemanager/src/test/java/tech/getarrays/employeemanager/EmployeeResourceTest.java`
- `employeemanager/src/test/java/tech/getarrays/employeemanager/EmployeemanagerApplicationTests.java`
- `employeemanager/src/test/java/tech/getarrays/employeemanager/model/EmployeeTest.java`

Run backend tests:

```bash
cd employeemanager
./mvnw test
```

On Windows PowerShell:

```powershell
cd employeemanager
.\mvnw.cmd test
```

## Docker Run Guide

The project uses two compose files:

- `docker-compose.api.yml` for `db` + `api`
- `docker-compose.frontend.yml` for `frontend`

### 1. Prerequisites

- Docker Desktop running
- `.env` file in repo root with:

```env
MYSQL_ROOT_PASSWORD=your_password
MYSQL_DATABASE=employeemanager
```

### 2. One-time setup (required by current compose files)

```bash
docker network create employeemanager_shared
docker volume create employeersapp_db_data
```

### 3. Start backend + database

```bash
docker compose -f docker-compose.api.yml up -d --build
```

Backend API: `http://localhost:8080`

MySQL exposed on host: `localhost:3307`

### 4. Start frontend

```bash
docker compose -f docker-compose.frontend.yml up -d --build
```

Frontend: `http://localhost:4200`

### 5. Stop services

```bash
docker compose -f docker-compose.frontend.yml down
docker compose -f docker-compose.api.yml down
```

### 6. Restart existing containers (without rebuild)

```bash
docker compose -f docker-compose.api.yml start
docker compose -f docker-compose.frontend.yml start
```
