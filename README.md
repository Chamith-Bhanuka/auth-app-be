# Spring Boot JWT Role-Based Access Control (RBAC) Project

This project is a **Spring Boot REST API** with **JWT authentication** and **role-based authorization**, connected to a React frontend.

---

## 🚀 Tech Stack

- Java 17
- Spring Boot
- Spring Security (JWT)
- MySQL
- JPA / Hibernate
- Maven
- React (Frontend)
- Axios
- Tailwind CSS

---

## 🔐 Roles in System

We have 4 roles:

- **SUPER_ADMIN**
- **TENANT**
- **EMPLOYEE**
- **CUSTOMER**

---

## 📊 Role Permissions

| Role        | Access Level        |
|-------------|---------------------|
| SUPER_ADMIN | All 7 endpoints     |
| TENANT      | 5 endpoints         |
| EMPLOYEE    | 4 endpoints         |
| CUSTOMER    | 1 endpoint          |

---

## 🌐 API BASE URL

   ```bash
   http://localhost:8080
```

## 🔑 AUTH ENDPOINTS

### Register User

POST /auth/register


Request:
```json
{
  "name": "John",
  "email": "john@gmail.com",
  "password": "1234",
  "role": "CUSTOMER"
}

```

### Register User

POST /auth/register

Request:
```json
{
  "name": "John",
  "email": "john@gmail.com",
  "password": "1234",
  "role": "CUSTOMER"
}