# Employee Manager (Spring Boot + Angular + MySQL)

A simple full‑stack CRUD application for managing employees.  
It includes a **Spring Boot REST API** (backend) and an **Angular** application (frontend) connected to a **MySQL** database.

---

## Features

- ✅ Create employee
- ✅ Read/list employees
- ✅ Update employee
- ✅ Delete employee
- ✅ REST API + Angular UI
- ✅ MySQL persistence using Spring Data JPA/Hibernate

---

## Tech Stack

### Backend
- **Java** (tested with Java 25)
- **Spring Boot 4**
- **Spring Web (REST)**
- **Spring Data JPA**
- **Hibernate**
- **MySQL Connector/J**
- **Maven**

### Frontend
- **Angular** (standalone-style project structure)
- **TypeScript**
- **HttpClient / RxJS**
- CSS (default Angular styling)

### Database
- **MySQL** (running locally on port `3306`)

---

## Project Structure

EmployeersApp/
│
├── employeemanager/ # Spring Boot Backend (REST API)
│ ├── src/main/java/ # Java source code
│ ├── src/main/resources/
│ │ └── application.properties # Database configuration
│ └── pom.xml # Maven configuration
│
└── employeemanagerapp/ # Angular Frontend (UI)
├── src/app/ # Angular components & services
├── package.json # Node dependencies
└── angular.json # Angular configuration


---

## Prerequisites

Make sure you have the following installed:

- **Java JDK** (21+ recommended, tested with 25)
- **Maven** (or use the included Maven Wrapper `mvnw`)
- **Node.js** (LTS recommended)
- **Angular CLI**
  ```bash
  npm install -g @angular/cli



Database Setup (MySQL)

Start the MySQL Server.

Create the database schema:
CREATE DATABASE IF NOT EXISTS employeemanager;
