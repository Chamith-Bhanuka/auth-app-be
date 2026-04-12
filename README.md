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
Response:
```json
{
  "success": true,
  "message": "User registered",
  "data": "success"
}

```

### Login User

POST /auth/login

Request:
```json
{
  "email": "john@gmail.com",
  "password": "1234"
}

```
Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0b21AZ21haWwuY29tIiwicm9sZSI6IkNVU1RPTUVSIiwiaWF0IjoxNzc1ODE0NjIyLCJleHAiOjE3NzU4MTgyMjJ9.BW5nhErZF4FU2rqCZWb8r1qwho0SziSP4v211_AzPpU",
  "role": "CUSTOMER"
}
