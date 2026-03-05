🏥 Patient Management System

Aplicación full-stack para la gestión de pacientes construida con:

Frontend: React (Vite)

Backend: Java Spring Boot

Base de datos: MySQL

Contenedores: Docker & Docker Compose

CI/CD: Jenkins

Code Quality: SonarQube

La aplicación permite crear, consultar, actualizar y eliminar pacientes (CRUD) a través de una interfaz web moderna.

📸 Arquitectura

React (Frontend)
       │
       │ REST API
       ▼
Spring Boot (Backend)
       │
       ▼
MySQL Database

Con Docker:
Frontend (Nginx)
        │
        ▼
Backend (Spring Boot)
        │
        ▼
MySQL


📂 Estructura del proyecto

patient-management
│
├─ backend
│   ├─ src
│   ├─ pom.xml
│   └─ Dockerfile
│
├─ frontend
│   └─ patients-frontend
│        ├─ src
│        └─ Dockerfile
│
├─ docker-compose.yml
└─ README.md


